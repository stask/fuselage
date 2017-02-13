(ns leiningen.new.fuselage
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files
                                             project-name sanitize-ns]]
            [leiningen.core.main     :as main]
            [clojure.string          :as string]))

(def render (renderer "fuselage"))

(defn fuselage
  "Generates new application."
  [name & opts]
  (println "params:" opts)
  (let [render  (renderer "fuselage")
        main-ns (sanitize-ns name)
        data    {:raw-name       name
                 :name           (project-name name)
                 :namespace      main-ns
                 :cljs-namespace (string/replace main-ns \- \_)
                 :sanitized      (name-to-path name)}
        files   [["README.md"
                  (render "README.md" data)]
                 [".gitignore" (render "gitignore")]
                 ["project.clj"
                  (render "project.clj" data)]
                 ["dev/user.clj"
                  (render "user.clj" data)]
                 ["src/clj/{{sanitized}}/core.clj"
                  (render "core.clj" data)]
                 ["src/clj/{{sanitized}}/system.clj"
                  (render "system.clj" data)]
                 ["src/clj/{{sanitized}}/web_server.clj"
                  (render "web_server.clj" data)]
                 ["test/clj/{{sanitized}}/core_test.clj"
                  (render "core_test.clj" data)]
                 ["src/cljs/{{sanitized}}/core.cljs"
                  (render "core.cljs" data)]
                 ["src/cljs/{{sanitized}}/config.cljs"
                  (render "config.cljs" data)]
                 ["src/cljs/{{sanitized}}/db.cljs"
                  (render "db.cljs" data)]
                 ["src/cljs/{{sanitized}}/events.cljs"
                  (render "events.cljs" data)]
                 ["src/cljs/{{sanitized}}/subs.cljs"
                  (render "subs.cljs" data)]
                 ["src/cljs/{{sanitized}}/views.cljs"
                  (render "views.cljs" data)]
                 ["resources/catacumba.basedir"
                  (render "catacumba.basedir")]
                 ["resources/public/index.html"
                  (render "index.html" data)]
                 ["resources/public/css/app.css"
                  (render "app.css")]]]
    (main/info "Generating fresh 'lein new' fuselage project.")
    (apply ->files data files)))
