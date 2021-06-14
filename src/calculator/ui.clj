(ns calculator.ui
  (:require [clojure.string :as s]))

(defn print-intro []
  (println "Hi, I'm a calculator!")
  (println "Available commands:")
  (println "  help, ?, h")
  (println "  exit, quit, q")
  (println "You may enter an expression:")
  (flush))

(defn prompt-input []
  (newline)
  (flush)
  (s/lower-case (read-line)))

(defn print-result [r]
  (println (str "= " r))
  (flush))

(defn exit? [input]
  (#{"exit" "quit" "q"} input))

(defn help? [input]
  (#{"help" "?" "h"} input))

(defn print-help []
  (println "Available commands:")
  (println "  help (?, h)")
  (println "  exit (quit, q)")
  (println "Supported operations:")
  (println "  + - * / ^ !")
  (println "Supported functions:")
  (println "  sqrt(x) cbrt(x)")
  (println "  sin(x) cos(x) tan(x) [tg(x)] cot(x) [cotg(x)]")
  (println "  log(base,x) ln(x) log10(x) log2(x) [lb(x)]")
  (println "  abs(x) sgn(x) floor(x) ceil(x) round(x)")
  (println "  double(x)")
  (println "Supported constants:")
  (println "  pi e phi [golden_ratio]")
  (println "Parentheses are also supported")
  (flush))
