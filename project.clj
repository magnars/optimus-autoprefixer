(defproject optimus-autoprefixer "0.1.0-SNAPSHOT"
  :description "Autoprefixer, CSS prefixing for Optimus."
  :url "http://github.com/magnars/optimus-autoprefixer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [optimus "0.19.0"]
                 [clj-v8 "0.1.5"]]
  :profiles {:dev {:dependencies [[midje "1.8.3"]
                                  [test-with-files "0.1.1"]]
                   :plugins [[lein-midje "3.2"]
                             [lein-shell "0.5.0"]]
                   :resource-paths ["test/resources"]}}
  :prep-tasks [["shell" "./build-js-sources.sh"]])
