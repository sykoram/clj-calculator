(ns calculator.core
  (:gen-class)
  (:require [calculator.ui :as ui]
            [calculator.parse :as p]
            [calculator.eval :as e]))

(defn evaluate [expr]
  (e/evaluate-tokenized (p/parse-expression expr)))

(defn -main [& args]
  (ui/print-intro)
  (loop []
    (let [input (ui/prompt-input)]
      (cond
        (ui/exit? input) 0
        (ui/help? input) (do (ui/print-help)
                             (recur))
        :else (do (ui/print-result (try (evaluate input)
                                        (catch ArithmeticException e (str "Math error: " (.getMessage e)))
                                        (catch Exception _ (str "Error"))))
                  (recur))))))
