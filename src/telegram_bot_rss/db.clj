(ns telegram-bot-rss.db
  (:require [conman.core :as conman]
            [coldnew.config :refer [conf]]
            [mount.core :as mount]))

(def ^:private db {:classname "org.sqlite.JDBC"
                   :subprotocol "sqlite"
                   :subname (conf :database)})

(def ^:private db-spec {:dataSourceClassName (:classname db)
                        :jdbc-url (str "jdbc:sqlite:" (:subname db))})

(mount/defstate ^:dynamic *db*
  :start (conman/connect! db-spec)
  :stop  (conman/disconnect! *db*))