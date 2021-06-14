(ns calculator.eval
  (:require [calculator.config :as c]
            [calculator.utils :as u]))

; https://www.engr.mun.ca/~theo/Misc/exp_parsing.htm#shunting_yard
(defn operator> [id1 id2]
  (let [op1 (c/operators id1)
        op1-arity (:args op1)
        op1-preced (:preced op1)
        op2 (c/operators id2)
        op2-arity (:args op2)
        op2-preced (:preced op2)]
    (or
     ; binary x > binary y: if x has higher precedence than y, or x is left associative and x and y have equal precedence.
     (and (= op1-arity 2) (= op2-arity 2)
          (or (> op1-preced op2-preced)
              (and (= op1-preced op2-preced)
                   (= (:assoc op1) :left))))
     ; unary x > binary y: if x has precedence higher or equal to y's
     (and (= op1-arity 1) (= op2-arity 2)
          (>= op1-preced op2-preced))
     ; x > unary y: never
     )))

(defn shunting-yard [tokens]
  (loop [tokens tokens
         op-stack '()
         output []]
    (if (empty? tokens)
      (concat output op-stack)
      (let [tok (first tokens)]
        (cond
          (or (number? tok) (c/constants tok)) (recur (rest tokens) op-stack (conj output tok))
          (c/functions tok) (recur (rest tokens) (conj op-stack tok) output)
          (= :sep tok) (recur (rest tokens) op-stack output)
          (= :l-paren tok) (recur (rest tokens) (conj op-stack tok) output)
          (= :r-paren tok) (let [move-ops (take-while #(not= :l-paren %) op-stack)
                                 new-op-stack (drop (inc (count move-ops)) op-stack)
                                 first-op (first new-op-stack)
                                 fn-first-op? (c/functions first-op)
                                 new-op-stack (if fn-first-op? (rest new-op-stack) new-op-stack)
                                 new-output (vec (concat output move-ops))
                                 new-output (if fn-first-op? (conj new-output first-op) new-output)]
                             (recur (rest tokens) new-op-stack new-output))
          (c/operators tok) (let [move-ops (take-while #(and (not= :l-paren %)
                                                             (operator> % tok))
                                                       op-stack)]
                                 (recur (rest tokens)
                                        (conj (drop (count move-ops) op-stack) tok)
                                        (vec (concat output move-ops)))))))))

(defn eval-rpn [tokens]
  (first
   (reduce (fn [out tok]
             (cond
               (number? tok) (conj out tok)
               (c/constants tok) (conj out (get-in c/constants [tok :val]))
               :else (let [op-fn (or (c/operators tok) (c/functions tok))
                           n-args (:args op-fn)]
                       (conj (drop n-args out)
                             (u/smart-cast (apply (:fn op-fn) (reverse (take n-args out))))))))
           '() tokens)))

(defn evaluate-tokenized [tokens]
  (eval-rpn (shunting-yard tokens)))
