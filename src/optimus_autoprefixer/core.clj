(ns optimus-autoprefixer.core
  (:require [clojure.string :as str]
            [optimus.assets.creation :refer [last-modified existing-resource]]
            [optimus.js :as js]))

(defn- escape [s]
  (-> s
      (str/replace "\\" "\\\\")
      (str/replace "'" "\\'")
      (str/replace "\n" "\\n")))

(defn normalize-line-endings [s]
  (-> s
      (str/replace "\r\n" "\n")
      (str/replace "\r" "\n")))

(defn- prefix-code [css options]
  (let [opts (if-let [browsers (seq (:browsers options))]
               (str "{ overrideBrowserslist: ['" (str/join "', '" browsers) "']}")
               "{}")]
    (str "autoprefixer.process('" (escape (normalize-line-endings css)) "', {}, " opts ").css;")))

(def autoprefixer
  (slurp (clojure.java.io/resource "autoprefixer.js")))

(defn prepare-autoprefixer-engine []
  (let [engine (js/make-engine)]
    (.eval engine autoprefixer)
    engine))

(defn prefix-css
  ([css] (prefix-css css {}))
  ([css options]
   (js/with-engine [engine (prepare-autoprefixer-engine)]
     (prefix-css engine css options)))
  ([engine css options]
   (js/run-script-with-error-handling
    engine
    (prefix-code css (dissoc options :path))
    (:path options))))

(defn prefix-css-asset
  [engine asset options]
  (let [#^String path (:path asset)]
    (if (.endsWith path ".css")
      (update-in asset [:contents] #(prefix-css engine % (assoc options :path path)))
      asset)))

(defn prefix-css-assets
  ([assets] (prefix-css-assets assets {}))
  ([assets options]
   (js/with-engine [engine (prepare-autoprefixer-engine)]
     (doall (map #(prefix-css-asset engine % options) assets)))))
