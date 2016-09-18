(ns optimus-autoprefixer.core
  (:require [clojure.string :as str]
            [optimus.assets.creation :refer [last-modified existing-resource]]
            [optimus.optimizations.minify :refer [with-context]]
            [v8.core :as v8]))

(defn- escape [s]
  (-> s
      (str/replace "\\" "\\\\")
      (str/replace "'" "\\'")
      (str/replace "\n" "\\n")))

(defn- throw-v8-exception [#^String text path]
  (if (= (.indexOf text "ERROR: ") 0)
    (let [prefix (when path (str "Exception in " path ": "))
          error (clojure.core/subs text 7)]
      (throw (Exception. (str prefix error))))
    text))

(defn- run-script-with-error-handling [context script file-path]
  (throw-v8-exception
   (try
     (v8/run-script-in-context context script)
     (catch Exception e
       (str "ERROR: " (.getMessage e))))
   file-path))

(defn normalize-line-endings [s]
  (-> s
      (str/replace "\r\n" "\n")
      (str/replace "\r" "\n")))

(defn- prefix-code [css options]
  (let [opts (if-let [browsers (seq (:browsers options))]
               (str "{ browsers: ['" (str/join "', '" browsers) "']}")
               "{}")]
    (str "(function () {
    try {
        return autoprefixer.process('" (escape (normalize-line-endings css)) "', " opts ").css;
    } catch (e) { return 'ERROR: ' + e.message; }
}());")))

(def autoprefixer
  (slurp (clojure.java.io/resource "autoprefixer.js")))

(defn create-autoprefixer-context []
  (let [context (v8/create-context)]
    (v8/run-script-in-context context autoprefixer)
    context))

(defn prefix-css
  ([css] (prefix-css css {}))
  ([css options]
   (with-context [context (create-autoprefixer-context)]
     (prefix-css context css options)))
  ([context css options]
   (run-script-with-error-handling context (prefix-code css (dissoc options :path)) (:path options))))

(defn prefix-css-asset
  [context asset options]
  (let [#^String path (:path asset)]
    (if (.endsWith path ".css")
      (update-in asset [:contents] #(prefix-css context % (assoc options :path path)))
      asset)))

(defn prefix-css-assets
  ([assets] (prefix-css-assets assets {}))
  ([assets options]
   (with-context [context (create-autoprefixer-context)]
     (doall (map #(prefix-css-asset context % options) assets)))))
