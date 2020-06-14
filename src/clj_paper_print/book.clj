(ns clj-paper-print.book
(:import [java.awt.print Book Paper Printable PageFormat])
(:require ;;[clj-paper-print.paper :refer :all]
          [clojure.spec.alpha :as s]
          ; [clojure.spec.test.alpha :as stest]
          [expound.alpha :as expound]
          [spec.book-spec :as bs]))


(defn append
  "A function that will take a vector as input and will append a (^Printable)`printable` to the (^Book) `abook` object with a (^PageFormat) `pageformat`.
   The function will accept a vector of `[abook printable pageformat]` and after the spec
    validation  will return a map with the addition of the dispatch keyword for the appropriate `book` multimethod."
  [[^Book abook ^Printable printable ^Pageformat pageformat]]
    (try
      (.append abook printable pageformat)
      (catch Exception e (.getMessage e))))

(defn append-wp
  "A function that will take a vector as input and will append a (^Printable)`printable` page to the (^Book) `abook` object with a (^PageFormat) `PageFormat` and
   with the number_of_pages that will append.
   The function will accept a vector of [^Book ^Printable ^PageFormat ^Int] and after the spec
   validation will return a map with addition of the dispatch keywork for the appropriate `book` multimethod."
  [[^Book abook ^Printable printable ^PageFormat pageformat ^int num_of_pages ]]
    (try
      (.append abook printable pageformat num_of_pages)
      (catch Exception e (.getMessage e))))
(defn get-num-of-pages
  "A function that will accept a (^Book) `book` object and will return the number of pages of the book as integer."
  [^Book abook]
    (try
      (.getNumberOfPages abook)))
(defn get-page-format  
  "A function that will take a vector as input and will accept a (^Book) `book` object and an int with the `page-index` of the page 
   to return the (^PageFormat) `pageformat` of the printable on the requested index of the book."
  [[^Book abook thepage-index]]
    (try
      (.getPageFormat abook thepage-index)
      (catch Exception e (.getMessage e))))
(defn get-printable
  "A function that will take a vector as input and will accept a (^Book) `book` object and an int with the `page-index` of the page
  to return the (^Printable) `printable` object of the requested index in the Book object."
  [[^Book abook page-index]]
    (try
      (.getPrintable abook page-index)
      (catch Exception e (.getMessage e))))
(defn set-page
  "A function that will take a vector as input and will accept  the supplied (^PageFormat) `pageformat` the painter (^Printable) `printable` to the `page-index`
  of (^Book) `book`."
  [[^Book abook ^Printable printable ^PageFormat pageformat ^int page-index]]
    (try
      (.setPage abook page-index printable pageformat)
      (catch Exception e (.getMessage e))))

(defmulti book
  "A multimethod for Book objects that will execute the methods of the `java.awt.print.Book` class, using the corresponding
  helper functions: `clj-paper-print.book/append` `clj-paper-print.book/append-wp` `clj-paper-print.book/get-num-of-pages`
  `clj-paper-print.book/get-page-format` `clj-paper-print.book/get-printable` `clj-paper-print.book/set-page`."
  (fn [[action _ _ _ _ _ :as cfg]]
    (if-not (s/valid? ::bs/book-multi-input cfg)
      (throw (ex-info "book input failed! " (expound/expound ::bs/book-multi-input cfg)))
      action)))

(defmethod book :append
  [[_ ^Book book ^Printable printable ^PageFormat pageformat _ _]]
  (append [book printable pageformat]))

(defmethod book :append-wp
  [[_ ^Book book ^Printable printable ^PageFormat pageformat num_of_pages _]]
  (append-wp [book printable pageformat num_of_pages]))

(defmethod book :get-num-of-pages
  [[_ ^Book book]]
  (get-num-of-pages book))

(defmethod book :get-page-format
  [[_ ^Book book page-index]]
  (get-page-format [book page-index]))

(defmethod book :get-printable
  [[_ ^Book book page-index]]
  (get-printable [book page-index]))

(defmethod book :set-page
  [[_ ^Book book ^Printable printable ^PageFormat pageformat page-index]]
  (set-page [book printable pageformat page-index]))
