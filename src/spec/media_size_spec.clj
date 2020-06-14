(ns spec.media-size-spec
  (:import [javax.print.attribute.standard Media MediaSize MediaSizeName]
           [javax.print.attribute Size2DSyntax]
           [javax.print PrintService])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

(def media-size-action? #{:inch
                          :mm
                          :inch-im
                          :mm-im
                          :screen
                          :screen-im})
(def ms-lookup-size-action? #{:inch
                              :mm})

(s/def ::media-size-action? media-size-action?)
(s/def ::ms-lookup-action? ms-lookup-size-action?)
(s/def ::media? #(instance? javax.print.attribute.standard.Media %))
(s/def ::media-size? #(instance? javax.print.attribute.standard.MediaSize %))
(s/def ::media-size-name? #(instance? javax.print.attribute.standard.MediaSizeName %))
(s/def ::printservice? #(instance? javax.print.PrintService %))
(s/def ::media-size-multi-input (s/cat :a ::media-size-action?
                                       :b ::media-size-name?))
(s/def ::ms-lookup-multi (s/cat :a ::ms-lookup-action?
                                :b double?
                                :c double?))
