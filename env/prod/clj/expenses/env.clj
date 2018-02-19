(ns expenses.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[expenses started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[expenses has shut down successfully]=-"))
   :middleware identity})
