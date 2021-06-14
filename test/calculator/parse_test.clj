(ns calculator.parse-test
  (:require [clojure.test :refer [deftest testing is]]
            [calculator.parse :refer [separate-tokens parse-constant parse-expression]]))

(deftest separate-tokens-test
  (testing "separate-tokens function"
    (is (= ["20"] (separate-tokens "20")))
    (is (= ["85.4"] (separate-tokens "85.4")))
    (is (= ["2" "+" "2.1"] (separate-tokens "2 +2.1")))
    (is (= ["(" "2" "/" "(" "2" "+" "3.33" ")" "*" "4" ")" "-" "-" "6"] (separate-tokens "(2 / (2 + 3.33) * 4) - -6")))
    (is (= ["(" "-" "2" ")" "^" "(" "5" "-" "3" ")"] (separate-tokens " (-  2)^  (5-  3 )  ")))
    (is (= ["-" "sin" "(" "3" "+" "0.14" ")"] (separate-tokens "-sin(3 + 0.14)")))
    (is (= ["5" "*" "log" "(" "2" "," "8" ")" "+" "5"] (separate-tokens "5*log(2, 8)+5")))))

(deftest parse-constant-test
  (testing "integer"
    (is (= 0 (parse-constant "0")))
    (is (= 7 (parse-constant "07")))
    (is (= 23 (parse-constant "23")))
    (is (= 1059870 (parse-constant "1059870"))))
  (testing "floating point number"
    (is (= 0.0 (parse-constant "0.0")))
    (is (= 7.8 (parse-constant "7.8")))
    (is (= 23.46 (parse-constant "023.46000")))
    (is (= 1059870.123456 (parse-constant "1059870.123456"))))
  (testing "constants"
    (is (= :pi (parse-constant "pi")))
    (is (= :e (parse-constant "e"))))
  (testing "unknown"
    (is (= "" (parse-constant "")))
    (is (= "test" (parse-constant "test")))))

(deftest parse-expression-test
  (testing "constants"
    (is (= [0] (parse-expression "0")))
    (is (= [1] (parse-expression "1")))
    (is (= [42] (parse-expression "42")))
    (is (= [4.2] (parse-expression "4.2")))
    (is (= [:e] (parse-expression " e ")))
    (is (= [:pi] (parse-expression "pi")))
    (is (= [:phi] (parse-expression "phi")))
    (is (= [:phi] (parse-expression "golden_ratio")))
    (is (= [150.25] (parse-expression "150.25"))))
  (testing "basic operators"
    (is (= [:unary- 10] (parse-expression "-10")))
    (is (= [10 :add 20] (parse-expression "10+20")))
    (is (= [10 :add 20] (parse-expression "10+ 20")))
    (is (= [10 :add 20] (parse-expression "10 +20")))
    (is (= [10 :add 20] (parse-expression "10 + 20")))
    (is (= [10 :sub 20] (parse-expression "10-20")))
    (is (= [10 :mul 20] (parse-expression "10*20")))
    (is (= [10 :div 20] (parse-expression "10/20")))
    (is (= [10 :pow 3] (parse-expression "10^3")))
    (is (= [10 :fact] (parse-expression "10!"))))
  (testing "simple expression"
    (is (= [10 :sub :unary- 20] (parse-expression "10--20")))
    (is (= [20 :add 4 :mul 10] (parse-expression "20+4*10")))
    (is (= [20 :div 5 :sub :unary- 2] (parse-expression "20/5--2")))
    (is (= [5 :fact :add 2] (parse-expression "5! +2"))))
  (testing "expression with parentheses"
    (is (= [:l-paren 5 :r-paren] (parse-expression "(5)")))
    (is (= [:l-paren 5 :add 2 :r-paren :mul 10] (parse-expression "(5+2)*10")))
    (is (= [:l-paren 5 :add 2 :r-paren :fact] (parse-expression "(5+2)!")))
    (is (= [:l-paren :l-paren 5 :add 2 :r-paren :mul 10 :r-paren] (parse-expression "[(5+2)*10]")))
    (is (= [:l-paren 10 :sub :l-paren 5 :add 2 :r-paren :r-paren :mul 10] (parse-expression "(10-(5+2))*10")))
    (is (= [:unary- :l-paren :unary- 1 :sub :unary- 1 :r-paren :sub :l-paren 1 :r-paren :sub :unary- :l-paren :unary- 1 :r-paren] (parse-expression "-(-1--1)-(1)--(-1)"))))
  (testing "functions"
    (is (= [:unary- :sin :l-paren 3 :add 0.14 :r-paren] (parse-expression "-sin(3 + 0.14)")))
    (is (= [:cos :l-paren 2 :mul :pi :r-paren :pow 2] (parse-expression "cos(2*pi)^2")))
    (is (= [:log10 :l-paren :sqrt :l-paren 100 :r-paren :r-paren] (parse-expression "log10(sqrt(100))")))
    (is (= [5 :mul :sgn :l-paren :unary- 5 :r-paren :mul :ln :l-paren :e :r-paren] (parse-expression "5*sgn(-5)*ln(e)")))
    (is (= [5 :mul :log :l-paren 2 :sep 8 :r-paren :add 5] (parse-expression "5*log(2, 8)+5")))
    (is (= [5 :fact :mul :log :l-paren 2 :sep 8 :r-paren :fact] (parse-expression "5!*log(2,8)!")))))
