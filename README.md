# clj-paper-print
![](resources/images/printer.png | width=300)

Clj-paper-print is a Clojure thin wrapper library for java.awt.print package. It's an effort to simplify the process of printing on actual paper from Clojure. At the current state provides just a basic functionality but more features will come.
## Why
I was working on a personal project and at the time I needed to print from Clojure on a thermal printer, so as my search for an equivelant library did not bear any fruit and due to that the alternative would need excessive use of java interop, I decided to fill the gap.
## Documentation
[Docs](http://narocath.github.io/clj-paper-print)
## Installation
### leiningen
```clojure
[clj-paper-print "0.1.0"]
```
## Usage
First add it on the project:
```clojure
(ns project.foo
    (:require [clj-paper-print.core :refer :all]
              [clj-paper-print.media-size-names :refer :all]))
```
Define the paper with it's size along with the size of the imageable area of the paper that the printing of the graphics will take place. The size and the imageable area should be a map with the required keys, as bellow, more on docs.
#### media-size
```clojure
(def paper-size {:size (media-size! [:inch A4])
            :im-area (media-size! [:inch-im A4])})
;; user=> paper-size
;; user=> {:size {:width 8.27, :height 11.69}, :im-area {:im-w 8.27, :im-h 11.69, :x 0.0, :y 0.0}}
```
**Note:** ```:x, :y``` are the points from where the imageable area should start.
#### paper
```clojure
(defpaper apaper :from-inches paper-size)
;; user => #object[java.awt.print.Paper 0x59ebd45e "java.awt.print.Paper@59ebd45e"]
```
or
```clojure
(defpaper apaper)
;; user => #object[java.awt.print.Paper 0x59ebd45e "java.awt.print.Paper@59ebd45e"]
```
```Clojure
;; from the already defined `paper-size`
(paper! [:set-size :from-inches (:size paper-size)])
;; => true
(paper! [:set-im-area :from-inches (:im-size paper-size)])
;; => true
;; more on docs for the `clj-paper-print.paper/paper` multimethod
```
**Note**: By not providing a size it will create the paper with the printer's default sizes. Also the default input for setting the paper size will accept the dimensions on units of 1/72 of an inch, hence the addition of ```:from-inches```, which will translate it to screen units.
``` clojure
(paper! [:get-w :in-inches apaper])
;; => 8.27
(paper! [:get-h :in-mm apaper])
;; => 11.69
```
#### pageformat
Next define the pageformat, in which will add the paper from above.
```clojure
(defpageformat pgf)
;; => #object[java.awt.print.PageFormat 0x7ed183b0 "java.awt.print.PageFormat@7ed183b0"]
(pageformat! [:pf-set-paper pgf apaper])
;; => true
(pageformat! [:pf-set-orientation pgf portrait])
;; => true
```
or
```clojure
(defpageformat pgf {:paper apaper :orientation portrait})
;; => #object[java.awt.print.PageFormat 0x9fec4b0 "java.awt.print.PageFormat@9fec4b0"]
```
#### printable
Define the printable which will use the  ```java.awt.Graphics``` object to render the 2d graphics to the page. This can be a string, a file (txt only for the moment), or any other Graphics element that can be drawn on paper. For the implemented Graphics methods, check the ```clj-paper-print/graphics``` multimethods at the docs along with the ```java.awt.Graphics```.
##### one-page document
The printable multi, for simple prints, accepts a vector of vectors with the desired graphics dispatch methods.
```clojure
(def prnt (printable! [:one-page [
                                [:draw-string "foo" 10 10]
                                [:draw-rect 1 1 100 100]]))
```
It will dispatch in sequence each vector to the ```clj-paper-print.graphics/graphics-methods``` multimethod and will create the graphics object which will be printed.
**Note** for the simple needs it will automatically translate the graphics and dispose them afterwards. For more control and for the addition of further logic at the Graphics object, the ```clj-paper-print.printable/printable``` multimethod can accept also a function, see bellow:
```clojure
(defn prnt
    [^Graphics g ^PageFormat pf page-index]
    (do
    (graphics-methods! [:translate g (.getImageableX pf) (.getImageableY pf)])
    (graphics-methods! [:draw-string g "foo" 10 10])
    (graphics-methods! [:draw-rect g 1 1 100 100])
    (graphics-methods! [:dispose! g])))
    
(def prnt (printable! [:one-page-func prnt]))
```
##### multi-page document
For multi-page document is implemented by java`s ```java.awt.print.Book``` class, which implements the ```Pageable``` interface. For the available methdos see ```clj-paper-print.book```.
```clojure
(defbook abook)
(def prnt1 (printable! [:multi-page [[:draw-string "foo" 10 10]]]))
(def prnt2 (printable! [:multi-page [[:draw-rect 1 1 100 100]]]))
(book! [:append abook prnt1 pgf]) ;; index 0
(book! [:append abook prnt2 pgf]) ;; index 1
;; same
(def prnt1-2
    [g pf p]
    (do
    (graphics-methdos! [:translate g (.getImageableX pf) (.getImageableY pf)])
    (if (= p 0)
        (graphics-methods! [:draw-string g "foo" 10 10]))
    (if (= p 1)
        (graphics-methdos! [:draw-rect g 1 1 100 100]))
    (graphics-methods! [:dispose! g])))
```
#### printer-job
The next step is to create a printjob, on which the page will be printed. At the printJob we also set the printer (PrintService) that we want to print the page.
We can create a printerJob or to be created automatically with the default printer.
```clojure
(defprintjob foojob) 
;; to see the available printers
(def printer-map (print-job! [:lookup-p-serv!]))
;; => {:Zijiang-ZJ-58 #object[sun.print.IPPPrintService 0x54ff41a1 "IPP Printer : Zijiang-ZJ-58"], :POS58 #object[sun.print.IPPPrintService 0x465a40f8 "IPP Printer : POS58"]}
(print-job! [:set-print-service! foojob (:Zijiang-ZJ-58 printer-map)])
```
#### print
We have two choices, one to use the ```clj-paper-print.core/print->one!```  multiarity function to print, two to use directly the ```clj-paper-print.core/print-job!``` multi with the ```:print!``` dispatch.
```clojure
(print->one! ^Printable prnt1 ^PageFormat pgf) ;; automatically will create a printjob with the default printer
(print->one! ^Printable prnt1 ^PageFormat pgf ^PrinterJob foojob) ;; will use the supplied printerJob
;; for multi-page
(print->multi! ^Pageable abook)
(print->multi! ^Pageable abook ^PrinterJob foojob)
```
or
```clojure
(print-job! [:set-printable! foojob prnt1])
(print-job! [:print! foojob])
;; for multi-page
(print-job! [:set-pageable! foojob abook])
(print-job! [:print! foojob abook])
```
#### printer-state
To check the printer state use the ```clj-paper-print.core/printer-state?``` function
```clojure
(printer-state? ^PrintService (:Zijiang-ZJ-58 printer-map) true)
;; =>
" \---------- PrinterState ----------\
color-supported  =  not-supported
printer-is-accepting-jobs  =  accepting-jobs
printer-name  =  Zijiang-ZJ-58
queued-job-count  =  1
pdl-override-supported  =  not-attempted
printer-info  =  Zijiang ZJ-58
----------END PrinterState--------"
;;
(printer-state? (:Zijiang-ZJ-58 printer-map) false)
;; => {:color-supported "not-supported", :printer-is-accepting-jobs "accepting-jobs", :printer-name "Zijiang-ZJ-58", :queued-job-count "1", :pdl-override-supported "not-attempted", :printer-info "Zijiang ZJ-58"}
```
### Current functionality
* Strings
* Shapes that implemented at ```java.awt.Graphics```
* Simple txt files
### TODO
* add csv support
* add pdf support
* implement also ```java.awt.Graphics2D```
* fully implement also ```javax.print``` api

### License
MIT


