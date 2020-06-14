(ns clj-paper-print.media-size
  (:import [javax.print.attribute.standard MediaSize Media]
           [javax.print.attribute Size2DSyntax]
           [javax.print.attribute.standard MediaSizeName]
           [javax.print PrintService])
  (:require [clojure.string :as str]
            [clj-paper-print.media-size-names :refer [sizes-map]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [spec.media-size-spec :as mss]
            [clj-paper-print.utility :refer :all]))
;;
;;
;;
(defmulti media-size
  "The multi `media-size` return a vector with the dimesions of the requested ISO paper.
  It consists in 2 variations, each one accepts a vector with the variation keyword and a MediaSizeName object
  The input of each one (media-size [:inch/:mm mediaSizeName])
  1: `:inch`
   accepts: (^MediaSizeName) MediaSizeName (e.g A4).
   returns: vector of [x y] dimensions of the requested paper in inches.
  2; `:mm`
   accepts: (^MediaSizeName) MediaSizeName (e.g A4).
   returns: vector of [x y] dimensions of the requested paper in milimeters."
  (fn [[action & rest :as cfg]]
    (if-not (s/valid? ::mss/media-size-multi-input cfg)
      (throw (ex-info "media-size input failed! " (expound/expound ::mss/media-size-multi-input cfg)))
      action)))
(defmethod media-size :inch
  [[_ name]]
  {:width (rounding (double (.getX (MediaSize/getMediaSizeForName name) Size2DSyntax/INCH))) :height (rounding (double (.getY (MediaSize/getMediaSizeForName name) Size2DSyntax/INCH)))})
(defmethod media-size :mm
  [[_ name]]
  {:width (rounding (double (.getX (MediaSize/getMediaSizeForName name) Size2DSyntax/MM))) :height (rounding (double (.getY (MediaSize/getMediaSizeForName name) Size2DSyntax/MM)))})
(defmethod media-size :inch-im
  [[_ name]]
  {:im-w (rounding (double (.getX (MediaSize/getMediaSizeForName name) Size2DSyntax/INCH))) :im-h (rounding (double (.getY (MediaSize/getMediaSizeForName name) Size2DSyntax/INCH))) :x 0.0 :y 0.0})
(defmethod media-size :mm-im
  [[_ name]]
  {:im-w (rounding (double (.getX (MediaSize/getMediaSizeForName name) Size2DSyntax/MM))) :im-h (rounding (double (.getY (MediaSize/getMediaSizeForName name) Size2DSyntax/MM))) :x 0.0 :y 0.0})
(defmethod media-size :screen
  [[_ name]]
  {:width (rounding (* 72.0 (double (.getX (MediaSize/getMediaSizeForName name) Size2DSyntax/INCH)))) :height (rounding (* 72.0 (double (.getY (MediaSize/getMediaSizeForName name) Size2DSyntax/INCH))))})
(defmethod media-size :screen-im
  [[_ name]]
  {:im-w (rounding (* 72.0 (double (.getX (MediaSize/getMediaSizeForName name) Size2DSyntax/INCH)))) :im-h (rounding (* 72.0 (double (.getY (MediaSize/getMediaSizeForName name) Size2DSyntax/INCH))))
   :x 0.0 :y 0.0})

(defmulti ms-lookup-size
  "A multi-method that will perform a lookup with the given dimensions to return the equivalent `MediaSizeName` object.
  The dispatch values are :inch :mm.
  If there isn't an exact match, it will return the closest match.
  It might return nil if the closest match has no `Media` instance."
  (fn [[action & rest :as cfg]]
    (if-not (s/valid? ::mss/ms-lookup-multi cfg)
      (throw (ex-info "ms-lookup-size input failed! " (expound/expound ::mss/ms-lookup-multi cfg)))
      action)))
(defmethod ms-lookup-size :inch
  [[_ x y]]
  (let [size (MediaSize/findMedia x y Size2DSyntax/INCH)]
    (if size
      {(keyword (.toString size)) size}
      {})))
(defmethod ms-lookup-size :mm
  [[_ x y]]
  (let [size (MediaSize/findMedia x y Size2DSyntax/MM)]
    (if size
      {(keyword (.toString size)) size}
      {})))

(defn ms-supported-sizes
  "A function that will accept the supproted Media dimensions of a PrintService(printer)
  and will search for the equivelant ISO vaulues that are supported.
  The function will return a map with keys the name of each `MediaSizeName` object and value
  the actual `MediaSizeName`."
  [^PrintService prs]
  (let [sup-s (.getSupportedAttributeValues ^PrintService prs (Class/forName "javax.print.attribute.standard.Media") nil nil)]
    (reduce into {} (map #(assoc {} (keyword (str/replace (str %) #" " "-")) %) sup-s))))
(defn ms-predefined-sizes
  "A function that will return a map with all the predefined `MediaSizeName` objects of the supported variants."
  []
  sizes-map)
