(ns telegram-bot-rss.db.subscribers
  (:require [telegram-bot-rss.db :refer [*db*]]
            [conman.core :as conman]))

(conman/bind-connection *db* "sql/subscribers.sql")

(comment
  ;; ({:feed "https://coldnew.github.io"} {:feed "http://google.com"})
  (get-subscribe-list {:subscriber 81660412})
  (empty? (get-subscribe-list {:subscriber "1113"}))
  (subscribe {:feed "http://google.com"
              :subscriber 81660412})
  (subscribe {:feed "123"
              :subscriber "11133"})
  (subscribe {:feed "456"
              :subscriber "11133"})
  (unsubscribe {:feed "123"
                :subscriber "11133"})


  (str "Subscribes:\n"
   (reduce #(format "%s[%s](%s)\n" %1 (:title %2) (:feed %2))
           ""
           '({:feed "https://coldnew.github.io"
              :title "王王"}
             {:feed "http://google.com"
              :title "狗狗"})))
  )