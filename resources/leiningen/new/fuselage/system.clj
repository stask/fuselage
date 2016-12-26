(ns {{namespace}}.system
  (:require
   [com.stuartsierra.component :as component]
   [{{namespace}}.web-server   :refer [new-web-server]]))

(defn new-system [config]
  (component/system-map
   :web-server (new-web-server (:web-server config))))
