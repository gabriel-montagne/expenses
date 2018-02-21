(ns expenses.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [expenses.handler :refer :all]
            [mount.core :as mount]
            [clojure.data.json :as json]))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'expenses.config/env
                 #'expenses.handler/app)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/api/expenses"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response))))))