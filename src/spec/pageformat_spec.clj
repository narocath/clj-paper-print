(ns spec.pageformat-spec
  (:import [java.awt.print PageFormat]
           [java.awt.print Paper])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))
(def pageformat-action? #{:pf-clone
                          :pf-get-h
                          :pf-get-w
                          :pf-get-im-h
                          :pf-get-im-w
                          :pf-get-im-x
                          :pf-get-im-y
                          :pf-get-matrix
                          :pf-get-orientation
                          :pf-get-paper
                          :pf-set-orientation
                          :pf-set-paper})
(def pageformat-unit-action? #{:in-inches
                               :in-mm
                               :in-screen})
(s/def ::action pageformat-action?)
(s/def ::unit-action pageformat-unit-action?)
(s/def ::pageformat #(instance? java.awt.print.PageFormat %))
(s/def ::orientation int?) ;; to test the return value on test
(s/def ::paper #(instance? java.awt.print.Paper %))
(s/def ::height-width-x-y? double?) ;; to test the return value on test
(s/def ::matrix #(= (Class/forName "[D") (type %))) ;; to test the return value on tests
(s/def ::set-orientation (s/tuple ::pageformat ::orientation))
(s/def ::set-paper (s/tuple ::pageformat ::paper))
(s/def ::pageformat-multi-input (s/alt :one (s/cat :a ::action :c ::unit-action :b ::pageformat)
                                       :two (s/cat :a ::action :b ::pageformat)
                                       :three (s/cat :a ::action :b ::pageformat :c ::paper)
                                       :four (s/cat :a ::action :b ::pageformat :c ::orientation)))
(s/def ::config (s/or :a (s/keys :req-un [::paper]
                                 :opt-un [::orientation])
                      :b (s/keys :req-un [::orientation]
                                 :opt-un [::paper])))

(s/fdef spec.pageformat-spec/defpageformat
  :args (s/alt :one (s/cat :name symbol?)
               :two (s/cat :name symbol? :cfg ::config))
  :ret ::pageformat)


