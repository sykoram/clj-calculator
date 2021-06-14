(ns calculator.math-test
  (:require [clojure.test :refer [deftest testing is]]
            [calculator.math :refer [abs sgn fact pow]]))

(deftest abs-test
  (is (= 0 (abs 0)))
  (is (= 25 (abs 25)))
  (is (= 25 (abs -25)))
  (is (= 2.5 (abs 2.5)))
  (is (= 2.5 (abs -2.5))))

(deftest sgn-test
  (is (= 0 (sgn 0)))
  (is (= 1 (sgn 25)))
  (is (= -1 (sgn -25)))
  (is (= 1 (sgn 2.5)))
  (is (= -1 (sgn -2.5))))

(deftest fact-test
  (is (= 1 (fact 0)))
  (is (= 1 (fact 1)))
  (is (= 2 (fact 2)))
  (is (= 6 (fact 3)))
  (is (= 120 (fact 5)))
  (is (= 3628800 (fact 10))))

(deftest pow-test
  (testing "pos-int exponent"
    (is (= 8 (pow 2 3)))
    (is (= 9/25 (pow 3/5 2)))
    (is (= -1 (pow -1 3)))
    (is (= 1 (pow 85 0))))
  (testing "neg-int exponent"
    (is (= 1/9 (pow 3 -2)))
    (is (= -1/125 (pow -5 -3))))
  (testing "other exponent"
    (is (< 1.414213 (pow 2 0.5) 1.414214))))
