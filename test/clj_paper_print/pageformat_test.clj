(ns clj-paper-print.pageformat-test
  (:require [clojure.test :refer :all]
            [clj-paper-print.core :refer [defpageformat landscape portrait rev-landscape]]
            [clj-paper-print.pageformat :refer :all]
            [spec.pageformat-spec :as pfs]
            [clojure.spec.alpha :as s]
            [clj-paper-print.utility :as u])
  (:import [java.awt.print PageFormat Paper]))

(defmacro set-scene-pgf
  "A macro to define the required object to test Pageformat namespace."
  []
  `(try
     (do
       (def t-paper (Paper.))
       (.setSize t-paper 135.0 135.0)
       (.setImageableArea t-paper 5.0 5.0 111.0 111.0)
       (def t-pgf (PageFormat.))
       (.setPaper t-pgf t-paper))
     (catch Exception e# (.getMessage e#))))
(defn clear-pgf-scene
  "A function that will clear the defined objects for the tests."
  []
  (map #(ns-unmap *ns* %) ['t-paper 't-pgf]))

(deftest test-pageformat-creation
  (try
    (defpageformat t-pgf)
    (is (s/valid? ::pfs/pageformat t-pgf))
    (catch Exception e (.getMessage e))
    (finally (ns-unmap *ns* 't-pgf))))
(deftest test-pageformat-creation-with-orientation
  (testing "landscape"
    (try
      (defpageformat t-pgf {:orientation landscape})
      (is (s/valid? ::pfs/pageformat t-pgf))
      (is (= 0 (.getOrientation t-pgf)))
      (catch Exception e (.getMessage e))
      (finally (ns-unmap *ns* 't-pgf))))
  (testing "portrait"
    (try
      (defpageformat t-pgf {:orientation portrait})
      (is (s/valid? ::pfs/pageformat t-pgf))
      (is (= 1 (.getOrientation t-pgf)))
      (catch Exception e (.getMessage e))
      (finally (ns-unmap *ns* 't-pgf))))
  (testing "rev-landscape"
    (try
      (defpageformat t-pgf {:orientation rev-landscape})
      (is (s/valid? ::pfs/pageformat t-pgf))
      (is (= 2 (.getOrientation t-pgf)))
      (catch Exception e (.getMessage e))
      (finally (ns-unmap *ns* 't-pgf)))))
(deftest test-pageformat-creation-with-paper
  (try
    (def t-paper (Paper.))
    (.setSize t-paper 100.0 100.0)
    (defpageformat t-pgf {:paper t-paper})
    (is (s/valid? ::pfs/pageformat t-pgf))
    (is (and (= 100.0 (:height (bean (.getPaper t-pgf)))) (= 100.0 (:width (bean (.getPaper t-pgf))))))
    (catch Exception e (.getMessage e))
    (finally (ns-unmap *ns* 't-pgf)
             (ns-unmap *ns* 't-paper))))
(deftest test-pageformat-creation-with-map
  (try
    (def t-paper (Paper.))
    (.setSize t-paper 150.0 150.0)
    (defpageformat t-pgf {:paper t-paper :orientation portrait})
    (is (s/valid? ::pfs/pageformat t-pgf))
    (is (and (= 150.0 (:height (bean (.getPaper t-pgf)))) (= 150.0 (:width (bean (.getPaper t-pgf))))))
    (is (= 1 (.getOrientation t-pgf)))
    (catch Exception e (.getMessage e))
    (finally (ns-unmap *ns* 't-pgf)
             (ns-unmap *ns* 't-paper))))
(deftest test-clone
  (try
    (set-scene-pgf)
    (def t-pgf2 (pageformat [:pf-clone t-pgf]))
    (is (s/valid? ::pfs/pageformat t-pgf2))
    (is (and (= (:height (bean t-pgf2)) (:height (bean t-pgf))) (= (:width (bean t-pgf2)) (:width (bean t-pgf)))))
    (catch Exception e (.getMessage e))
    (finally (clear-pgf-scene)
             (ns-unmap *ns* 't-pgf2))))
(deftest test-get-size&area
  (testing "size/area in inches"
    (try
      (set-scene-pgf)
      (is (= (pageformat [:pf-get-h :in-inches t-pgf]) (u/screen->inches (:height (bean t-pgf)))))
      (is (= (pageformat [:pf-get-w :in-inches t-pgf]) (u/screen->inches (:width (bean t-pgf)))))
      (is (= (pageformat [:pf-get-im-h :in-inches t-pgf]) (u/screen->inches (:imageableHeight (bean t-pgf)))))
      (is (= (pageformat [:pf-get-im-w :in-inches t-pgf]) (u/screen->inches (:imageableWidth (bean t-pgf)))))
      (is (= (pageformat [:pf-get-im-x :in-inches t-pgf]) (u/screen->inches (:imageableX (bean t-pgf)))))
      (is (= (pageformat [:pf-get-im-y :in-inches t-pgf]) (u/screen->inches (:imageableY (bean t-pgf)))))
      (catch Exception e (.getMessage e))
      (finally (clear-pgf-scene))))
  (testing "size/area in mm"
    (try
      (set-scene-pgf)
      (is (= (pageformat [:pf-get-h :in-mm t-pgf]) (u/screen->mm (:height (bean t-pgf)))))
      (is (= (pageformat [:pf-get-w :in-mm t-pgf]) (u/screen->mm (:width (bean t-pgf)))))
      (is (= (pageformat [:pf-get-im-h :in-mm t-pgf]) (u/screen->mm (:imageableHeight (bean t-pgf)))))
      (is (= (pageformat [:pf-get-im-w :in-mm t-pgf]) (u/screen->mm (:imageableWidth (bean t-pgf)))))
      (is (= (pageformat [:pf-get-im-x :in-mm t-pgf]) (u/screen->mm (:imageableX (bean t-pgf)))))
      (is (= (pageformat [:pf-get-im-y :in-mm t-pgf]) (u/screen->mm (:imageableY (bean t-pgf)))))
      (catch Exception e (.getMessage e))
      (finally (clear-pgf-scene))))
  (testing "size/area in screen"
    (try
      (set-scene-pgf)
      (is (= (pageformat [:pf-get-h :in-screen t-pgf]) (:height (bean t-pgf))))
      (is (= (pageformat [:pf-get-w :in-screen t-pgf]) (:width (bean t-pgf))))
      (is (= (pageformat [:pf-get-im-h :in-screen t-pgf]) (:imageableHeight (bean t-pgf))))
      (is (= (pageformat [:pf-get-im-w :in-screen t-pgf]) (:imageableWidth (bean t-pgf))))
      (is (= (pageformat [:pf-get-im-x :in-screen t-pgf]) (:imageableX (bean t-pgf))))
      (is (= (pageformat [:pf-get-im-y :in-screen t-pgf]) (:imageableY (bean t-pgf))))
      (catch Exception e (.getMessage e))
      (finally (clear-pgf-scene))))
  (testing "matrix/orientation/paper"
    (try
      (set-scene-pgf)
      (is (= (into [] (pageformat [:pf-get-matrix t-pgf])) (into [] (:matrix (bean t-pgf)))))
      (is (= (pageformat [:pf-get-orientation t-pgf]) (:orientation (bean t-pgf))))
      (is (s/valid? ::pfs/paper (pageformat [:pf-get-paper t-pgf])))
      (catch Exception e (.getMessage e))
      (finally (clear-pgf-scene))))
  (testing "set orientation"
    (try
      (set-scene-pgf)
      (pageformat [:pf-set-orientation t-pgf portrait])
      (is (= portrait (:orientation (bean t-pgf))))))
  (testing "set paper"
    (try
      (set-scene-pgf)
      (is (and
           (= 135.0 (:width (bean (:paper (bean t-pgf)))))
           (= 135.0 (:height (bean (:paper (bean t-pgf)))))))
      (do
        (def t-paper2 (Paper.))
        (.setSize t-paper2 200.0 200.0)
        (pageformat [:pf-set-paper t-pgf t-paper2]))
      (is (and
           (= (:width (bean t-paper2)) (:width (bean (:paper (bean t-pgf)))))
           (= (:height (bean t-paper2)) (:height (bean (:paper (bean t-pgf)))))))
      (catch Exception e (.getMessage e))
      (finally (clear-pgf-scene)
               (ns-unmap *ns* 't-paper2)))))
