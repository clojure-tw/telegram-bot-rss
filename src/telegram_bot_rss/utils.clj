(ns telegram-bot-rss.utils
  (:require [clj-http.client :as http]))

(defn valid-url?
  "Check if url is valid link. We hate 404 error."
  [url]
  (try (= 200
          (-> (http/get url  {:insecure? true})
              :status))
       (catch Exception e
         false)))