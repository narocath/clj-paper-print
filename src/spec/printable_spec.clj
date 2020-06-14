(ns spec.printable_spec
  (:import [java.awt.print Printable Pageable Book]
           [java.awt Graphics Font])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [spec.graphics-spec :as gs]))
(s/def ::printable-action? #{:one-page
                              :one-page-func
                              :multi-page
                              :multi-page-func
                              :txt})

(s/def ::printable-data? (s/alt :one (s/cat :a ::gs/action?   :c int? :d int? :e int? :f int?)
                                     :two (s/cat :a ::gs/action?   :c int? :d int? :e int? :f int? :g int? :h int?)
                                     :three (s/cat :a ::gs/action?  )
                                     :four (s/cat :a ::gs/action?   :c int? :d int? :e int? :f int? :g boolean?)
                                     :five (s/cat :a ::gs/action?   :c bytes? :d int? :e int? :f int? :g int?)
                                     :six (s/cat :a ::gs/action?   :c ::gs/ch-array? :d int? :e int? :f int? :g int?)
                                     :seven (s/cat :a ::gs/action?   :c ::gs/image? :d int? :e int? :f (s/or :a ::gs/observer :b nil?))
                                     :eight (s/cat :a ::gs/action?   :c ::gs/image? :d int? :e int? :f ::gs/color? :g (s/or :a ::gs/observer :b nil?))
                                     :nine (s/cat :a ::gs/action?   :c ::gs/image? :d int? :e int? :f int? :g int? :h (s/or :a ::gs/observer :b nil?))
                                     :ten (s/cat :a ::gs/action?   :c ::gs/image? :d int? :e int? :f int? :g int? :h ::gs/color? :i (s/or :a ::gs/observer :b nil?))
                                     :eleven (s/cat :a ::gs/action?   :c ::gs/image? :d int? :e int? :f int? :g int? :h int? :i int? :j int? :k int? :l (s/or :a ::gs/observer :b nil?))
                                     :twelve (s/cat :a ::gs/action?   :c ::gs/image? :d int? :e int? :f int? :g int? :h int? :i int? :j int? :k int? :l ::gs/color? :m (s/or :a ::gs/observer :b nil?))
                                     :thirteen (s/cat :a ::gs/action?   :c ::gs/polygon?)
                                     :fourteen (s/cat :a ::gs/action?   :c int? :d int? :e int?)
                                     :fifteen (s/cat :a ::gs/action?   :c string? :d int? :e int?)
                                     :sixteen (s/cat :a ::gs/action?   :c ::gs/rect?)
                                     :seventeen (s/cat :a ::gs/action?   :c ::gs/font?)
                                     :eighteen (s/cat :a ::gs/action?   :c ::gs/shape?)
                                     :nineteen (s/cat :a ::gs/action?   :c ::gs/color?)
                                     :twenty (s/cat :a ::gs/action? :c int? :d int?)))
(s/def ::printable-multi-input (s/alt :a (s/cat :a ::printable-action? :b (s/coll-of ::printable-data? :kind vector?))
                                      ;; :b (s/cat :a ::printable-action? :b symbol?)
                                      ))

