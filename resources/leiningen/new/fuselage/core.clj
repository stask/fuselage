(ns {{namespace}}.core
  (:require
   [com.stuartsierra.component :as component]
   [clojure.tools.reader.edn   :as edn]
   [{{namespace}}.system       :refer [new-system]])
  (:gen-class))

(def system nil)

(defn -main [config-file]
  (let [config (edn/read-string (slurp config-file))]
    (alter-var-root #'system (fn [_] (component/start (new-system config))))
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. #(component/stop system)))))
