(ns spec.print-job-spec
  (:import [java.awt.print PrinterJob PageFormat Pageable Printable]
           [javax.print PrintService]
           [javax.print.attribute PrintRequestAttributeSet PrintServiceAttributeSet]
           [javax.print.event PrintServiceAttributeListener PrintServiceAttributeEvent])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

(def print-job-action? #{:cancel
                        :default-page
                        :default-page-pgf
                        :get-copies
                        :get-job-name
                        :get-printer-job
                        :get-print-service
                        :get-user-name
                        :is-cancelled
                        :lookup-p-serv!
                        :page-dialog
                        :page-dialog-attrset
                        :print!
                        :print-dialog!
                        :print-dialog-attrset!
                        :set-copies!
                        :set-job-name!
                        :set-pageable!
                        :set-printable!
                        :set-printable-pgf!
                        :set-print-service!
                        :validate-page})
(s/def ::action? print-job-action?)
(s/def ::printer-job #(instance? java.awt.print.PrinterJob %))
(s/def ::pageformat #(instance? java.awt.print.PageFormat %))
(s/def ::printservice? #(instance? javax.print.PrintService %))
(s/def ::print-req-attribute-set? #(instance? javax.print.attribute.PrintRequestAttributeSet %))
(s/def ::printservice-array? #(= (Class/forName "[Ljavax.print.PrintService;]")));; for the creation of the tests
(s/def ::pageable? #(instance? java.awt.print.Pageable %))
(s/def ::printable? #(instance? java.awt.print.Printable %))
(s/def ::copies int?)
(s/def ::name string?)
(s/def ::cancelled? boolean?)

(s/def ::printer-job-multi-input (s/alt
                                  :one (s/cat :a ::action? :b ::printer-job)
                                  :two (s/cat :a ::action? :b ::printer-job :c ::pageformat)
                                  :three (s/cat :a ::action? :b ::printer-job :c ::print-req-attribute-set?)
                                  :four (s/cat :a ::action? :b ::printer-job :c ::printable?)
                                  :five (s/cat :a ::action? :b ::printer-job :c ::printable? :d ::pageformat)
                                  :six (s/cat :a ::action? :b ::printer-job :c ::printservice?)
                                  :seven (s/cat :a ::action? :b ::printer-job :c ::pageable?)
                                  :eight (s/cat :a ::action? :b ::printer-job :c ::copies)
                                  :nine (s/cat :a ::action? :b ::printer-job :c ::name)
                                  :ten (s/cat :a ::action? :b ::name)
                                  :eleven (s/cat :a ::action?)))
(s/def ::config (s/or :a (s/keys :req-un [::name]
                                 :opt-un [::copies])
                      :b (s/keys :req-un [::copies]
                                 :opt-un [::name])))

(s/fdef clj-paper-print.print-job/defprintjob
  :args (s/alt :one (s/cat :name symbol?)
               :two (s/cat :nam symbol? :cfg ::config))
  :ret ::printer-job)
