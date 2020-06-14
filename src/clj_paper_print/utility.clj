(ns clj-paper-print.utility)
;;
;; Utility helper functions
;;
(defn rounding
  "A multi-arity function  to round the dimensions to decimal points.
  By default rounding at 1 decimal points."
(  [x]
 (double (.setScale (bigdec x) 2 java.math.RoundingMode/HALF_EVEN)))
  ([x y]
   (double (.setScale (bigdec x) y java.math.RoundingMode/HALF_EVEN))))
(defn inches->screen 
  "The java print api is accepting the dimension values on 1/72 of an inch.
The values will be multiplied by 72"
  [^Double d]
  (rounding (* d 72.0)))
(defn screen->inches
  "A helper function to translate java's screen units ()"
  [^Double d]
  (rounding (/ d 72.0)))
(defn mm->screen
  "A helper function to translate mm to java's screen units , (1/72 of inch)."
  [^Double d]
  (rounding (* (/ d 25.4) 72.0)))
(defn screen->mm
  "A helper function to tranlate from java's screen units (1 u = 1/72 inch)."
  [^Double d]
  (rounding (* (/ d 72.0) 25.4)))
