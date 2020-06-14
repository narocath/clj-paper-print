(ns clj-paper-print.print-job-test
  (:require [clj-paper-print.print-job :refer :all]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [spec.print-job-spec :as pjs])
  (:import [java.awt.print PrinterJob PageFormat Pageable Printable Paper Book]
           [javax.print PrintService]))

(defmacro set-scene-pj
  ""
  []
  `(do
     (def t-pj (PrinterJob/getPrinterJob))
     (def t-paper (Paper.))
     (def t-paper2 (Paper.))
     (def t-pgf2 (PageFormat.))
     (def t-pgf (PageFormat.))
     (.setSize t-paper 111.0 111.0)
     (.setSize t-paper 222.0 222.0)
     (.setPaper t-pgf2 t-paper2)
     (.setPaper t-pgf t-paper)
     (def t-prnt (reify Printable
                   (print [this# g# pf# p#]
                     (if (= p# 0)
                       (do
                         (.translate g# (int (.getImageableX pf#)) (int (.getImageableY pf#)))
                         (.drawString g# "heloo" 10 10)
                         Printable/PAGE_EXISTS)
                       Printable/NO_SUCH_PAGE))))))
(defn clear-scene-pj
  ""
  []
  (map #(ns-unmap *ns* %) ['t-paper 't-paper2 't-pgf 't-pgf2 't-pj 't-prnt]))

(deftest test-default-page
  (testing "default-page/pgf"
    (try
      (set-scene-pj)
      (is (s/valid? ::pjs/pageformat (print-job [:default-page t-pj])))
      (is (s/valid? ::pjs/pageformat (print-job [:default-page-pgf t-pj t-pgf])))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj)))))
(deftest test-get-methods
  (testing ":get-copies"
    (try
      (set-scene-pj)
      (is (= (:copies (bean t-pj)) (print-job [:get-copies t-pj])))
      (do
        (.setCopies t-pj 5)
        (is (= 5 (print-job [:get-copies t-pj]))))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj))))
  (testing ":get-job-name"
    (try
      (set-scene-pj)
      (is (s/valid? ::pjs/name (print-job [:get-job-name t-pj])))
      (is (= (:jobName (bean t-pj)) (print-job [:get-job-name t-pj])))
      (do
        (.setJobName t-pj "TestName")
        (is (= "TestName" (print-job [:get-job-name t-pj]))))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj))))
  (testing "create printer job"
    (try
      (is (s/valid? ::pjs/printer-job (print-job [:get-printer-job])))
      (is (= (type (print-job [:get-printer-job])) (type (PrinterJob/getPrinterJob))))
      (catch Exception e (.getMessage e))))
  (testing "user-name"
    (try
      (set-scene-pj)
      (is (s/valid? ::pjs/name (print-job [:get-user-name t-pj])))
      (is (= (:userName (bean t-pj)) (print-job [:get-user-name t-pj])))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj))))
  (testing "is-cancelled"
    (try
      (set-scene-pj)
      (is (s/valid? boolean? (print-job [:is-cancelled t-pj])))
      (is (s/valid? false? (print-job [:is-cancelled t-pj])))))
  (testing "lookup-printservice"
    (try
      (set-scene-pj)
      (is (s/valid? map? (print-job [:lookup-p-serv! t-pj])))
      (is (for [[k v] (print-job [:lookup-p-serv! t-pj])]
            (s/valid? ::pjs/printservice? v)))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj)))))
(deftest test-set-methods
  (testing "set-copies"
    (try
      (set-scene-pj)
      (do
        (print-job [:set-copies! t-pj 10])
        (is (= 10 (.getCopies t-pj))))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj))))
  (testing "set jobName"
    (try
      (set-scene-pj)
      (do
        (print-job [:set-job-name! t-pj "TestName"])
        (is (= "TestName" (.getJobName t-pj))))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj))))
  (testing "set print service"
    (try
      (set-scene-pj)
      (def t-p-serv-vec (into [] (vals (print-job [:lookup-p-serv! t-pj]))))
      (if (> (count t-p-serv-vec) 0)
        (do
          (print-job [:set-print-service! t-pj (t-p-serv-vec 0)])
          (is (= (t-p-serv-vec 0) (:printService (bean t-pj))))
          (print-job [:set-print-service! t-pj (t-p-serv-vec 1)])
          (is (= (t-p-serv-vec 1) (:printService (bean t-pj))))))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj)
               (ns-unmap *ns* 't-p-serv-vec))))
  (testing "validate-page"
    (try
      (set-scene-pj)
      (is (s/valid? ::pjs/pageformat (print-job [:validate-page t-pj t-pgf])))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-pj)))))
