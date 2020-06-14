(ns clj-paper-print.paper
  (:import [java.awt.print PageFormat Paper])
  (:require [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [spec.paper-spec :as ps]
            [clj-paper-print.utility :as u]))

;;
;;
;;
;; Helper Methods
;;
(defn clone
  "A function that will create a clone of the supplied paper with its contents and will return it."
  [^Paper paper]
  (try
    (.clone paper)
    (catch Exception e (.getMessage e))))
(defn get-w
  "A helper function that will be called by the corresponding multimethod and will return the width
  of the supplied Paper object in screen size (1/72 of an inch)."
  [^Paper paper]
  (try
    (.getWidth paper)
    (catch Exception e (.getMessage e))))
(defn get-h
  "A helper function that will be called by the corresponding multimethod and will return the height of the supplied Paper object."
  [^Paper paper]
  (try
    (.getHeight paper)
    (catch Exception e (.getMessage e))))
(defn get-im-w
  "A helper function that will be called by the corresponding multimethod and will return the imageable's area width of the supplied Paper object."
  [^Paper paper]
  (try
    (.getImageableWidth paper)
    (catch Exception e (.getMessage e))))
(defn get-im-h
  "A helper function that will be called by the corresponding multimethod and will return the imageable's area height of the supplied Paper object."
  [^Paper paper]
  (try
    (.getImageableHeight paper)
    (catch Exception e (.getMessage e))))
(defn get-im-x
  "A helper function that will be called by the corresponding multimethod and will return the imageable's area X point of the supplied Paper object."
  [^Paper paper]
  (try
    (.getImageableX paper)
    (catch Exception e (.getMessage e))))
(defn get-im-y
  "A helper function that will be called by the corresponding multimethod and will return the imageable's area Y of the supplied Paper object."
  [^Paper paper]
  (try
    (.getImageableY paper)
    (catch Exception e (.getMessage e))))
(defn set-size
  "A helper function that will be called by the corresponding multimethod and will set the width and height for the supplied Paper object, translated from inches to screen."
  [[^Paper paper {{:keys [width height]} :size :as dimensions}]]
    (try
      (.setSize paper (get-in dimensions [:size :width]) (get-in dimensions [:size :height]))
      true
    (catch Exception e (.getMessage e))))
(defn set-im-area
  "A helper function that will be called by the corresponding multimethod and will set the X, Y point for the start of the
  Imageable area and the width, height of the Imageable area  for the supplied Paper object
  translated from inches to screen."
  [[^Paper paper {{:keys [x y im-w im-h]} :im-size :as dimensions}]]
  (try
    (.setImageableArea paper ^Double (get-in dimensions [:im-size :x])
                              ^Double (get-in dimensions [:im-size :y])
                              ^Double (get-in dimensions [:im-size :im-w])
                              ^Double (get-in dimensions [:im-size :im-h]))
    true
    (catch Exception e (.getMessage e))))
(defn get-all
  "A helper function that will be called by the corresponding multimethod and will return the size and the imageable's area size
  of the supplied Paper object."
  [^Paper paper]
  (let [width (.getWidth paper)
        height (.getHeight paper)
        x (.getImageableX paper)
        y (.getImageableY paper)
        im-w (.getImageableWidth paper)
        im-h (.getImageableHeight paper)]
    (try
      {:size {:width width :height height}
       :im-size {:x x :y y
                 :im-w im-w
                 :im-h im-h}}
      (catch Exception e (str "caught exception: " (.getMessage e))))))
(defmulti paper
  "A multimethod for Paper objects that will execute the methods of the `java.awt.print.Paper` class using the corresponding
  helper functions: `get-w`,`get-h`, `get-im-w`, `get-im-h`, `get-im-x`, `get-im-y`, `set-size`, `set-im-area`,
  along with the nested  `p-unit!` multimethod which will tranlate the requested output and the given input from/to the desired unit's values."
  (fn [[action unit ^Paper paper c :as cfg]]
    (if-not (s/valid? ::ps/paper-multi-input? cfg)
      (throw (ex-info "paper multi input failed." (expound/expound ::ps/paper-multi-input? cfg))))
    action))
(defmulti p-unit!
  "A multimethod nested to `paper` multimethod to give the choice to return the desired result of dimensions
  in the requested unit (inches, mm, default[screen = 1/72 of inch])."
  (fn [[unit cfg]]
    unit))
(defmethod paper :get-w
  [[_ unit ^Paper paper _]]
  (p-unit! [unit (get-w paper)]))
(defmethod paper :clone
  [[_ _ ^Paper paper _]]
  (clone paper))
(defmethod paper :get-h
  [[_ unit ^Paper paper _]]
  (p-unit! [unit (get-h paper)]))
(defmethod paper :get-im-w
  [[_ unit ^Paper paper _]]
  (p-unit! [unit (get-im-w paper)]))
(defmethod paper :get-im-h
  [[_ unit ^Paper paper _]]
  (p-unit! [unit (get-im-h paper)]))
(defmethod paper :get-im-x
  [[_ unit ^Paper paper _]]
  (p-unit! [unit (get-im-x paper)]))
(defmethod paper :get-im-y
  [[_ unit ^Paper paper _]]
  (p-unit! [unit (get-im-y paper)]))
(defmethod paper :set-size
  [[_ unit ^Paper paper {{:keys [width height]} :size :as cfg}]]
  (as-> cfg c
    (update-in c [:size :width] #(p-unit! [unit %]))
    (update-in c [:size :height] #(p-unit! [unit %]))
    (set-size [paper c])))
(defmethod paper :set-im-area
  [[_ unit ^Paper paper {{:keys [x y width height]} :im-size :as cfg}]]
  (as-> cfg c
    (update-in c [:im-size :x] #(p-unit! [unit %]))
    (update-in c [:im-size :y] #(p-unit! [unit %]))
    (update-in c [:im-size :im-w] #(p-unit! [unit %]))
    (update-in c [:im-size :im-h] #(p-unit! [unit %]))
    (set-im-area [paper c])))
(defmethod paper :get-all
  [[_ unit ^Paper paper _]]
  (-> (get-all paper)
      (update-in [:size :width] #(p-unit! [unit %]))
      (update-in [:size :height] #(p-unit! [unit %]))
      (update-in [:im-size :im-w] #(p-unit! [unit %]))
      (update-in [:im-size :im-h] #(p-unit! [unit %]))
      (update-in [:im-size :x] #(p-unit! [unit %]))
      (update-in [:im-size :y] #(p-unit! [unit %]))))
(defmethod p-unit! :in-inches
  [[_ d]]
  (u/screen->inches d))
(defmethod p-unit! :in-mm
  [[_ d]]
  (u/screen->mm d))
(defmethod p-unit! :in-screen
  [[_ d]]
  d)
(defmethod p-unit! :from-screen
  [[_ d]]
  d)
(defmethod p-unit! :from-inches
  [[_ d]]
  (u/inches->screen d))
(defmethod p-unit! :from-mm
  [[_ d]]
  (u/mm->screen d))
