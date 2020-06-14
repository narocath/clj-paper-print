(ns spec.paper-spec
  (:import [java.awt.print Paper])
  (:require [clojure.spec.alpha :as s]))
(def paper-action? #{:get-w
                     :get-h
                     :get-im-w
                     :get-im-h
                     :get-im-x
                     :get-im-y
                     :set-size
                     :set-im-area
                     :get-all
                     :clone})
(def unit-action? #{:in-inches
                    :in-mm
                    :in-screen ;; in screen
                    :from-inches
                    :from-mm
                    :from-screen})

(s/def ::width double?)
(s/def ::height double?)
(s/def ::x double?)
(s/def ::y double?)
(s/def ::im-w double?)
(s/def ::im-h double?)
(s/def ::size (s/keys :req-un [::width ::height]))
(s/def ::im-size (s/keys :req-un [::x ::y ::im-w ::im-h]))
(s/def ::dimension (s/or :a (s/keys :req-un [::size]
                                 :opt-un [::im-size])
                         :b (s/keys :req-un [::im-size]
                                 :opt-un [::size])))
(s/def ::unit-action? unit-action?)
(s/def ::action? paper-action?)
(s/def ::paper? #(instance? java.awt.print.Paper %))

(s/def ::paper-multi-input? (s/alt :a (s/cat :d ::action? :g ::unit-action? :e ::paper?)
                                   :b (s/cat :d ::action? :g ::unit-action? :e ::paper?
                                             :f ::dimension)
                                   :c (s/cat :d ::action? :e ::paper?)))



(s/fdef clj-paper-print.paper/defpaper
  :args (s/alt :one (s/cat :name symbol?)
               :two (s/cat :name symbol? :unit ::unit-action? :cfg ::dimension))
  :ret ::paper?)


