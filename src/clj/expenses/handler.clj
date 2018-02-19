(ns expenses.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [expenses.routes.services :refer [service-routes]]
            [expenses.routes.oauth :refer [oauth-routes]]
            [expenses.routes.api.expenses :refer [expenses-routes]]
            [compojure.route :as route]
            [expenses.env :refer [defaults]]
            [mount.core :as mount]
            [expenses.middleware :as middleware]))

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(mount/defstate app
  :start
  (middleware/wrap-base
    (routes
      oauth-routes
      expenses-routes
      service-routes
      (route/not-found
        "page not found"))))
