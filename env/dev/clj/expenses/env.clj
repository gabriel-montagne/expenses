(ns expenses.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [expenses.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[expenses started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[expenses has shut down successfully]=-"))
   :middleware wrap-dev})
