(ns calculator.utils)

(defn smart-cast [val]
  (if (number? val)
    (let [int-val (bigint val)]
      (if (== val int-val) int-val val))
    val))
