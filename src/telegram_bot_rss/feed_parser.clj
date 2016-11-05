(ns telegram-bot-rss.feed-parser
  (:require [feedparser-clj.core :as feedparser]
            [clj-http.client :as http]
            [taoensso.timbre :as timbre]))


(defn parse-feed [url]
  (try (let [feed (feedparser/parse-feed url)]
         (map #(select-keys % [:title :link]) (:entries feed)))
       (catch Exception e
         (throw (ex-info (.getMessage e) {:url url})))))

;; (parse-feed "http://coldnew.github.io/rss.xml")