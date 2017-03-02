(defproject {{raw-name}} "0.1.0-SNAPSHOT"
  :description "TODO: FIXME"
  :url "TODO: FIXME"
  :min-lein-version "2.7.0"

  :dependencies [[org.clojure/clojure            "1.9.0-alpha14"]
                 [org.clojure/tools.logging      "0.3.1"]
                 [org.clojure/tools.reader       "1.0.0-beta4"]
                 [com.stuartsierra/component     "0.3.2"]
                 [funcool/catacumba              "1.2.0"]
                 [ch.qos.logback/logback-classic "1.2.1"]
                 [org.clojure/core.async         "0.3.441"]
                 ;; client
                 [org.clojure/clojurescript  "1.9.495"]
                 [re-frame                   "0.9.2"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [binaryage/devtools         "0.9.2"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :profiles {:uberjar {:aot :all
                       :hooks [leiningen.cljsbuild]}
             :dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [figwheel-sidecar            "0.5.9"]
                                  [com.cemerick/piggieback     "0.2.1"]
                                  [org.clojure/test.check      "0.9.0"]]
                   :source-paths ["dev"]
                   :plugins [[lein-figwheel "0.5.9"]]}}

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    :target-path]

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]

  :main ^:skip-aot {{namespace}}.core

  :target-path "target/%s"
  :uberjar-name "{{name}}-standalone.jar"
  :figwheel {:css-dirs ["resources/public/css"]}

  :repl-options {:init-ns user
                 :init (set! *print-length* 100)
                 :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/cljs"]
                :figwheel {:on-jsload "{{namespace}}.core/mount-root"}
                :compiler {:main {{namespace}}.core
                           :output-to "resources/public/js/compiled/app.js"
                           :output-dir "resources/public/js/compiled/out"
                           :asset-path "js/compiled/out"
                           :source-map-timestamp true}}
               {:id "min"
                :source-paths ["src/cljs"]
                :compiler {:main {{namespace}}.core
                           :output-to "resources/public/js/compiled/app.js"
                           :optimizations :advanced
                           :closure-defines {goog.DEBUG false}
                           :pretty-print false}}]})
