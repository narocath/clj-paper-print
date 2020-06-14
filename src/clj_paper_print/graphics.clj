(ns clj-paper-print.graphics
  (:import [java.awt Graphics Color Font Image Rectangle Shape Polygon]
           [java.awt.image ImageObserver])
  (:require [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [spec.graphics-spec :as gspec]))




(defn create
  "A multi-arity function that will create a copy of the supplied Graphics object.
  The function will accept either just the Graphics object or the graphics object with the dimensions."
  ([^Graphics g]
  (try
    (.create g)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height]
  (try
    (.create g x y width height)
    (catch Exception e (prn "Caugt `graphics-methods` exception: " (.getMessage e))))))
(defn copy-area
  "A function that will copy a compoment's area by the supplied distance."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height ^Integer xdistance ^Integer ydistance]
  (try
    (.copyArea g x y width height xdistance ydistance)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn clear-rect!
  "A function that will clear the supplied rectangle by filling it with the background color of the drawing surface."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height]
  (try
    (.clearRect g x y width height)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn clip-rect
  "A function that will intersect the supplied rectangle with the clip area."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height]
  (try
    (.clipRect g x y width height)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn dispose!
  "A function that will dispose the supplied Graphics object and release system resources."
  [^Graphics g]
  (try
    (.dispose g)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-3d-rect
  "A function that will create a 3D outline rectangle with the supplied dimensions."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height ^Boolean raised]
   (try
     (.draw3DRect g x y width height raised)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-arc
  "A function that will draw a circular/eliptical arc."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height ^Integer start-angle ^Integer arc-angle]
  (try
    (.drawArc g x y width height start-angle arc-angle)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-bytes
  "A function that will draw the supplied text in a form of a byte-array using current font and color."
  [^Graphics g ^bytes data ^Integer offset ^Integer length ^Integer x ^Integer y]
  (try
    (.drawBytes g data offset length x y)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-chars
  "A function that will draw the supplied char-array using the current font and color."
  [^Graphics g ^chars data ^Integer offset ^Integer length ^Integer x ^Integer y]
  (try
    (.drawChars g data offset length x y)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-image
  "A function with multi arity that will draw an Image.
  It will need to be supplied with a BufferedImage object and the analogous coordinations and
  dimensions depending of the input variations of the method.
  For more details visit the java
  'https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics.html#drawImage-java.awt.Image-int-int-java.awt.Color-java.awt.image.ImageObserver-',
  also normally the drawImage method can take an ImageObserver object as last argument, but usually this will be nil."
  ([^Graphics g ^Image image ^Integer x ^Integer y ^Color bgcolor ^ImageObserver imgobserver]
   (try
     (.drawImage g image x y bgcolor imgobserver)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Image image ^Integer x ^Integer y ^Integer width ^Integer height ^ImageObserver imgobserver]
   (try
     (.drawImage g image x y width height imgobserver)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Image image ^Integer x ^Integer y ^Integer width ^Integer height ^Color bgcolor ^ImageObserver imgobserver]
   (try
     (.drawImage g image x y width height bgcolor imgobserver)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Image image ^Integer dx1 ^Integer dy1
                 ^Integer dx2 ^Integer dy2
                 ^Integer sx1 ^Integer sy1
                 ^Integer sx2 ^Integer sy2
                 ^Color bgcolor ^ImageObserver imgobserver]
   (try
     (.drawImage g image dx1 dy1 dx2 dy2 sx1 sy1 sx2 sy2 bgcolor imgobserver)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Image image ^Integer dx1 ^Integer dy1
                 ^Integer dx2 ^Integer dy2
                 ^Integer sx1 ^Integer sy1
                 ^Integer sx2 ^Integer sy2 ^ImageObserver imgobserver]
   (try
     (.drawImage g image dx1 dy1 dx2 dy2 sx1 sy1 sx2 sy2 imgobserver)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e))))))
(defn draw-line
  "A function that will draw a line, for the supplied Graphics objcet along with the coordination and dimensions."
  [^Graphics g ^Integer x1 ^Integer y1 ^Integer x2 ^Integer y2]
  (try
    (.drawLine g x1 y1 x2 y2)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-oval
  "A function that will draw the outline of an oval."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height]
  (try
    (.drawOval g x y width height)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-polygon
  "A function with multi-arity, for the method `java.awt.Graphics/drawPolygon` with two variations on input.
  First with a Polygon object and second with the coordinations for each line in a sequence that is to draw.
  The second variation will accept an array of ints for x and y coordination with the number of points to be
  drawed."
  ([^Graphics g ^Polygon p]
   (try
     (.drawPolygon g p)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^ints xpoints ^ints ypoints ^Integer npoints]
   (try
     (.drawPolygon g xpoints ypoints npoints)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e))))))
(defn draw-polyline
  "A function that will draw a sequence of connected lines, by the supplied arrays of ints for x and y coordinations."
  [^Graphics g ^ints xpoints ^ints ypoints ^Integer npoints]
  (try
    (.drawPolyLine g xpoints ypoints npoints)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-rect
  "A function that will draw a Rectangle with the supplied coordination and dimensions."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height]
  (try
    (.drawRect g x y width height)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-round-rect
  "A function that will draw a Rectangle with round cornered with the supplied coordination and dimensions along with the
  arc width and height."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height ^Integer arc-width ^Integer arc-height]
  (try
    (.drawRoundRect g x y width height arc-width arc-height)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn draw-string
  "A function that will draw the supplied string.
  The function will use an atom to draw the string correctly if the supplied string has new line characters `\n`,
  specifically will split the string in new lines if the supplied string has any."
  [^Graphics g ^String string ^Integer x ^Integer y]
  (let [^Graphics g2d g, strg (clojure.string/split string #"\n") xpoint x ypoint (atom y)]
    (try
      (doseq [^String s strg]
      (.drawString ^Graphics g2d ^String s ^Integer xpoint ^Integer @ypoint)
      (swap! ypoint #(+ % (.getHeight (.getFontMetrics ^Graphics g2d)))))
      (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e))))))
(defn fill-arc
  "A function that will fill a circular/elliptical arc in the supplied coordinations, dimensions and the begining of
  the point of the arc along with the angle of the arc in Integer."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height ^Integer start-angle ^Integer arc-angle]
  (try
    (.fillArc g x y width height start-angle arc-angle)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn fill-oval
  "A function that will fill an oval with the supplied coordinations and dimensions."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height]
  (try
    (.fillOval g x y width height)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn fill-polygon
  "A function with multi-arity that will fill a closed polygon by the supplied array of x and y, or with the supplied Polygon object."
  ([^Graphics g ^ints xpoints ^ints ypoints ^Integer npoints]
   (try
     (.fillPolygon g xpoints ypoints npoints)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Polygon p]
   (try
     (.fillPolygon g p)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e))))))
(defn fill-rect
  "A function that will fill the supplied rectangle."
  [^Graphics g [^Integer x ^Integer y ^Integer width ^Integer height]]
  (try
    (.fillRect g x y width height)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn fill-round-rect
  "A function that will fill a RoundRect."
  [^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height ^Integer arc-width ^Integer arc-height]
  (try
    (.fillRoundRect g x y width height arc-width arc-height)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn finalize!
  "A function that will dispose the supplied Graphics object if no longer is referenced."
  [^Graphics g]
  (try
    (.finalize g)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn get-clip
  "A function that will return the current clip area."
  [^Graphics g]
  (try
    (.getClip g)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn get-clip-bounds
  "A multi-arity function that will return the bounding rectangle of the used clip area, or of the supplied rectangle."
  ([^Graphics g]
  (try
    (.getClipBounds g)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Rectangle r]
   (try
     (.getClipBounds g r)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e))))))
(defn get-color
  "A function that will return the Color object of the current context."
  [^Graphics g]
  (try
    (.getColor g)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn get-font
  "A function that will return the Font object of the current Graphics object."
  [^Graphics g]
  (try
    (.getFont g)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn get-font-metrics
  "A multi-arity function that will return the font metrics of the current font if is called without any other argument,
  or the font metrics of the supplied Font object."
  ([^Graphics g]
   (try
     (.getFontMetrics g)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Font font]
   (try
     (.getFontMetrics g font)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e))))))
(defn set-clip!
  "A multi-arity function that will set the dimensions of the clip area for the supplied Graphics object.
  The function will accept coordinations/dimensions or a Shape object."
  ([^Graphics g ^Shape shape]
   (try
     (.setClip g shape)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
  ([^Graphics g ^Integer x ^Integer y ^Integer width ^Integer height]
   (try
     (.setClip g x y width height)
     (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e))))))
(defn set-color!
  "A function that will set the Color object for the supplied Graphics object."
  [^Graphics g ^Color color]
  (try
    (.setColor g color)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn set-font!
  "A function that will set the Font object for the supplied Graphics object."
  [^Graphics g ^Font font]
  (try
    (.setFont g font)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))
(defn translate
  "A function that will translate the x y origins of the graphics context to the current coordinate system."
  [^Graphics g x y]
  (try
    (.translate g x y)
    (catch Exception e (prn "Caught `graphics-methods` exception: " (.getMessage e)))))

(defmulti graphics-methods
  (fn [[action _ & rest :as c]]
    (if-not (s/valid? ::gspec/graphics-multi-input? c)
      (throw (ex-info "graphics-methods input failed!" (expound/expound ::gspec/graphics-multi-input? c)))
      action)))
(defmethod graphics-methods :create
  [[_ ^Graphics g & rest]]
  (if (= rest nil)
    (create g)
    (apply create g rest)))
(defmethod graphics-methods :copy-area
  [[_ ^Graphics g & rest]]
  (apply copy-area g rest))
(defmethod graphics-methods :dispose!
  [[_ ^Graphics g]]
  (apply dispose! g))
(defmethod graphics-methods :clear-rect!
  [[_ ^Graphics g & rest]]
  (apply clear-rect! g rest))
(defmethod graphics-methods :clip-rect
  [[_ ^Graphics g & rest]]
  (apply clip-rect g rest))
(defmethod graphics-methods :draw-3d-rect
  [[_ ^Graphics g & rest]]
  (apply draw-3d-rect g rest))
(defmethod graphics-methods :draw-arc
  [[_ ^Graphics g & rest]]
  (apply draw-arc g rest))
(defmethod graphics-methods :draw-bytes
  [[_ ^Graphics g & rest]]
  (apply draw-bytes g rest))
(defmethod graphics-methods :draw-image
  [[_ ^Graphics g & rest]]
  (apply draw-image g rest))
(defmethod graphics-methods :draw-chars
  [[_ ^Graphics g & rest]]
  (apply draw-chars g rest))
(defmethod graphics-methods :draw-line
  [[_ ^Graphics g & rest]]
  (apply draw-line g rest))
(defmethod graphics-methods :draw-oval
  [[_ ^Graphics g & rest]]
  (apply draw-oval g rest))
(defmethod graphics-methods :draw-polygon
  [[_ ^Graphics g & rest]]
  (apply draw-polygon  g rest))
(defmethod graphics-methods :draw-polyline
  [[_ ^Graphics g & rest]]
  (apply draw-polyline g rest))
(defmethod graphics-methods :draw-rect
  [[_ ^Graphics g & rest]]
  (apply draw-rect g rest))
(defmethod graphics-methods :draw-round-rect
  [[_ ^Graphics g & rest]]
  (apply draw-round-rect g rest))
(defmethod graphics-methods :draw-string
  [[_ ^Graphics g & rest]]
  (apply draw-string g rest))
(defmethod graphics-methods :fill-arc
  [[_ ^Graphics g & rest]]
  (apply fill-arc g rest))
(defmethod graphics-methods :fill-oval
  [[_ ^Graphics g & rest]]
  (apply fill-oval g rest))
(defmethod graphics-methods :fill-polygon
  [[_ ^Graphics g & rest]]
  (apply fill-polygon g rest))
(defmethod graphics-methods :fill-rect
  [[_ ^Graphics g & rest]]
  (apply fill-rect g rest))
(defmethod graphics-methods :fill-round-rect
  [[_ ^Graphics g & rest]]
  (apply fill-round-rect g rest))
(defmethod graphics-methods :finalize!
  [_ ^Graphics g]
  (apply finalize! g))
(defmethod graphics-methods :get-clip
  [[_ ^Graphics g]]
  (apply get-clip g))
(defmethod graphics-methods :get-clip-bounds
  [[_ ^Graphics g]]
  (apply get-clip-bounds g))
(defmethod graphics-methods :get-color
  [[_ ^Graphics g]]
  (apply get-color g))
(defmethod graphics-methods :get-font
  [[_ ^Graphics g]]
  (apply get-font g))
(defmethod graphics-methods :get-font-metrics
  [[_ ^Graphics g]]
  (apply get-font-metrics g))
(defmethod graphics-methods :set-clip!
  [[_ ^Graphics g & rest]]
  (apply set-clip! g rest))
(defmethod graphics-methods :set-color!
  [[_ ^Graphics g & rest]]
  (apply set-color! g rest))
(defmethod graphics-methods :set-font!
  [[_ ^Graphics g & rest]]
  (apply set-font! g rest))
(defmethod graphics-methods :translate
  [[_ ^Graphics g & rest]]
  (apply translate g rest))
