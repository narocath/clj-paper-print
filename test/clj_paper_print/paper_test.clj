(ns clj-paper-print.paper-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clj-paper-print.paper :refer :all]
            [clj-paper-print.core :refer [defpaper]]
            [spec.paper-spec :as ps]
            [clj-paper-print.utility :refer :all])
  (:import [javax.print.attribute.standard MediaSizeName MediaSize Media]
           [javax.print.attribute Size2DSyntax]))
(defn map-kv
  [f coll]
  (reduce-kv (fn [m k v] (assoc m k (f v))) (empty coll) coll))
(def max-size-value 5000)
(def size-gen (s/gen (s/map-of
                          #{:width :height} (s/double-in :min 1.0 :max 100.0 :NaN? false :infinite? false)
                          :count 2 :distinct true)))

(def im-size-gen (s/gen (s/map-of
                          #{:x :y :im-w :im-h} (s/double-in :min 1.0 :max 100.0 :NaN? false :infinite? false)
                          :count 4)))

(defn gen-dimensions-for-paper
  "A function that will create dummy values to use for testing paper object.
  Accepts the number of samples"
  [samples]
  (for [x (range samples)
        :let [size (gen/sample size-gen samples)
              im-size (gen/sample im-size-gen samples)]]
    (merge {:size (reduce-kv (fn [c k v] (assoc c k (rounding v))) {} (nth size x))}
           {:im-size (reduce-kv (fn [c k v] (assoc c k (rounding v))) {} (nth im-size x))})))

(defmacro sizes-from-paper
  "A macro that will return the size and imageable area size of the suplied paper
  in the desired unit and in the form of a map {:size {}, :im-size {}}."
  [paper unit]
  `(let [p# ~paper u# ~unit
        s# [:width :height]
        sm# [(~(symbol ".getWidth") p#) (~(symbol ".getHeight") p#)]
        a# [:x :y :im-w :im-h]
        am# [(~(symbol ".getImageableX") p#)
              (~(symbol ".getImageableY") p#)
              (~(symbol ".getImageableWidth") p#)
             (~(symbol ".getImageableHeight") p#)]]
     (case u#
       :inches {:size (map-kv screen->inches (zipmap s# sm#))
                :im-size (map-kv screen->inches (zipmap a# am#))}
       :mm {:size (map-kv screen->mm (zipmap s# sm#))
            :im-size (map-kv screen->mm (zipmap a# am#))}
       :screen {:size (zipmap s# sm#)
                :im-size (zipmap a# am#)})))
(defmacro size-from-paper-using-multi
  [paper unit]
  `(case ~unit
     :inches {:size {:width (paper [:get-w :in-inches ~paper])
                     :height (paper [:get-h :in-inches ~paper])}
              :im-size {:x (paper [:get-im-x :in-inches ~paper])
                        :y (paper [:get-im-y :in-inches ~paper])
                        :im-w (paper [:get-im-w :in-inches ~paper])
                        :im-h (paper [:get-im-h :in-inches ~paper])}}
     :mm {:size {:width (paper [:get-w :in-mm ~paper])
                     :height (paper [:get-h :in-mm ~paper])}
              :im-size {:x (paper [:get-im-x :in-mm ~paper])
                        :y (paper [:get-im-y :in-mm ~paper])
                        :im-w (paper [:get-im-w :in-mm ~paper])
                        :im-h (paper [:get-im-h :in-mm ~paper])}}
     :screen {:size {:width (paper [:get-w :in-screen ~paper])
                     :height (paper [:get-h :in-screen ~paper])}
              :im-size {:x (paper [:get-im-x :in-screen ~paper])
                        :y (paper [:get-im-y :in-screen ~paper])
                        :im-w (paper [:get-im-w :in-screen ~paper])
                        :im-h (paper [:get-im-h :in-screen ~paper])}}))
(defmacro sizes-from-multi
  [paper from to]
  `(let []))
(defmacro generate-testings
  [dim name from unit n]
  `(deftest ~(symbol (str name n))
            (try
              (defpaper ~(symbol (str "t-paper-" n)) ~from ~dim)
              (is (= (sizes-from-paper ~(symbol (str "t-paper-" n)) ~unit) ~dim))
              )))
(deftest paper-creation-without-args
  (try
    (defpaper t-paper)
    (is (s/valid? ::ps/paper? t-paper))
    (finally (ns-unmap *ns* 't-paper))))

(deftest paper-creation-with-inch-args
  (let [dimension (gen-dimensions-for-paper 2)]
    (try
      (defpaper t-paper1 :from-inches (nth dimension 0))
      (is (s/valid? ::ps/paper? t-paper1))
      (is (= (sizes-from-paper t-paper1 :inches) (nth dimension 0)))
      (is (not= (sizes-from-paper t-paper1 :inches) (nth dimension 1)))
      (finally (ns-unmap *ns* 't-paper1)))))
(deftest paper-creation-with-mm-args
  (let [dimension (gen-dimensions-for-paper 2)]
    (try
      (defpaper t-paper2 :from-mm (nth dimension 0))
      (is (s/valid? ::ps/paper? t-paper2))
      (is (not= (sizes-from-paper t-paper2 :mm) (nth dimension 1)))
      (is (= (sizes-from-paper t-paper2 :mm) (nth dimension 0)))
      (finally (ns-unmap *ns* 't-paper2)))))
(deftest paper-multi_get-test
  (let [dimension (gen-dimensions-for-paper 2)]
    (try
      (defpaper t-paper3 :from-inches (nth dimension 0))
      (is (s/valid? ::ps/paper? t-paper3))
      (is (= (size-from-paper-using-multi t-paper3 :inches) (sizes-from-paper t-paper3 :inches)))
      (is (= (size-from-paper-using-multi t-paper3 :mm) (sizes-from-paper t-paper3 :mm)))
      (is (= (size-from-paper-using-multi t-paper3 :screen) (sizes-from-paper t-paper3 :screen)))
      (finally (ns-unmap *ns* 't-paper3)))))
(deftest paper-multi_set-test
  (let [dimension (gen-dimensions-for-paper 2)
        pap (java.awt.print.Paper.)
        paper-def-dim (sizes-from-paper pap :inches)]
    (try
      (is (not= paper-def-dim (nth dimension 0)))
      (paper [:set-size :from-inches pap (nth dimension 0)])
      (is (= (get (sizes-from-paper pap :inches) :size) (get (nth dimension 0) :size)))
      (paper [:set-im-area :from-inches pap (nth dimension 0)])
      (is (= (get (sizes-from-paper pap :inches) :im-size) (get (nth dimension 0) :im-size)))
      )))


