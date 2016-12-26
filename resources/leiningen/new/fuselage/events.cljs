(ns {{namespace}}.events
  (:require
   [re-frame.core    :as re-frame]
   [{{namespace}}.db     :refer [default-value state->local-store]]
   [{{namespace}}.config :as config]))

;; this interceptor stores the state in local storage
;; we attach it to each event handler which could update the state
(def ->local-store (re-frame/after state->local-store))

;; the chain of interceptors we use for all handlers that manipulate state
(def state-interceptors [->local-store
                         (when config/debug? re-frame/debug)])

;; ===========================================================================
;; event handlers

(re-frame/reg-event-fx
 :initialize-db
 [(re-frame/inject-cofx :local-store-state)]
 (fn [{:keys [local-store-state]} _]
   {:db (merge default-value local-store-state)}))
