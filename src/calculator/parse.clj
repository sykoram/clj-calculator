(ns calculator.parse
  (:require [calculator.config :as c]))

(defn separate-tokens [strng]
  (map first (re-seq #"(\d+\.?\d*)|[+*/^()\[\]{}!?@&~%:|-]|[a-z][a-z0-9_]*|,|;" strng)))

(defn parse-constant [strng]
  (cond
    (re-matches #"\d+" strng) (Integer/parseInt strng 10)
    (re-matches #"\d+\.\d+" strng) (Double/parseDouble strng)
    (contains? c/constant-parse-map strng) (c/constant-parse-map strng)
    :else strng))

(defn parse-expression [expr]
  (reduce (fn [sq tok]
            (let [prev (last sq)]
              (conj sq
                    (or
                     (and (= "-" tok) ; hard-coded unary minus
                          (or (nil? prev)
                              (#{:l-paren :sep} prev)
                              (contains? c/operators prev)
                              (contains? c/functions prev))
                          :unary-)
                     (c/operator-parse-map tok)
                     (c/function-parse-map tok)
                     (c/special-parse-map tok)
                     (parse-constant tok)))))
          [] (separate-tokens expr)))
