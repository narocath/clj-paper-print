(ns clj-paper-print.core-test
  (:require [clojure.test :refer :all]
            [clj-paper-print.core :refer :all]
            [clojure.spec.alpha :as s])
  (:import [javax.print.attribute.standard MediaSizeName MediaSize Media]
           [javax.print.attribute Size2DSyntax]))

(deftest prinjob-can-be-defined 
  (testing "It can be defined"
    (defprintjob some-name)
  ))

;; TODO add tests of core
;; (deftest a-test
;;   (testing "FIXME, I fail."
;;     (is (= 0 1))))
