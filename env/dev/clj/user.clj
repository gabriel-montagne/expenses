(ns user
  (:require [luminus-migrations.core :as migrations]
            [expenses.config :refer [env]]
            [mount.core :as mount]
            [expenses.core :refer [start-app]]))

(defn start []
  (mount/start-without #'expenses.core/repl-server))

(defn stop []
  (mount/stop-except #'expenses.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


