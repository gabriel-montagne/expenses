(ns expenses.routes.api.expenses
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))

(defapi expenses-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Expenses API"
                           :description "Expenses Services"}}}}

  (context "/expenses" []
    (GET "/" []
      :summary "returns an array with all the expenses"
      (ok {:expenses ["e 1" "e 2" "e 3" "e 4"]}))
    (GET "/:userid" []
      :summary "returns an array with all the expenses from a specific user"
      (ok {:expenses ["u e 1" "u e 2" "u e r"]}))
    (GET "/:userid/:expenseid" []
      :summary "returns a specific expense"
      (ok {:expense "some e"}))))
