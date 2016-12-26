(ns {{namespace}}.views
  (:require
   [reagent.core  :as reagent]
   [re-frame.core :as re-frame]))

(def initial-focus-wrapper
  "Use this to wrap the element you want to receive focus."
  (with-meta identity
    {:component-did-mount #(.focus (reagent/dom-node %))}))

(defn value-setter [container value-f]
  (fn [e]
    (reset! container (value-f (.-target e)))))

(defn text-value-setter [container]
  (value-setter container #(.-value %)))

(defn checkbox-value-setter [container]
  (value-setter container #(.-checked %)))

(defn signin-panel []
  (let [email        (reagent/atom "")
        password     (reagent/atom "")
        remember-me? (reagent/atom true)]
    (fn []
      [:div.card-container.card
       [:form.form-signin
        [:h2.form-signin-heading "please sign in"]
        [:label.sr-only {:for "inputEmail"} "email-address"]
        [initial-focus-wrapper
         [:input.form-control {:type        "email"
                               :id          "inputEmail"
                               :placeholder "email address"
                               :required    true
                               :value       @email
                               :on-change   (text-value-setter email)}]]
        [:label.sr-only {:for "inputPassword"} "password"]
        [:input.form-control {:type        "password"
                              :id          "inputPassword"
                              :placeholder "password"
                              :required    true
                              :value       @password
                              :on-change   (text-value-setter password)}]
        [:div.checkbox
         [:label
          [:input {:type           "checkbox"
                   :defaultChecked @remember-me?
                   :on-change      (checkbox-value-setter remember-me?)}]
          " remember me "]]
        [:button.btn.btn-lg.btn-primary.btn-block
         {:type     "button"
          :on-click #(re-frame/dispatch [:login
                                         {:username @email
                                          :password @password}
                                         @remember-me?])}
         [:i.fa.fa-sign-in]]]])))

(defn main-panel []
  (let [signed-in? (re-frame/subscribe [:signed-in?])]
    (fn []
      [:div.container
       (if @signed-in?
         [:p "main view"]
         [signin-panel])])))
