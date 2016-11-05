(ns telegram-bot-rss.db.feeds
  (:require [telegram-bot-rss.db :refer [*db*]]
            [conman.core :as conman]))

(conman/bind-connection *db* "sql/feeds.sql")

(comment
  ;; (get-info)
  )