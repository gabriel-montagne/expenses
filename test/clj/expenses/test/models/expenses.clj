(ns expenses.test.models.expenses
  (:require
    [ak-dbg.core :refer :all]
    [expenses.models.expenses :as db]
    [luminus-migrations.core :as migrations]
    [clojure.test :refer :all]
    [clojure.java.jdbc :as jdbc]
    [expenses.config :refer [env]]
    [expenses.handler :refer :all]
    [expenses.test.base-tests :as base]
    [ring.mock.request :refer :all]
    [mount.core :as mount]
    [clojure.data.json :as json]))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'expenses.config/env
                 #'expenses.handler/app)
    (f)))

(def response-expense (atom {:expense {}}))

(defonce test-expense
         {:expense
          {
           :userid      "1"
           :date        "2018-02-20 12:00"
           :amount      10
           :description "some description"
           :comment     "test"
           }
          })

(defonce patch-expense
         {:expense
          {
           :userid      "1"
           :date        "2018-02-20 12:00"
           :amount      20
           :description "some description updated"
           :comment     "test"
           }
          })

(deftest test-expenses
  (testing "post expense"
    (let [response (app (base/test-post-request "/api/expenses/1" test-expense))]
      (swap! response-expense assoc :expense (:expense (json/read-json (slurp (:body response)) true)))
      (is (= 202 (:status response)))))
  (testing "get all expenses"
    (let [response (app (request :get "/api/expenses"))]
      (is (= 200 (:status response)))))
  (testing "get all expenses for user"
    (let [response (app (request :get (str "/api/expenses/"
                                           (get-in (deref response-expense) [:expense :userid]))))]
      (is (= 200 (:status response)))))
  (testing "get expense for expense id"
    (let [response (app (request :get (str "/api/expenses/"
                                           (get-in (deref response-expense) [:expense :userid])
                                           "/"
                                           (get-in (deref response-expense) [:expense :id]))))]
      (is (= 200 (:status response)))))
  (testing "put expense"
    (let [response (app (base/test-put-request (str "/api/expenses/"
                                                      (get-in (deref response-expense) [:expense :userid])
                                                      "/"
                                                      (get-in (deref response-expense) [:expense :id])) patch-expense))]
      (is (= 202 (:status response)))))
  (testing "delete expense"
    (let [response (app (request :delete (str "/api/expenses/"
                                              (get-in (deref response-expense) [:expense :userid])
                                              "/"
                                              (get-in (deref response-expense) [:expense :id]))))]
      (is (= 202 (:status response))))))

