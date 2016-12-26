(ns {{namespace}}.core
  (:require
   [reagent.core  :refer [render]]
   [re-frame.core :refer [dispatch]]
   [devtools.core :as devtools]
   [{{namespace}}.config :as config]
   [{{namespace}}.events]
   [{{namespace}}.subs]
   [{{namespace}}.views :refer [main-panel]]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")
    (devtools/install! [:formatters :hints])))

(defn mount-root []
  (render [main-panel] (.getElementById js/document "app")))

(defn ^:export init []
  (dispatch [:initialize-db])
  (dev-setup)
  (mount-root))
