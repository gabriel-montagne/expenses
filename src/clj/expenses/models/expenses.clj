(ns expenses.models.expenses
  (:require
    [ak-dbg.core :refer :all]
    [clojure.tools.logging :as log]
    [expenses.config :refer [env]]
    [hugsql.core :as hugsql])
  (:import (org.postgresql.util PSQLException)))

(hugsql/def-db-fns "expenses/sql/expenses.sql")

(defn get-all-expenses []
  {:expenses (expenses-read (:database-url env))})

(defn get-expenses-by-userid [userid]
  {:expenses (by-userid-expenses-read
               (:database-url env)
               {:userid userid})})

(defn get-expense-by-id [userid id]
  {:expense (first
              (by-userid-expenses-read
                (:database-url env)
                      {:userid userid
                       :id id}))})

(defn post-expense [userid expense]
  (try
    {:expense (expense-insert!
                (:database-url env)
                {:userid userid
                 :date (:date expense)
                 :description (:description expense)
                 :amount (:amount expense)
                 :comment (:comment expense)})}
    (catch PSQLException e
      {:message (.getMessage e)})))

(defn patch-expense [userid id expense]
  (try
    {:expense (expense-update!
                (:database-url env)
                (merge
                  (:expense (get-expense-by-id userid id))
                  expense))}
    (catch PSQLException e
      {:message (.getMessage e)})))

(defn delete-expense [userid id]
  (try
    {:expense (expense-remove!
                (:database-url env)
                {:userid userid
                 :id id})}
    (catch PSQLException e
      {:message (.getMessage e)})))