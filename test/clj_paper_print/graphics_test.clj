(ns clj-paper-print.graphics-test
  (:require  [clojure.test :refer :all]
             [clj-paper-print.graphics :refer [graphics-methods]])
  (:import [javax.swing JPanel JFrame]
           [java.awt Graphics]
           [java.awt.print Paper PageFormat]
           [java.awt.image BufferedImage]))
;; Because the java.awt.Graphics haven't got public constructor to test
;; that the methods of the `clj-paper-print.graphics/graphics-methods` are called
;; I create two buffered images which I compare pixel to pixel.
(defn test-panel
  "A function that will use the Graphics object of the supplied image to draw, using the `clj-paper-print.graphics/graphics-methods`."
  [^BufferedImage im graph-vec]
  (dorun (map #(graphics-methods %) (map #(into (conj (subvec % 0 1) ^Graphics (.getGraphics ^BufferedImage im)) (subvec % 1)) graph-vec))))
(defn test-compare-images
  "A function that will compare two buffered images pixel by pixel.
  If the images match return an empty list, else a list with false."
  [^BufferedImage img1 ^BufferedImage img2]
  (let [width (.getWidth img1)
        height (.getHeight img1)]
    (if (or (not= (.getWidth img1) (.getWidth img2)) (not= (.getHeight img1) (.getHeight img2)))
      false
      (for [y (range height)
            x (range width)
            :let [rgb1 (.getRGB img1 x y) rgb2 (.getRGB img2 x y)]
            :when (not= rgb1 rgb2 )]
        false))))

(def bimg-1 (BufferedImage. 250 250 BufferedImage/TYPE_INT_RGB))
(def bimg-2 (BufferedImage. 250 250 BufferedImage/TYPE_INT_RGB))

(def graph1 [[:draw-line 10 10 200 300] [:draw-string "hello " 10 10]])
(def graph2 [[:draw-line 10 10 200 300] [:draw-string "hello foo" 10 10]])
(def graph3 [[:draw-rect 5 5 100 100]])
(def graph4 [[:draw-rect 5 5 100 100] [:draw-string "foo foo" 10 10]])
(def graph5 [[:draw-line 10 10 200 300] [:draw-string "hello " 10 5]])
(def graph6 [[:draw-line 10 10 200 300] [:draw-string "hello " 10 10]])
(def graph7 [[:draw-line 10 10 200 300] [:draw-string "foo foo" 10 10]])
(def graph8 [[:draw-rect 5 5 100 200]])

(deftest test-graphics
  (testing "compare :draw methods"
    (do
      (test-panel bimg-1 graph1)
      (test-panel bimg-2 graph6)
      (is (empty? (test-compare-images bimg-1 bimg-2))))
    (do
      (test-panel bimg-1 graph1)
      (test-panel bimg-2 graph2)
      (is (not (empty? (test-compare-images bimg-1 bimg-2)))))
    (do
      (test-panel bimg-1 graph5)
      (test-panel bimg-2 graph6)
      (is (not (empty? (test-compare-images bimg-1 bimg-2)))))
    (do
      (test-panel bimg-1 graph3)
      (test-panel bimg-2 graph8)
      (is (not (empty? (test-compare-images bimg-1 bimg-2)))))
    (do
      (test-panel bimg-1 graph4)
      (test-panel bimg-2 graph7)
      (is (not (empty? (test-compare-images bimg-1 bimg-2)))))))

