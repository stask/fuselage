(ns {{namespace}}.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :signed-in?
 (fn [db _]
   (boolean (:auth-token db))))
