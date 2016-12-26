(ns {{namespace}}.db
  (:require
   [cognitect.transit :as t]
   [re-frame.core     :as re-frame]))

(def default-value
  {})

;; we use transit here because it has support for more data types
;; and still uses fast native json parser
(defn clj->json [x]
  (t/write (t/writer :json) x))

(defn json->clj [x]
  (t/read (t/reader :json) x))

(def ls-key "{{namespace}}.state")

(defn state->local-store [state]
  (.setItem js/localStorage ls-key (clj->json state)))

(re-frame/reg-cofx
 :local-store-state
 (fn [cofx _]
   (assoc cofx :local-store-state
          (some->> (.getItem js/localStorage ls-key)
                   json->clj))))
