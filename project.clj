(defproject clj-paper-print "0.1.0"
  :description "A thin wraper of java.awt.print for clojure."
  :url "https://github.com/narocath/clj-paper-print"
  :license {:name "The MIT Licence"
            :url "http://opensource.org/licenses/MIT"}
  :global-vars {*warn-on-reflection* false}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [expound "0.7.2"]]
  :repl-options {:init-ns clj-paper-print.core}
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.10.0"]
                                  [org.clojure/data.generators "0.1.2"]]
                   :plugins [[lein-codox "0.10.6"]]}})
