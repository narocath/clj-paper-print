(ns clj-paper-print.pageformat
  (:import [java.awt.print PageFormat])
  (:require [expound.alpha :as expound]
            [clojure.spec.alpha :as s]
            [spec.pageformat-spec :as pfs]
            [clj-paper-print.utility :as u]))





(defn pf-clone
  "A function that will accept a PageFormat object where after spec validation will create a clone of it and
  return the clone."
  [^PageFormat pageformat]
    (try
      (.clone pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-get-h
  "A function that will accept a PageFormat object where after spec validation will return the page height in 1/72 of an inch."
  [^PageFormat pageformat]
    (try
      (.getHeight pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-get-w
  "A function that will accept a PageFormat object where after spec validation will return the page width in 1/72 of an inch."
  [^PageFormat pageformat]
    (try
      (.getWidth pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-get-im-h
  "A function that will accept a PageFormat object where after spec validation will return the page Imageable area height
  in 1/72 of an inch."
  [^PageFormat pageformat]
    (try
      (.getImageableHeight pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-get-im-w
  "A function that will accept a PageFormat object where after spec validation will reutrn the page Imageable area width
  in 1/72 of an inch."
  [^PageFormat pageformat]
    (try
      (.getImageableWidth pageformat)
      (catch Exception e (.getMessage e))))
 (defn pf-get-im-x
  "A function that will accept a PageFormat object where after spec validation will return the x coordinate (upper left)
  of the Paper object of this pageformat."
  [^PageFormat pageformat]
    (try
      (.getImageableX pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-get-im-y
  "A function that will accept a PageFormat object where after spec validation will return the y coordinate (upper left)
  of the Paper object of this pageformat"
  [^PageFormat pageformat]
    (try
      (.getImageableY pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-get-matrix
  "A function that will accept a PageFormat object where after spec validation will return a java array of Doubles with the
  transformation matrix of user space rendering to the requested orientatio of the page."
  [^PageFormat pageformat]
    (try
      (.getMatrix pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-get-orientation
  "A function that will accept a PageFormat object where after spec validationwill reutrn the orientation of the pageformat.
  Orientations are static fields of the class type int.eg LANDSCAPE = 0, PORTRAIT = 1, REVERSE_LANDSCAPE = 2."
  [^PageFormat pageformat]
    (try
      (.getOrientation pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-get-paper
  "A function that will accept a PageFormat object where after spec validation
  will return a copy of the Paper object of this format."
  [^PageFormat pageformat]
    (try
      (.getPaper pageformat)
      (catch Exception e (.getMessage e))))
(defn pf-set-orientation
  "A function that will accept a vector of PageFormat object and an orientation int.
  For the orientation field the `clj-paper-print.pageformat/landscape`, `clj-paper-print.pageformat/portrait`,
  `clj-paper-print.pageformat/re-landscape` defs can be used."
  [[^PageFormat pageformat ornt]]
    (try
      (.setOrientation pageformat ornt)
      (catch Exception e (.getMessage e))))
(defn pf-set-paper
  "A function that will accept a vector of PageForamt object and a Paper object, which will set the supplied
  paper to the pageformat."
  [[^PageFormat pageformat ^Paper paper]]
    (try
      (.setPaper pageformat paper)
      true
      (catch Exception e (.getMessage e))))

(defmulti pageformat
  "A multimethod for PageFormat objects that will execute the methods of the
   `java.awt.print.PageForamt` class using the corresponding helper functions:
  `clj-paper-print.pageformat/clone`, `clj-paper-print.pageformat/get-h`,
  `clj-paper-print.pageformat/get-w`, `clj-paper-print.pageformat/get-im-h`,
  `clj-paper-print.pageformat/get-im-w`, `clj-paper-print.pageformat/get-im-x`,
  `clj-paper-print.pageformat/get-im-y`, `clj-paper-print.pageformat/get-matrix`,
  `clj-paper-print.pageformat/get-orientation`, `clj-paper-print.pageformat/get-paper`,
  `clj-paper-print.pageformat/set-orientation`, `clj-paper-print.pageformat/set-paper`."
  (fn [[action _ _ _ :as c]]
    (if-not (s/valid? ::pfs/pageformat-multi-input c)
      (throw (ex-info "pageformat input failed! " (expound/expound ::pfs/pageformat-multi-input c)))
      action)))
(defmulti pf-unit!
  "A multimethod nested to `pageformat` multimethod to give the choice to return the desired result of dimensions
  in the requested unit (inches, mm, default[screen = 1/72 of inch])."
  (fn [[unit cfg :as c]]
    unit))
(defmethod pageformat :pf-clone
  [[_ pageformat]]
  (pf-clone pageformat))
(defmethod pageformat :pf-get-h
  [[_ unit pageformat]]
  (pf-unit! [unit (pf-get-h pageformat)]))
(defmethod pageformat :pf-get-w
  [[_ unit pageformat]]
  (pf-unit! [unit (pf-get-w pageformat)]))
(defmethod pageformat :pf-get-im-h
  [[_ unit pageformat]]
  (pf-unit! [unit (pf-get-im-h pageformat)]))
(defmethod pageformat :pf-get-im-w
  [[_ unit pageformat]]
  (pf-unit! [unit (pf-get-im-w pageformat)]))
(defmethod pageformat :pf-get-im-x
  [[_ unit pageformat]]
  (pf-unit! [unit (pf-get-im-x pageformat)]))
(defmethod pageformat :pf-get-im-y
  [[_ unit pageformat]]
  (pf-unit! [unit (pf-get-im-y pageformat)]))
(defmethod pageformat :pf-get-matrix
  [[_ pageformat]]
  (pf-get-matrix pageformat))
(defmethod pageformat :pf-get-orientation
  [[_ pageformat _]]
  (pf-get-orientation pageformat))
(defmethod pageformat :pf-get-paper
  [[_ pageformat]]
  (pf-get-paper pageformat))
(defmethod pageformat :pf-set-orientation
  [[_ pageformat cfg]]
  (pf-set-orientation [pageformat cfg]))
(defmethod pageformat :pf-set-paper
  [[_ pageformat cfg]]
  (pf-set-paper [pageformat cfg]))

(defmethod pf-unit! :in-inches
  [[_ d]]
  (u/screen->inches d))
(defmethod pf-unit! :in-mm
  [[_ d]]
  (u/screen->mm d))
(defmethod pf-unit! :in-screen
  [[_ d]]
  d)








