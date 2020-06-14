(ns clj-paper-print.core
  (:import [java.awt.print PageFormat Paper Printable PrinterJob PrinterException PrinterAbortException PrinterIOException] 
           [java.awt Graphics2D Graphics Font]
           [javax.print PrintService]
           [javax.print.attribute Attribute AttributeSet PrintServiceAttributeSet HashPrintServiceAttributeSet Size2DSyntax]
           [javax.print.attribute.standard PrinterState MediaSizeName MediaSize Media]
           [java.awt Container]
           )
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [expound.alpha :as expound]
            [clojure.spec.gen.alpha :as gen]
            [clj-paper-print.paper :refer :all]
            [clj-paper-print.book :refer :all]
            [clj-paper-print.pageformat :refer :all]
            [clj-paper-print.media-size :refer :all]
            [clj-paper-print.media-size-names :refer :all]
            [clj-paper-print.graphics :refer :all]
            [clj-paper-print.printable :refer :all]
            [clj-paper-print.print-job :refer :all]
            [clj-paper-print.utility :as u]
            [spec.book-spec :as bs]
            [spec.paper-spec :as ps]
            [spec.pageformat-spec :as pfs]
            [spec.print-job-spec :as prjs]
            [spec.printable_spec :as prnts]
            [spec.graphics-spec :as gs]
            [spec.media-size-spec :as mss])
  )

; (s/check-asserts true)
(alter-var-root #'s/*explain-out* (constantly expound/printer))
(set! s/*explain-out* expound/printer)

(def landscape PageFormat/LANDSCAPE) ;; = 0
(def portrait PageFormat/PORTRAIT) ;; = 1
(def rev-landscape PageFormat/REVERSE_LANDSCAPE) ;; = 2

(defmacro atom-checker
  "A helper macro for the spec validation of the additional input of the macros for atoms."
  [cfg]
  `(cond
     (instance? clojure.lang.Atom ~cfg) @~cfg
     :else ~cfg))

;; Definitions
;; Paper
(defmacro defpaper 
  "A macro to define a var for a java.awt.print.Paper.
    The macro use arity with one argument, the paper name or the paper and the map with the dimensions.
    i.e a. (defpaper mypaper)
        b. (defpaper mypaper {:size {:width xxx :height xxx} :im-size {:x xx :y xx :im-w xxx :im-h xxx}})."
  ([-symbol]
   `(do
      (def ~-symbol (Paper.))
      ~-symbol)) 
  ([-symbol unit config]
   `(let [c# (atom-checker ~config)
          u# ~unit]
      (if (clojure.spec.alpha/valid? :spec.paper-spec/dimension c#)
        (do
          (defonce ~-symbol (Paper.))
          (if (contains? c# :size)
            (.setSize ~-symbol (p-unit! [u# (get-in c# [:size :width])])
                      (p-unit! [u# (get-in c# [:size :height])])))
          (if (contains? c# :im-size)
            (.setImageableArea ~-symbol (p-unit! [u# (get-in c# [:im-size :x])])
                               (p-unit! [u# (get-in c# [:im-size :y])])
                               (p-unit! [u# (get-in c# [:im-size :im-w])])
                               (p-unit! [u# (get-in c# [:im-size :im-h])])))
          ~-symbol)
        (do
          (clojure.spec.alpha/explain :spec.paper-spec/dimension c#) ;; if not succeded unreference symbol
          (ns-unmap *ns* '~-symbol))))))

(defmacro defpageformat
  "A macro to create a java.awt.PageFormat and generate its multimethods for manipulation.
  The macro use arity with no argument which will just create the PageFormat object, or
  with one argument which will be a map containing the paper object to automatically set the paper."
  ([-symbol]
   `(do
      (def ~-symbol (PageFormat.))
      ~-symbol))
  ([-symbol config]
   `(let [c# (atom-checker ~config)]
      (if (clojure.spec.alpha/valid? :spec.pageformat-spec/config c#)
        (do
          (def ~-symbol (PageFormat.))
          (if (contains? c# :paper)
            (try
              (.setPaper ~-symbol (c# :paper))
              (catch Exception e# (.getMessage e#))))
          (if (contains? c# :orientation)
            (try
              (.setOrientation ~-symbol (c# :orientation))
              (catch Exception e# (.getMessage e#))))
          ~-symbol)
        (do
          (s/explain :spec.pageformat-spec/config c#)
          (ns-unmap *ns* '~-symbol))))))

(defmacro defbook
  "A macro to create a Book object."
  [-symbol]
  `(def ~-symbol (Book.)))
(defmacro defprintjob
  "A macro to create a PrintrJob object."
  ([-symbol]
   `(do
      (def ~-symbol PrinterJob/getPrinterJob)
   ~-symbol))
  ([-symbol config]
   `(let [c# (atom-checker ~config)]
      (if (clojure.spec.alpha/valid? :spec.print-job/config c#)
        (do
          (def ~-symbol PrinterJob/getPrinterJob)
          (if (contains? c# :name)
            (try
              (.setJobName ~-symbol (c# :name))
              (catch Exception e# (.getMessage e#))))
          (if (contains? c# :copies)
            (try
              (.setCopies ~-symbol (c# :copies))
              (catch Exception e# (.getMessage e#)))))
        (do
          (clojure.spec.alpha/explain :spec.print-job/config c#)
          (ns-unmap *ns* '~-symbol))))))

(defn print->one!
  "A function with multi-arity that will accept the printable data and the pageformat only or with a PrinterJob
  and will execute the actual print to the printer."
  ([^Printable data ^PageFormat pageformat]
    (let [^Printable printable data ^PageFormat pagef pageformat ^PrinterJob job (print-job [:get-printer-job])]
    (try
      (dorun
        (.setPrintable job printable pagef)
        (.print job))
        (catch PrinterException pe (str "Caught printer exception: " (.getMessage pe)))
        (catch PrinterAbortException pae (str "Caught printer abort exception: " (.getMessage pae)))
        (catch PrinterIOException pio (str "Caught print IO exception: " (.getMessage pio))))))
  ([^Printable data ^PageFormat pageformat ^PrinterJob job]
    (try
      (dorun
        (.setPrintable job data (print-job [:validate-page job pageformat]))
        (.print job))
        (catch PrinterException pe (str "Caught printer exception: " (.getMessage pe)))
        (catch PrinterAbortException pae (str "Caught printer abort exception: " (.getMessage pae)))
        (catch PrinterIOException pio (str "Caught print IO exception: " (.getMessage pio))))))
(defn print->multi!
  "A function with multi-arity that will accept the Pageable object or the Book object with a PrinterJob and
  will execute the actual print to the printer"
  ([^java.awt.print.Pageable book]
    (let [^java.awt.print.Pageable b book ^PrinterJob job (print-job [:get-printer-job]) serv (.getPrintService job)]
      (try
        (dorun
          (.setPageable job b)
          (.print job))
          (catch PrinterException pe (str "Caught printer exception: " (.getMessage pe)))
          (catch PrinterAbortException pae (str "Caught printer abort exception: " (.getMessage pae)))
          (catch PrinterIOException pio (str "Caught print IO exception: " (.getMessage pio))))))
  ([^java.awt.print.Book book ^PrinterJob job]
    (try
      (dorun 
       (.setPageable job book)
        (.print job))
        (catch PrinterException pe (str "Caught printer exception: " (.getMessage pe)))
        (catch PrinterAbortException pae (str "Caught printer abort exception: " (.getMessage pae)))
        (catch PrinterIOException pio (str "Caught print IO exception: " (.getMessage pio))))))

(defn printer-state?
"A function that will return a java array of the attributes that are supported by the printer (PrintService) or
it will just print them in REPL.
The first argument is the PrintService object and the second `printer-status` a boolean value. If `printer-status` is
true will just print in REPL the attributes and their corresponding values. If false will just return an array of attributes.
The function mainly is used to check the printJob queued-job-count, to see if the job was finished."
  [^PrintService service ^Boolean printer-status?]
  (let [ attributes ^PrintServiceAttributeSet (.getAttributes service)
    attributes-set ^HashPrintServiceAttributeSet (HashPrintServiceAttributeSet. ^PrintServiceAttributeSet (.getAttributes service))
    array-attr (.toArray attributes-set)
    ps  printer-status?]
      (if ps
        (do
          (println "---------- PrinterState ----------")
          (dorun
            (map #(println (.getName %) " = " (.toString %)) array-attr))
          (println "----------END PrinterState--------"))
        (reduce conj {} (map #(conj {} [(keyword (.getName %)) (.toString %)]) array-attr)))))

(defn paper!
  "A helper function to call the multimethod `clj-paper-print.paper/paper` from the `clj-paper-print.paper` namespace."
  [cfg]
  (if-not (s/valid? ::ps/paper-multi-input? cfg)
    (throw (ex-info "paper multi input failed." (expound/expound ::ps/paper-multi-input? cfg))))
  (paper cfg))
(defn pageformat!
  "A helper function to call the multimethod `clj-paper-print.pageformat/pageformat` from the `clj-paper-print.pageforamt` namespace."
  [cfg]
  (if-not (s/valid? ::pfs/pageformat-multi-input cfg)
  (throw (ex-info "pageformat input failed! " (expound/expound ::pfs/pageformat-multi-input cfg)))
  (pageformat cfg)))
(defn print-job!
  "A helper function to call the multimethod `clj-paper-print.print-job/print-job` from the `clj-paper-print.print-job` namespace."
  [cfg]
  (if-not (s/valid? ::prjs/printer-job-multi-input cfg)
    (throw (ex-info "print-job input failed! " (expound/expound ::prjs/printer-job-multi-input cfg)))
    (print-job cfg)))
(defn printable!
  "A helper function to call the multimethod `clj-paper-print.printable/printable` from the `clj-paper-print.printable` namespace."
  [cfg]
  (if-not (s/valid? ::prnts/printable-multi-input cfg)
    (throw (ex-info "printable input failed: " (expound/expound ::prnts/printable-multi-input cfg)))
    (printable cfg)))
(defn book!
  "A helper function to call the multimethod `clj-paper-print.book/book` from the `clj-paper-print.book` namespace."
  [cfg]
  (if-not (s/valid? ::bs/book-multi-input cfg)
    (throw (ex-info "book! input failed! " (expound/expound ::bs/book-multi-input cfg)))
    (book cfg)))
(defn graphics-methods!
  "A helper function to call the multimethod `clj-paper-print.graphics/graphics-methods` from the `clj-paper-print.graphics` namespace."
  [cfg]
  (if-not (s/valid? ::gs/graphics-multi-input? cfg)
    (throw (ex-info "graphics-methods input failed!" (expound/expound ::gs/graphics-multi-input? cfg)))
    (graphics-methods cfg)))
(defn media-size!
  "A helper function to call the multimethod `clj-paper-print.media-size/media-size` from the `clj-paper-print.media-size` namespace."
  [cfg]
  (if-not (s/valid? ::mss/media-size-multi-input cfg)
    (throw (ex-info "media-size input failed! " (expound/expound ::mss/media-size-multi-input cfg)))
    (media-size cfg)))
(defn ms-lookup-size!
  "A helper function to call the multimethod `clj-paper-print.media-size/ms-lookup-size` from the `clj-paper-print.media-size` namespace."
  [cfg]
  (if-not (s/valid? ::mss/ms-lookup-multi cfg)
    (throw (ex-info "ms-lookup-size input failed! " (expound/expound ::mss/ms-lookup-multi cfg)))
    (ms-lookup-size cfg)))
