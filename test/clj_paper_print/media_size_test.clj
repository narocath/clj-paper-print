(ns clj-paper-print.media-size-test
  (:require [clj-paper-print.media-size :refer :all]
            [clj-paper-print.media-size-names :refer :all]
            [clojure.test :refer :all]
            [clj-paper-print.utility :refer :all])
  (:import [javax.print.attribute.standard MediaSizeName MediaSize Media]
           [javax.print.attribute Size2DSyntax]
           [java.awt.print PrinterJob]))

(defmacro create-scene-ms
  ""
  []
  `(do
     (def a4-inch {:width (rounding (double (.getX (MediaSize/getMediaSizeForName A4) Size2DSyntax/INCH)))
                   :height (rounding (double (.getY (MediaSize/getMediaSizeForName A4) Size2DSyntax/INCH)))})
     (def a4-mm {:width (rounding (double (.getX (MediaSize/getMediaSizeForName A4) Size2DSyntax/MM)))
                 :height (rounding (double (.getY (MediaSize/getMediaSizeForName A4) Size2DSyntax/MM)))})
     (def a4-inch-im {:im-w (rounding (double (.getX (MediaSize/getMediaSizeForName A4) Size2DSyntax/INCH)))
                      :im-h (rounding (double (.getY (MediaSize/getMediaSizeForName A4) Size2DSyntax/INCH)))
                      :x 0.0 :y 0.0})
     (def a4-mm-im {:im-w (rounding (double (.getX (MediaSize/getMediaSizeForName A4) Size2DSyntax/MM)))
                    :im-h (rounding (double (.getY (MediaSize/getMediaSizeForName A4) Size2DSyntax/MM)))
                    :x 0.0 :y 0.0})
     (def a4-screen {:width (rounding (* 72.0 (double (.getX (MediaSize/getMediaSizeForName A4) Size2DSyntax/INCH))))
                     :height (rounding (* 72.0 (double (.getY (MediaSize/getMediaSizeForName A4) Size2DSyntax/INCH))))})
     (def a4-screen-im {:im-w (rounding (* 72.0 (double (.getX (MediaSize/getMediaSizeForName A4) Size2DSyntax/INCH))))
                        :im-h (rounding (* 72.0 (double (.getY (MediaSize/getMediaSizeForName A4) Size2DSyntax/INCH))))
                        :x 0.0 :y 0.0})))
(defn clear-scene-ms
  "A function to reset the 'scene' for the tests."
  []
  (map #(ns-unmap *ns* %) ['a4-inch 'a4-mm 'a4-inch-im 'a4-mm-im]))

(deftest test-media-size-multi
  (testing ":inch/inch-im"
    (try
      (create-scene-ms)
      (is (= (media-size [:inch A4]) a4-inch))
      (is (= (media-size [:inch-im A4]) a4-inch-im))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-ms))))
  (testing ":mm/mm-im"
    (try
      (create-scene-ms)
      (is (= (media-size [:mm A4]) a4-mm))
      (is (= (media-size [:mm-im A4]) a4-mm-im))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-ms))))
  (testing ":screen/screen-im"
    (try
      (create-scene-ms)
      (is (= (media-size [:screen A4]) a4-screen))
      (is (= (media-size [:screen-im A4]) a4-screen-im))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-ms)))))
(deftest test-ms-lookup-size-multi
  (testing "inch"
    (create-scene-ms)
    (try
      (is (and
           (= A4 (:iso-a4 (ms-lookup-size [:inch 8.27 11.69])))
           (not= A3 (:iso-a4 (ms-lookup-size [:inch 8.27 11.69])))))
      (catch Exception e (.getMessage e))))
  (testing "mm"
    (try
      (is (and
           (= A4 (:iso-a4 (ms-lookup-size [:mm 210.0 297.0])))
           (not= A3 (:iso-a4 (ms-lookup-size [:mm 210.0 297.0])))))
      (catch Exception e (.getMessage e))
      (finally (clear-scene-ms)))))
(deftest test-me-supported-sizes
  (try
    (create-scene-ms)
    (doseq [x (into [] (vals (ms-supported-sizes (.getPrintService (PrinterJob/getPrinterJob)))))]
      (is (instance? javax.print.attribute.standard.MediaSizeName x)))
    (catch Exception e (.getMessage e))
    (finally (clear-scene-ms))))
