(ns calculator.math)

(defn abs [x]
  (if (neg? x) (- x) x))

(defn sgn [x]
  (cond (pos? x) 1
        (neg? x) -1
        :else 0))

(defn fact [n]
  (if (and (integer? n) (>= n 0))
    (reduce *' (range 1 (inc n)))
    (throw (ArithmeticException. "Not a natural number or zero"))))

(defn pow [x y]
  (if (integer? y)
    (if (>= y 0)
      (reduce *' (repeat y x))
      (/ (pow x (- y))))
    (Math/pow x y)))
