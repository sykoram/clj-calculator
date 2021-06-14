(ns calculator.config
  (:require [calculator.math :as m]))

(def operators
  {:add {:fn +', :preced 2, :args 2, :assoc :left}
   :sub {:fn -', :preced 2, :args 2, :assoc :left}
   :mul {:fn *', :preced 3, :args 2, :assoc :left}
   :div {:fn /, :preced 3, :args 2, :assoc :left}
   :unary- {:fn -', :preced 4, :args 1}
   :pow {:fn m/pow, :preced 5, :args 2, :assoc :right}
   :fact {:fn m/fact, :preced 6, :args 1}})

(def operator-parse-map ; w/o unary minus
  {"+" :add, "-" :sub, "*" :mul, "/" :div, "^" :pow, "!" :fact})


; arc-, ncr, npr, gcd, lcm, mod
(def functions
  {:sin {:fn #(Math/sin %), :args 1}
   :cos {:fn #(Math/cos %), :args 1}
   :tan {:fn #(Math/tan %), :args 1}
   :cot {:fn #(/ (Math/tan %)), :args 1}
   :sqrt {:fn #(Math/sqrt %), :args 1}
   :cbrt {:fn #(Math/cbrt %), :args 1}
   :ln {:fn #(Math/log %), :args 1}
   :log10 {:fn #(Math/log10 %), :args 1}
   :log {:fn #(/ (Math/log %2)
                 (Math/log %1)), :args 2}
   :log2 {:fn #(/ (Math/log %)
                  (Math/log 2)), :args 1}
   :floor {:fn #(Math/floor %), :args 1}
   :ceil {:fn #(Math/ceil %), :args 1}
   :round {:fn #(Math/round %), :args 1}
   :sgn {:fn m/sgn, :args 1}
   :abs {:fn m/abs, :args 1}
   :double {:fn double, :args 1}})

(def function-parse-map
  {"sin" :sin, "cos" :cos, "tan" :tan, "tg" :tan, "cot" :cot, "cotg" :cot
   "sqrt" :sqrt, "cbrt" :cbrt, "sgn" :sgn, "abs" :abs
   "floor" :floor, "ceil" :ceil, "round" :round
   "ln" :ln, "log10" :log10, "log" :log, "log2" :log2, "lb" :log2
   "double" :double})


(def constants
  {:pi {:val (Math/PI)}
   :e {:val (Math/E)}
   :phi {:val (/ (+ 1 (Math/sqrt 5))
                 2)}})

(def constant-parse-map
  {"pi" :pi, "e" :e, "phi" :phi, "golden_ratio" :phi})


(def specials
  #{:l-paren, :r-paren, :sep})

(def special-parse-map
  {"(" :l-paren, "[" :l-paren
   ")" :r-paren, "]" :r-paren
   "," :sep, ";" :sep})
