(ns spec.graphics-spec
  (:import [java.awt Graphics Color Font Image Rectangle Shape Polygon])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

(def graphics-action? #{:create
                        :copy-area
                        :clear-rect!
                        :clip-rect
                        :dispose!
                        :draw-arc
                        :draw-bytes
                        :draw-chars
                        :draw-image
                        :draw-line
                        :draw-oval
                        :draw-polygon
                        :draw-polyline
                        :draw-rect
                        :draw-round-rect
                        :draw-string
                        :fill-arc
                        :fill-oval
                        :fill-polygon
                        :fill-rect
                        :fill-round-rect
                        :finalize!
                        :get-clip
                        :get-clip-bounds
                        :get-color
                        :get-font
                        :get-font-metrics
                        :set-clip!
                        :set-color!
                        :set-font!
                        :translate})
(s/def ::action? graphics-action?)
(s/def ::graphics? #(instance? java.awt.Graphics %))
(s/def ::color? #(instance? java.awt.Color %))
(s/def ::font? #(instance? java.awt.Font %))
(s/def ::image? #(instance? java.awt.Image %))
(s/def ::rect? #(instance? java.awt.Rectangle %))
(s/def ::shape? #(instance? java.awt.Shape %))
(s/def ::ch-array? #(= (Class/forName "[C") (type %)))
(s/def ::polygon? #(instance? java.awt.Polygon %))

(s/def ::graphics-multi-input? (s/alt :one (s/cat :a ::action? :b ::graphics? :c int? :d int? :e int? :f int?)
                                     :two (s/cat :a ::action? :b ::graphics? :c int? :d int? :e int? :f int? :g int? :h int?)
                                     :three (s/cat :a ::action? :b ::graphics?)
                                     :four (s/cat :a ::action? :b ::graphics? :c int? :d int? :e int? :f int? :g boolean?)
                                     :five (s/cat :a ::action? :b ::graphics? :c bytes? :d int? :e int? :f int? :g int?)
                                     :six (s/cat :a ::action? :b ::graphics? :c ::ch-array? :d int? :e int? :f int? :g int?)
                                     :seven (s/cat :a ::action? :b ::graphics? :c ::image? :d int? :e int?)
                                     :eight (s/cat :a ::action? :b ::graphics? :c ::image? :d int? :e int? :f ::color?)
                                     :nine (s/cat :a ::action? :b ::graphics? :c ::image? :d int? :e int? :f int? :g int?)
                                     :ten (s/cat :a ::action? :b ::graphics? :c ::image? :d int? :e int? :f int? :g int? :h ::color?)
                                     :eleven (s/cat :a ::action? :b ::graphics? :c ::image? :d int? :e int? :f int? :g int? :h int? :i int? :j int? :k int?)
                                     :twelve (s/cat :a ::action? :b ::graphics? :c ::image? :d int? :e int? :f int? :g int? :h int? :i int? :j int? :k int? :l ::color?)
                                     :thirteen (s/cat :a ::action? :b ::graphics? :c ::polygon?)
                                     :fourteen (s/cat :a ::action? :b ::graphics? :c int? :d int? :e int?)
                                     :fifteen (s/cat :a ::action? :b ::graphics? :c string? :d int? :e int?)
                                     :sixteen (s/cat :a ::action? :b ::graphics? :c ::rect?)
                                     :seventeen (s/cat :a ::action? :b ::graphics? :c ::font?)
                                     :eighteen (s/cat :a ::action? :b ::graphics? :c ::shape?)
                                     :nineteen (s/cat :a ::action? :b ::graphics? :c ::color?)
                                     :twenty (s/cat :a ::action? :b ::graphics? :c int? :d int?)))

