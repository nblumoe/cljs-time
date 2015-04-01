(defproject com.andrewmcveigh/cljs-time "0.3.4-SNAPSHOT"
  :description "A clj-time inspired date library for clojurescript."
  :url "https://github.com/andrewmcveigh/cljs-time"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:url "git@github.com:andrewmcveigh/cljs-time.git"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2511"]]

  :plugins [[com.cemerick/clojurescript.test "0.3.3"]
            [lein-cljsbuild "1.0.3"]
            [lein-marginalia "0.8.0"]
            [com.cemerick/austin "0.1.5"]
            [com.andrewmcveigh/lein-auto-release "0.1.10"]]

  :jvm-opts ["-Djava.awt.headless=true"]
  :clojurescript? true
  :cljsbuild
  {:builds
   {:dev {:source-paths ["src"]
          :compiler {:output-to "target/main.js"
                     :optimizations :whitespace
                     :pretty-print true}}
    :test {:source-paths ["src" "test"]
           :incremental? true
           :notify-command ["phantomjs" :cljs.test/runner "target/unit-test.js"]
           :compiler {:output-to "target/unit-test.js"
                      :optimizations :whitespace
                      :pretty-print true}}
    :dce-test {:source-paths ["src" "test"]
               :incremental? false
               :compiler {:output-to "target/dce-test.js"
                          :main cljs-time.dce-compile-test
                          :optimizations :advanced
                          :pretty-print false}}}}
  :profiles
  {:prod
   {:hooks [leiningen.cljsbuild]
    :cljsbuild
    {:builds {:whitespace {:source-paths ["src" "test"]
                           :compiler {:output-to "target/cljs/whitespace.js"
                                      :optimizations :whitespace
                                      :pretty-print true}}
              :simple {:source-paths ["src" "test"]
                       :compiler {:output-to "target/cljs/simple.js"
                                  :optimizations :simple
                                  :pretty-print true}}
              :advanced {:source-paths ["src" "test"]
                         :compiler {:output-to "target/cljs/advanced.js"
                                    :optimizations :advanced
                                    :pretty-print true}}}
     :test-commands
     {"phantom-whitespace"
      ["phantomjs" "resources/runner.js" "target/cljs/whitespace.js"]
      "phantom-simple"
      ["phantomjs" "resources/runner.js" "target/cljs/simple.js"]
      "phantom-advanced"
      ["phantomjs" "resources/runner.js" "target/cljs/advanced.js"]}}}}

  :aliases {"test-all" ["with-profile" "prod" "test"]}

  :release-tasks [["vcs" "assert-committed"]
                  ["clean"]
                  ["test-all"]
                  ["auto-release" "checkout" "master"]
                  ["auto-release" "merge-no-ff" "develop"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["auto-release" "update-release-notes"]
                  ["auto-release" "update-readme-version"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "v"]
                  ["deploy" "clojars"]
                  ["vcs" "push"]
                  ["auto-release" "checkout" "develop"]
                  ["auto-release" "merge" "master"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]
                  ["auto-release" "checkout-latest-tag"]
                  ["marg"]
                  ["auto-release" "update-marginalia-gh-pages"]])
