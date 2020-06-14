(ns clj-paper-print.book-test
  (:require [clojure.test :refer :all]
            [clj-paper-print.book :refer :all]
            [clj-paper-print.core :refer [defbook]]
            [spec.book-spec :as bs]
            [clojure.spec.alpha :as s])
  (:import [java.awt.print Book Printable PageFormat Paper]))
(defmacro set-scene-1-page
  "A macro to define the needed objects to test the book multimethods"
  []
  `(do
     (def t-book (Book.))
     (def t-paper (Paper.))
     (def t-pgf (PageFormat.))
     (.setPaper t-pgf t-paper)
     (def t-prnt (reify Printable
                   (print [this# g# pf# p#]
                     (if (= p# 0)
                       (do
                         (.translate g# (int (.getImageableX pf#)) (int (.getImageableY pf#)))
                         (.drawString g# "heloo" 10 10)
                         Printable/PAGE_EXISTS)
                       Printable/NO_SUCH_PAGE))))))

(defn clear-scene
  "A function to reset the 'scene' for the tests."
  []
  (map #(ns-unmap *ns* %) ['t-book 't-paper 't-pgf 't-prnt]))

(deftest book-creation
  (try
    (defbook t-book)
    (is (s/valid? ::bs/book t-book))
    (finally (ns-unmap *ns* 't-book))))
(deftest test-append
  (try
    (set-scene-1-page)
    (book [:append t-book t-prnt t-pgf])
    (is (= (.getNumberOfPages t-book) 1))
    (book [:append t-book t-prnt t-pgf])
    (is (= (.getNumberOfPages t-book) 2))
    (finally (clear-scene))))
(deftest test-append-with-num-pages
  (try
    (set-scene-1-page)
    (book [:append-wp t-book t-prnt t-pgf 3])
    (is (= (.getNumberOfPages t-book) 3))
    (book [:append-wp t-book t-prnt t-pgf 3])
    (is (= (.getNumberOfPages t-book) 6))
    (finally (clear-scene))))
(deftest test-get-num-of-pages
  (try
    (set-scene-1-page)
    (.append t-book t-prnt t-pgf)
    (is (= (.getNumberOfPages t-book) (book [:get-num-of-pages t-book])))
    (finally (clear-scene))))
(deftest test-get-page-format
  (try
    (set-scene-1-page)
    (.append t-book t-prnt t-pgf)
    (is (s/valid? ::bs/pageformat (book [:get-page-format t-book 0])))
    (finally (clear-scene))))
(deftest test-get-printable
  (try
    (set-scene-1-page)
    (.append t-book t-prnt t-pgf)
    (is (s/valid? ::bs/printable (book [:get-printable t-book 0])))
    (finally (clear-scene))))
(deftest test-set-page
  (try
    (do
      (set-scene-1-page)
      (.append t-book t-prnt t-pgf)
      (book [:set-page t-book t-prnt t-pgf 0]))
    (is (= 1 (.getNumberOfPages t-book)))
    (is (not= 2 (.getNumberOfPages t-book)))
    (finally (clear-scene))))
