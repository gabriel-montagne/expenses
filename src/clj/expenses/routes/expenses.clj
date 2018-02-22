(ns expenses.routes.expenses
  (:require
    [ak-dbg.core :refer :all]
    [ring.util.http-response :refer :all]
    [compojure.api.sweet :refer :all]
    [schema.core :as s]
    [compojure.api.meta :refer [restructure-param]]
    [buddy.auth.accessrules :refer [restrict]]
    [buddy.auth :refer [authenticated?]]

    [expenses.models.expenses :as md-expenses]))

(s/defschema Expense {
                      :date s/Inst
                      :description s/Str
                      :amount s/Num
                      :comment s/Str})

(s/defschema Patch-expense {
                            (s/optional-key :date) (describe s/Inst "date")
                            (s/optional-key :description) (describe String "description")
                            (s/optional-key :comments) (describe String "comments")
                            (s/optional-key :amount) (describe Integer "integer")})

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



  (context "/api/expenses" []
    :tags ["expenses api"]
    (GET "/" []
      :summary "returns an array with all the expenses"
      (ok (md-expenses/get-all-expenses)))
    (GET "/:userid" []
      :path-params [userid :- String]
      :summary "returns an array with all the expenses from a specific user"
      (ok (md-expenses/get-expenses-by-userid userid)))
    (GET "/:userid/:id" []
      :path-params [userid :- String, id :- Long]
      :summary "returns a specific expense"
      (ok (md-expenses/get-expense-by-id userid id)))
    (POST "/:userid" []
      :path-params [userid :- String]
      :body-params [expense :- Expense]
      :summary "insert an expense"
      (accepted (md-expenses/post-expense userid expense)))
    (PATCH "/:userid/:id" []
      :path-params [userid :- String, id :- Long]
      :body [{:keys [date amount description comment] :or {}}
             (s/maybe {
                       (s/optional-key :date) (describe s/Inst "date")
                       (s/optional-key :amount) (describe s/Num "date")
                       (s/optional-key :description) (describe s/Str "date")
                       (s/optional-key :comment) (describe s/Str "date")})]
      :summary "patch an expense"
      (accepted (md-expenses/patch-expense userid id {:date date
                                                      :amount amount
                                                      :description description
                                                      :comment comment})))
    (PUT "/:userid/:id" []
      :path-params [userid :- String, id :- Long]
      :body-params [expense :- Expense]
      :summary "update an expense"
      (accepted (md-expenses/put-expense userid id expense)))
    (DELETE "/:userid/:id" []
      :path-params [userid :- String, id :- Long]
      :summary "delete an expense"
      (accepted (md-expenses/delete-expense userid id)))))
