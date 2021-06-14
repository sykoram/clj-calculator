(ns calculator.eval-test
  (:require [clojure.test :refer [deftest testing is]]
            [calculator.eval :refer [operator> shunting-yard eval-rpn evaluate-tokenized]]))

(deftest operator-gt-test
  (is (= true (operator> :add :add)))
  (is (= true (operator> :add :sub)))
  (is (= true (operator> :mul :add)))
  (is (= true (operator> :pow :mul)))
  (is (= true (operator> :unary- :add)))
  (is (= true (operator> :unary- :mul)))
  (is (= false (operator> :add :mul)))
  (is (= false (operator> :sub :pow)))
  (is (= false (operator> :div :pow)))
  (is (= false (operator> :unary- :pow)))
  (is (= false (operator> :add :unary-)))
  (is (= false (operator> :mul :unary-)))
  (is (= false (operator> :pow :unary-)))
  )

(deftest shunting-yard-algo-test
  (testing "simple"
    (is (= [0] (shunting-yard [0])))
    (is (= [1 :unary-] (shunting-yard [:unary- 1])))
    (is (= [1 2 :add] (shunting-yard [1 :add 2])))
    (is (= [1 2 :sub] (shunting-yard [1 :sub 2])))
    (is (= [2 :pi :pow] (shunting-yard [2 :pow :pi])))
    (is (= [1 2 :unary- :sub] (shunting-yard [1 :sub :unary- 2])))
    (is (= [3 4 :sub 5 :add] (shunting-yard [3 :sub 4 :add 5])))
    (is (= [3 4 :pow] (shunting-yard [3 :pow 4])))
    (is (= [3 2 :unary- :pow] (shunting-yard [3 :pow :unary- 2])))
    (is (= [3 2 :pow :unary-] (shunting-yard [:unary- 3 :pow 2])))
    (is (= [3 :unary- 2 :pow] (shunting-yard [:l-paren :unary- 3 :r-paren :pow 2])))
    (is (= [4 :sqrt] (shunting-yard [:sqrt :l-paren 4 :r-paren])))
    (is (= [2 8 :log] (shunting-yard [:log :l-paren 2 :sep 8 :r-paren])))
    (is (= [5 :fact] (shunting-yard [5 :fact])))
    (is (= [5 :fact :unary-] (shunting-yard [:unary- 5 :fact])))
    )
  (testing "complex"
    (is (= [3 4 5 :mul :sub] (shunting-yard [3 :sub :l-paren 4 :mul 5 :r-paren]))) ; 3-(4*5)
    (is (= [3 4 :sub 5 :mul] (shunting-yard [:l-paren 3 :sub 4 :r-paren :mul 5]))) ; (3-4)*5
    (is (= [5 3 4 :sub :mul] (shunting-yard [5 :mul :l-paren 3 :sub 4 :r-paren]))) ; 5*(3-4)
    (is (= [5 3 :mul 4 :sub] (shunting-yard [:l-paren 5 :mul 3 :r-paren :sub 4]))) ; (5*3)-4
    (is (= [3 4 2 :mul 1 5 :sub 2 3 :pow :pow :div :add] (shunting-yard [3 :add 4 :mul 2 :div :l-paren 1 :sub 5 :r-paren :pow 2 :pow 3]))) ; 3+4*2/(1-5)^2^3
    (is (= [1 :unary- 2 :unary- :sub :unary- 3 :sub 4 :unary- :unary- :sub] (shunting-yard [:unary- :l-paren :unary- 1 :sub :unary- 2 :r-paren :sub :l-paren 3 :r-paren :sub :unary- :l-paren :unary- 4 :r-paren]))) ; -(-1--2)-(3)--(-4)
    (is (= [10 5 2 :add :sub 20 :mul] (shunting-yard [:l-paren 10 :sub :l-paren 5 :add 2 :r-paren :r-paren :mul 20]))) ; (10-(5+2))*20
    (is (= [1 5 :unary- 4 8 :sub 2 :unary- :pow :mul :add] (shunting-yard [1 :add :unary- 5 :mul :l-paren 4 :sub 8 :r-paren :pow :unary- 2]))) ; 1+-5*(4-8)^-2
    (is (= [2 8 :log 1 :add] (shunting-yard [:log :l-paren 2 :sep 8 :r-paren :add 1])))
    (is (= [1 2 8 :log :add] (shunting-yard [1 :add :log :l-paren 2 :sep 8 :r-paren])))
    (is (= [5 3.14 :sin :mul 3 27 27 :mul :sqrt :log :add] (shunting-yard [5 :mul :sin :l-paren 3.14 :r-paren :add :log :l-paren 3 :sep :sqrt :l-paren 27 :mul 27 :r-paren :r-paren]))) ; 5*sin(3.14)+log(3,sqrt(27*27))
    (is (= [25 :e :e :ln :mul :add :pi 2 :div :sin :sub] (shunting-yard [25 :add :e :mul :ln :l-paren :e :r-paren :sub :sin :l-paren :pi :div 2 :r-paren])))
    (is (= [2 3 :fact :pow :unary-] (shunting-yard [:unary- 2 :pow 3 :fact])))
    (is (= [2 3 :pow :fact :unary- 2 :mul] (shunting-yard [:unary- :l-paren 2 :pow 3 :r-paren :fact :mul 2]))) ; -(2^3)!*2
    ))

(deftest eval-rpn-test
  (testing "simple"
    (is (= 1 (eval-rpn [1])))
    (is (= 2 (eval-rpn [2.0])))
    (is (= 3 (eval-rpn [1 2 :add])))
    (is (= -1 (eval-rpn [1 2 :sub])))
    (is (= 8 (eval-rpn [2 4 :mul])))
    (is (= 1024 (eval-rpn [2 10 :pow])))
    (is (= -1 (eval-rpn [1 2 :unary- :add])))
    (is (= 3 (eval-rpn [9 :sqrt])))
    (is (= 3 (eval-rpn [2 8 :log])))
    (is (= 1 (eval-rpn [0 :fact])))
    (is (= 120 (eval-rpn [5 :fact])))
    (is (= -120 (eval-rpn [5 :fact :unary-])))
    (is (< 3.14159265 (eval-rpn [:pi]) 3.14159266))
    )
  (testing "complex"
    (is (= -17 (eval-rpn [3 4 5 :mul :sub])))
    (is (= 24577/8192 (eval-rpn [3 4 2 :mul 1 5 :sub 2 3 :pow :pow :div :add])))
    (is (= 60 (eval-rpn [10 5 2 :add :sub 20 :mul])))
    (is (= 11/16 (eval-rpn [1 5 :unary- 4 8 :sub 2 :unary- :pow :mul :add])))
    (is (= 16 (eval-rpn [1 5 4 :sqrt 8 :log :mul :add])))
    (is (= -80640 (eval-rpn [2 3 :pow :fact :unary- 2 :mul])))
    (is (< 0.999999 (eval-rpn [2 :pi 6 :div :sin :mul]) 1.000001))
    ))

(deftest evaluate-tokenized-test
  (testing "simple"
    (is (= 42 (evaluate-tokenized [42])))
    (is (= 3 (evaluate-tokenized [1 :add 2])))
    (is (= -1 (evaluate-tokenized [1 :sub 2])))
    (is (= 2 (evaluate-tokenized [1 :mul 2])))
    (is (= 1/2 (evaluate-tokenized [1 :div 2])))
    (is (= 1 (evaluate-tokenized [1 :pow 2])))
    (is (= -120 (evaluate-tokenized [:unary- 5 :fact])))
    )
  (testing "complex"
    (is (= -5 (evaluate-tokenized [:l-paren 3 :sub 4 :r-paren :mul 5])))
    (is (= 24577/8192 (evaluate-tokenized [3 :add 4 :mul 2 :div :l-paren 1 :sub 5 :r-paren :pow 2 :pow 3])))
    (is (= -8 (evaluate-tokenized [:unary- :l-paren :unary- 1 :sub :unary- 2 :r-paren :sub :l-paren 3 :r-paren :sub :unary- :l-paren :unary- 4 :r-paren])))
    (is (= -2 (evaluate-tokenized [:unary- :log :l-paren :sqrt :l-paren 4 :r-paren :sep 8 :r-paren :add 1]))) ; -log(sqrt(4),8)+1
    (is (= 128 (evaluate-tokenized [:unary- 2 :pow 3 :fact :mul :unary- 2]))) ; -2^3!*-2
    (is (= 80640 (evaluate-tokenized [:unary- :l-paren 2 :pow 3 :r-paren :fact :mul :unary- 2]))) ; -(2^3)!*-2
    (is (< 0.9999 (evaluate-tokenized [:sin :l-paren 5 :r-paren :pow 2 :add :cos :l-paren 5 :r-paren :pow 2]) 1.0001)) ; sin(5)^2+cos(5)^2
    ))
