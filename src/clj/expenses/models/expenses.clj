(ns expenses.models.expenses
  (:require
    [ak-dbg.core :refer :all]
    [clojure.tools.logging :as log]
    [expenses.config :refer [env]]
    [hugsql.core :as hugsql]
    [conman.core :as conman])
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

(defn format-javadate-as-text [expense]
  (if (contains? expense :date)
    (assoc-in expense [:date]
              (.format
                (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm")
                (get-in expense [:date])))
    expense))

(defn remove-nils [expense]
  (into {} (remove (fn [[k v]] (nil? v)) expense)))

(defn post-expense [userid expense]
  (try
    {:expense (expense-insert!
                (:database-url env)
                (assoc (format-javadate-as-text expense) :userid userid))}
    (catch PSQLException e
      {:message (.getMessage e)})))

(defn put-expense [userid id expense]
  (try
    {:expense (expense-update!
                (:database-url env)
                (assoc expense :id id :userid userid))}
    (catch PSQLException e
      {:message (.getMessage e)})))

(defn patch-expense [userid id expense]
  (try
    {:expense (expense-update!
                (:database-url env)
                (merge
                  (:expense (get-expense-by-id userid id))
                  (remove-nils (:expense (format-javadate-as-text expense)))
                  ))}
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
