(ns spec.book-spec
  (:import [java.awt.print Book Printable PageFormat])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))


(def book-action? #{:append
                    :append-wp
                    :get-num-of-pages
                    :get-page-format
                    :get-printable
                    :set-page})
(s/def ::action book-action?)
(s/def ::book #(instance? java.awt.print.Book %))
(s/def ::printable #(instance? java.awt.print.Printable %))
(s/def ::pageformat #(instance? java.awt.print.PageFormat %))
(s/def ::page-num int?)
(s/def ::page-idx int?)
(s/def ::book-spec-input (s/cat
                         :act ::action
                         :b ::book
                         :pr (s/? ::printable)
                         :pg (s/? ::pageformat)
                         :pn (s/? ::page-num)
                         :pidx (s/? ::page-idx)))
(s/fdef clj-paper-print.book/defbook
  :args (s/cat :name symbol?)
  :ret ::book)

(s/def ::book-spec-append (s/tuple
                          ::book ::printable ::pageformat))
(s/def ::book-spec-append-wp (s/tuple ::action ::book ::printable ::pageformat))
(s/def ::book-spec-get-num-of-pages ::book)
(s/def ::book-spec-get-page-format (s/tuple ::book ::page-idx))
(s/def ::book-spec-get-printable (s/tuple ::book ::page-idx))
(s/def ::book-spec-set-page (s/tuple ::book ::page-idx ::printable ::pageformat))

(s/def ::book-multi-input (s/alt :one (s/cat :a ::action :b ::book)
                                 :two (s/cat :a ::action :b ::book :p-idx ::page-idx)
                                 :three (s/cat :a ::action :b ::book :pr ::printable :pf ::pageformat :p-num ::page-num)
                                 :four (s/cat :a ::action :b ::book :pr ::printable :pf ::pageformat)))


(s/fdef clj-paper-print.book/append
  :args ::book-spec-append
  :ret any?)
;; For Book object generation for testing.
(s/def ::Book-gen
  (s/with-gen #(instance? java.awt.print.Book %)
    #(gen/fmap (gen/set (Book.)))))
