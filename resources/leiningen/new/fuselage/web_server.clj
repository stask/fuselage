(ns {{namespace}}.web-server
  (:require
   [com.stuartsierra.component :as component]
   [clojure.tools.logging      :as log]
   [catacumba.core             :as ct]
   [catacumba.http             :as http]
   [catacumba.handlers.parse   :as parse]
   [catacumba.serializers      :as ser]))

;; ===========================================================================
;; handlers

(defn add-state
  "This handler is used to add other components to
  the context of all requests.
  Make sure to use namespaced keywords to avoid clashes."
  [& {:as state}]
  (fn [_]
    (ct/delegate state)))

(defn status-handler [_]
  (http/ok "ok"))

(defn foo-handler [{:keys [::foo]}]
  (http/ok (ser/encode {:foo foo} :json)))

;; ===========================================================================
;; component

(defn api-routes []
  [:prefix "api"
   [:any (parse/body-params)]
   [:prefix "1"
    [:get "foo" foo-handler]]])

(defrecord WebServer [port]
  component/Lifecycle
  (start [component]
    (log/info ";; starting WebServer")
    (let [routes [[:assets "" {:dir "public"
                               :indexes ["index.html"]}]
                  [:any (add-state ::foo "bar")]
                  [:get "status" status-handler]
                  (api-routes)]]
      (assoc component
             :server (ct/run-server (ct/routes routes)
                                    {:port port
                                     :debug true
                                     :marker-file "catacumba.basedir"}))))
  (stop [{:keys [server] :as component}]
    (log/info ";; stopping WebServer")
    (when server
      (.stop server))
    (dissoc component :server)))

;; ===========================================================================
;; constructor

(defn new-web-server [config]
  (component/using
   (map->WebServer (select-keys config [:port]))
   []))
