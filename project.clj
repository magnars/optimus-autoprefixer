(defproject optimus-autoprefixer "1.0.0-rc1"
  :description "CSS prefixing via autoprefixer for Optimus."
  :url "http://github.com/magnars/optimus-autoprefixer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[optimus "1.0.0-rc3"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.0"]
                                  [midje "1.9.9"]
                                  [test-with-files "0.1.1"]]
                   :plugins [[lein-midje "3.2.1"]
                             [lein-shell "0.5.0"]]
                   :resource-paths ["test/resources"]}}
  :prep-tasks [["shell" "./build-js-sources.sh"]])
