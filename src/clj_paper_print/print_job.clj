(ns clj-paper-print.print-job
    (:import [java.awt.print PrinterJob PageFormat Pageable Printable PrinterAbortException PrinterException PrinterIOException]
        [javax.print PrintService]
        [javax.print.attribute PrintRequestAttributeSet PrintServiceAttributeSet]
        [javax.print.event PrintServiceAttributeListener PrintServiceAttributeEvent]
        [java.io OutputStream])
    (:require [clojure.spec.alpha :as s]
              [clojure.string :as str]
              [expound.alpha :as expound]
              [spec.print-job-spec :as prs]))




(defn cancel
  "A function that will cancel the supplied print job that is in progress."
  [^PrinterJob printerjob]
    (try
      (.cancel printerjob)
      (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn default-page
  "A function that will return a new PageFormat instance from the supplied print job, with default size and orientation."
  [^PrinterJob printerjob]
    (try
      (.defaultPage printerjob)
      (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn default-page-pgf
  "A function that will clone the supplied pageFormat and set it as the default size and orientation."
  [[^PrinterJob printerjob ^PageFormat pageformat ]]
    (try
      (.defaultPage printerjob pageformat)
      (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn get-copies
  "A function that will reutrn the number of copies of the print job."
  [^PrinterJob printerjob]
    (try
      (.getCopies printerjob)
      (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn get-job-name
  "A function that will reutrn the name of the document that is to be printed."
  [^PrinterJob printerjob]
    (try
      (.getJobName printerjob)
      (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
      (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn get-printer-job
  "A function that will create and return a PrinterJob."
  []
  (try
    (PrinterJob/getPrinterJob)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn get-print-service
  "A funcntion that will return the printers for the supplied printer job."
  [^PrinterJob printerjob]
  (try
    (.getPrintService printerjob)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn get-user-name
  "A function that will return the name of the printing user."
  [^PrinterJob printerjob]
  (try
    (.getUserName printerjob)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn is-cancelled
  "A function that will return true if a printing job is in progress and is going to be cancelled, else false."
  [^PrinterJob printerjob]
  (try
    (.isCancelled printerjob)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn lookup-p-serv!
  "A function that will return a map with the `PrintService` objects that exist at the system.
  It will use the same objects to retrieve the name of each printer to create keywords and the same objects as values, and in turn to return a map with them."
  []
  (try
    (reduce into {} (map #(assoc {} (keyword (str/replace (.getName ^PrintService %) #" " "-")) ^PrintService %) (PrinterJob/lookupPrintServices)))
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn page-dialog
  "A function that will show a dialog for the modification of the PageFormat."
  [[^PrinterJob printerjob ^PageFormat pgf]]
  (try
    (.pageDialog printerjob pgf)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn page-dialog-attrset
  "A function that will show a cross-platform set-up dialog for the modification of the page."
  [[^PrinterJob printerjob ^PrintRequestAttributeSet pras]]
  (try
    (.pageDialog printerjob pras)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn print!
  "A function that will print a set of the pages."
  [^PrinterJob printerjob]
  (try
    (.print ^PrinterJob printerjob)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn print-dialog!
  "A function that will show a dialog to change the properties of the print job."
  [^PrinterJob printerjob]
  (try
    (.printDialog printerjob)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn print-dialog-attrset!
  "A function that will show a cross-platform print dialog that is capable on printing 2D graphics, using the Pageable."
  [[^PrinterJob printerjob ^PrintRequestAttributeSet pras]]
  (try
    (.printDialog printerjob pras)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn set-copies!
  "A function that will set the number of copies for the supplied print job."
  [[^PrinterJob printerjob ^Integer cps]]
  (try
    (.setCopies printerjob cps)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn set-job-name!
  "A function that will set the name for the print job."
  [[^PrinterJob printerjob ^String name]]
  (try
    (.setJobName printerjob name)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn set-pageable!
  "A function that will set the Pageable object (probably in most cases Book) for the print job."
  [[^PrinterJob printerjob ^Book book]]
  (try
    (.setPageable ^PrinterJob printerjob ^Book book)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn set-printable!
  "A function that will set the painter that will render the page for the supplied print job."
  [[^PrinterJob printerjob ^Printable printable]]
  (try
    (.setPrintable ^PrinterJob printerjob ^Printable printable)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn set-printable-pgf!
  "A function that will use the supplied painter and pageFormat for this print job."
  [[^PrinterJob printerjob ^Printable printable ^PageFormat pgf]]
  (try
    (.setPrintable ^PrinterJob printerjob ^Printable printable ^PageFormat pgf)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn set-print-service!
  "A function that will set the printService (printer) for the supplied print job."
  [[^PrinterJob printerjob ^PrintService print-service]]
  (try
    (.setPrintService ^PrinterJob printerjob ^PrintService print-service)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))
(defn validate-page
  "A function that will reutrn a clone of the page, with its settings changed to be compatible with the current printer of the supplied print job."
  [[^PrinterJob printerjob ^PageFormat pgf]]
  (try
    (.validatePage ^PrinterJob printerjob ^PageFormat pgf)
    (catch Exception e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterAbortException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterException e (prn "Caught `print-job` exception: " (.getMessage e)))
    (catch PrinterIOException e (prn "Caught `print-job` exception: " (.getMessage e)))))

(defmulti print-job
  "A multimethod for PrinterJob objects that will executer the methods of
  `java.awt.PrinterJob` class using the corresponding helper functions:
  `clj-paper-print.print-job/cancel`, `clj-paper-print.print-job/default-page`,
  `clj-paper-print.print-job/default-page-pgf`, `clj-paper-print.print-job/get-copies`,
  `clj-paper-print.print-job/get-job-name`, `clj-paper-print.print-job/get-printer-job`,
  `clj-paper-print.print-job/get-print-service`, `clj-paper-print.print-job/get-user-name`,
  `clj-paper-print.print-job/is-cancelled`, `clj-paper-print.print-job/lookup-p-serv`,
  `clj-paper-print.print-job/lookup-p-serv-strg`, `clj-paper-print.print-job/page-dialog`,
  `clj-paper-print.print-job/page-dialog-attrset`, `clj-paper-print.print-job/print`,
  `clj-paper-print.print-job/print-dialog`, `clj-paper-print.print-job/print-dialog-attrset`,
  `clj-paper-print.print-job/set-copies`, `clj-paper-print.print-job/set-job-name`,
  `clj-paper-print.print-job/set-pageable`, `clj-paper-print.print-job/set-printable`,
  `clj-paper-print.print-job/set-printable-pgf`, `clj-paper-print.print-job/set-print-service`,
  `clj-paper-print.print-job/validate-page`."
  (fn [[action & rest :as cfg]]
    (if-not (s/valid? ::prs/printer-job-multi-input cfg)
      (throw (ex-info "print-job input failed! " (expound/expound ::prs/printer-job-multi-input cfg)))
      action)))
(defmethod print-job :cancel
    [[_ job]]
    (cancel job))
(defmethod print-job :default-page
    [[_ job]]
    (default-page  job))

(defmethod print-job :default-page-pgf
    [[_ job pgf]]
    (default-page-pgf [job pgf]))
(defmethod print-job :get-copies
    [[_ job]]
    (get-copies job))
(defmethod print-job :get-job-name
    [[_ job]]
    (get-job-name job))
(defmethod print-job :get-printer-job
    [[_ ]]
    (get-printer-job))
(defmethod print-job :get-print-service
    [[_ job]]
    (get-print-service job))
(defmethod print-job :get-user-name
    [[_ job]]
    (get-user-name job))
(defmethod print-job :is-cancelled
    [[_ job]]
    (is-cancelled job))
(defmethod print-job :lookup-p-serv!
    [[_ ]]
    (lookup-p-serv!))
(defmethod print-job :page-dialog
    [[_ job pgf]]
    (page-dialog [job pgf]))
(defmethod print-job :page-dialog-attrset
    [[_ job pras]]
    (page-dialog-attrset [job pras]))
(defmethod print-job :print!
    [[_ job]]
    (print! job))
(defmethod print-job :print-dialog!
    [[_ job]]
    (print-dialog! job))
(defmethod print-job :print-dialog-attrset!
    [[_ job pras]]
    (print-dialog-attrset! [job pras]))
(defmethod print-job :set-copies!
  [[_ job cps]]
    (set-copies! [job cps]))
(defmethod print-job :set-job-name!
    [[_ job name]]
    (set-job-name! [job name]))
(defmethod print-job :set-pageable!
    [[_ job pageable]]
    (set-pageable! [job pageable]))
(defmethod print-job :set-printable!
    [[_ job printable]]
    (set-printable! [job printable]))
(defmethod print-job :set-printable-pgf!
    [[_ job printable pgf]]
    (set-printable-pgf! [job printable pgf]))
(defmethod print-job :set-print-service!
    [[_ job print-service]]
    (set-print-service! [job print-service]))
(defmethod print-job :validate-page
    [[_ job pgf]]
  (validate-page [job pgf]))
