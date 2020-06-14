(ns clj-paper-print.utility-test
  (:require [clj-paper-print.utility :refer :all]
            [clojure.test :refer :all]))

(deftest test-inches->screen
  (is (= 72.0 (inches->screen 1.0)))
  (is (= 144.0 (inches->screen 2.0))))
(deftest test-screen->inches
  (is (= 1.0 (screen->inches 72.0)))
  (is (= 2.0 (screen->inches 144.0))))
(deftest test-mm->screen
  (is (= 72.0 (mm->screen 25.4)))
  (is (= 144.0 (mm->screen 50.8))))
(deftest test-screen->mm
  (is (= 25.4 (screen->mm 72.0)))
  (is (= 50.8 (screen->mm 144.0))))
