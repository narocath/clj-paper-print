(ns clj-paper-print.printable
    (:require [clojure.spec.alpha :as s]
              [clj-paper-print.graphics :refer [graphics-methods]]
              [clojure.spec.alpha :as s]
              [expound.alpha :as expound]
              [spec.printable_spec :as prs])
    (:import [java.awt.print Printable]
             [java.awt Graphics Font]
             [javax.swing DebugGraphics]))

(defn op-file
  "A function that open a file and use the `(graphics-methods :draw-string)` to print a file to the printer.
The function arguments is the file name as String the Graphics object and the x and y points to print 
the contents of the file in the paper."
    [^String file ^Graphics g xpoint ypoint]
    (with-open [rdr (clojure.java.io/reader file)]
        (doseq [l (line-seq rdr)]
            (graphics-methods [:draw-string ^Graphics g l xpoint @ypoint])
            (swap! ypoint #(+ % (.getHeight (.getFontMetrics g)))))))


(defmacro printable-one-page-func
  "A macro that will create the Printable object, for one page document with further configuration.
  The macro accepts the function symbol that uses the `clj-paper-print.graphics2d` methods in its body.
  The function that will be supplied should have 3 arguments which the
  1st: should be the equivelant `java.awt.Graphics` object,
  2nd: `java.awt.print.PageFormat`
  3rd: the page index as int."
  [f]
  `(reify Printable
    (print [this# g# pf# p#]
      (if (= p# 0)
        (do
          ((ns-resolve *ns* ~f) g# pf# p#)
          Printable/PAGE_EXISTS)
        Printable/NO_SUCH_PAGE))))
(defmacro printable-multi-page-func
  "A macro that will create the Printable object, for multi page document, with further configuration.
  The macro accepts the function symbol that uses the `clj-paper-print.graphics2d` methods in its body.
  The function that will be supplied should have 3 arguments which the
  1st: should be the equivelant `java.awt.Graphics` object,
  2nd: `java.awt.print.PageFormat`
  3rd: the page index as int."
  [f]
  `(reify Printable
     (print [this g# pf# p#]
       (if (>= p# 0)
         (do
           ((ns-resolve *ns* ~f) g# pf# p#)
           Printable/PAGE_EXISTS)
         Printable/NO_SUCH_PAGE))))

(defn printable-one-page
"A function that create Printable objects to be printed as a single page."
    [graph-vec]
    (reify Printable
        (print [this g pgf p]
                (if (== p 0)
                    (do
                      (graphics-methods [:translate ^Graphics g (int (.getImageableX pgf)) (int (.getImageableY pgf))])
                      (dorun (map #(graphics-methods %) (map #(into (conj (subvec % 0 1) ^Graphics g) (subvec % 1)) graph-vec)))
                        (.dispose ^Graphics g)
                        Printable/PAGE_EXISTS)
                    Printable/NO_SUCH_PAGE))))
(defn printable-multi-page
"A function that create Printable ojcects that each one will be a page in a java Book object."
    [graph-vec]
    (reify Printable
        (print [this g pf p]
                (if (>= p 0)
                    (do
                        (graphics-methods [:translate ^Graphics g (.getImageableX pf) (.getImageableY pf)]) 
                        (dorun (map #(graphics-methods %) (map #(into (conj (subvec % 0 1) ^Graphics g) (subvec % 1)) graph-vec)))
                        (.dispose ^Graphics g)
                        Printable/PAGE_EXISTS)
                    Printable/NO_SUCH_PAGE))))
(defmulti printable
"A multimethod that will create the appropriate Printable for the printjob.
The multi accepts a vector which consists in the action for the appropriate
  method along  with the graphics  that will executed to create the Printable and the required arguments.
The methods change the state of the user vector for each graphics method by adding the
  current Graphics `g` object so the `graphics-methods` will be able to executed and in turn printed.
The input of each one method:
e.g (printable [:action [[:draw-string ['foo' 10 10]], [:set-font ...], [...]]]) -> (printable [:action [[:draw-string `g` ['foo' 10 10]], []]])."
  (fn [[action graph & rest :as cfg]]
    (if-not (s/valid? ::prs/printable-multi-input cfg)
      (throw (ex-info "printable input failed: " (expound/expound ::prs/printable-multi-input cfg)))
      action)))
(defmethod printable :one-page
    [[_ graph]]
  (printable-one-page graph))
(defmethod printable :one-page-func
  [[_ f]]
  (printable-one-page-func f))
(defmethod printable :multi-page
    [[_ graph]]
  (printable-multi-page graph))
(defmethod printable :multi-page-func
  [[_ f]]
  (printable-multi-page-func f))
(defmethod printable :txt
    [[_ [fname x y cfg]]]
    (let [file fname graph-vec cfg xpoint x ypoint (atom y) ]
        (reify Printable
            (print [this g pf p]
                (if (= p 0)
                (do
                    (if (empty? graph-vec) ;;default font/color ...
                        (do 
                            (graphics-methods [:translate ^Graphics g [(.getImageableX pf) (.getImageableY pf)]])
                            (op-file file g xpoint ypoint)
                            (.dispose ^Graphics g)
                            Printable/PAGE_EXISTS)
                        (do 
                            (graphics-methods [:translate ^Graphics g [(.getImageableX pf) (.getImageableY pf)]])
                            (doall (map #(graphics-methods %) (map #(into (conj (subvec % 0 1) ^Graphics g) (subvec % 1)) graph-vec)))
                            (op-file file g xpoint ypoint)
                            (.dispose ^Graphics g)
                            Printable/PAGE_EXISTS)))
                Printable/NO_SUCH_PAGE)))))


