(ns telegram-bot-rss.telegram
  (:require [mount.core :as mount]
            [morse.polling :as polling]
            [morse.handlers :as handler]
            [morse.api :as api]
            [taoensso.timbre :as timbre]
            [coldnew.config :refer [conf]]
            [clojure.string :as str]
            [telegram-bot-rss.utils :as utils]
            [telegram-bot-rss.feed-parser :as parser]
            [telegram-bot-rss.db.feeds :as feeds]
            [telegram-bot-rss.db.subscribers :as subscribers]))

;; 286993463:AAFEvWGdh_B0e0mnw4XQtWwGx86sczPJwe0
(def +token+ (conf :token))

(defn send-message
  ([chat-id message] (send-message chat-id message nil))
  ([chat-id message params]
   (api/send-text +token+ chat-id (merge {:parse_mode "Markdown"} params) message)))
;; message:  {:message_id 8, :from {:id 81660412, :first_name "Yen-Chin", :last_name "Lee", :username "coldnew"}, :chat {:id 81660412, :first_name "Yen-Chin", :last_name "Lee", :username "coldnew", :type "private"}, :date 1478276319, :text "/list", :entities [{:type "bot_command", :offset 0, :length 5}]}

;; (subscribers/get-subscribe-list {:subscriber 81660412})
;; https://github.com/iovxw/tg-rss-bot/blob/master/src/tg_rss_bot/core.clj#L157

(defn cmd-subscribe
  [message]
  (let [chat-id  (-> message :chat :id)
        feed-url (-> message :text (str/replace #"/sub" "") str/trim)]
    (cond
      ;; no argument pass
      (empty? feed-url)
      (send-message chat-id
                    "Please, provide a feed URL to which you want to subscribe. For example, /subscribe https://coldnew.github.io/rss.xml")
      ;; invalid url
      (false? (utils/valid-url? feed-url))
      (send-message chat-id
                    "We could not subscribe you to this feed... sorry!")
      ;; retrive rss feed info
      :else
      (let [feed-info (try (parser/parse-feed feed-url)
                           (catch Exception e
                             (send-message chat-id
                                           (format "Subscribe failed =口= : %s" (.getMessage e)))))]
        (when feed-info
          ;; save to db
          (feeds/add {:url feed-url :title (:title feed-info) :hash nil :error_count 0})
          (subscribers/subscribe {:feed feed-url :subscriber chat-id})
          (send-message chat-id
                        "Done! Next time the feed updates, you'll be the first to know!"))))))

(defn cmd-unsubscribe
  [message]
  ;; Please, provide a feed URL from which you want to unsubscribe. For example, /unsubscribe https://coldnew.github.io/rss.xml
  ;; We could not unsubscribe you to this feed... sorry!
  ;; Done! You will not hear from this feed again
  (let [chat-id  (-> message :chat :id)
        feed-url (-> message :text (str/replace #"/sub" "") str/trim)]
    (timbre/info "---> message: " message)
    (timbre/info "---> url: " feed-url)
    )
  )

(defn cmd-list
  [message]
  (let [chat-id  (-> message :chat :id)
        query (subscribers/get-subscribe-list {:subscriber chat-id})]
    (if (empty? query)
      (send-message chat-id
                    "There are no subscriptions. What\'s your favorite site?"))

    (send-message chat-id
                  (str (subscribers/get-subscribe-list {:subscriber chat-id}))
                  {:disable_web_page_preview true})
    )
  ;; We could not list subscriptions... sorry!
  ;;
  ;;
  (timbre/info "---> message: " message))

(defn cmd-help
  [chat-id]
  (send-message chat-id
                (str "Commands list:\n"
                     "/sub   <url> - Subscribe feed\n"
                     "/unsub <url> - Unsubscribe feed\n"
                     "/list        - List all feeds you subscribed\n"
                     "/help        - This message\n"
                     "Source Code: "
                     "\thttps://github.com/clojure-tw/telegram-bot-rss\n"
                     "Version: 0.1.0")
                {:disable_web_page_preview true}))

(handler/defhandler bot-api
  (handler/command "list"               ; list feeds
                   message
                   (cmd-list message))
  (handler/command "sub"          ; subscribe feed
                   message
                   (cmd-subscribe message))
  (handler/command "unsub"        ; unscribe feed
                   message
                   (cmd-unsubscribe message))
  (handler/command "help"               ; help info
                   {{:keys [id]} :chat}
                   (cmd-help id)))

(defn start []
  (polling/start +token+ bot-api))

(defn stop [conn]
  (polling/stop conn))

(mount/defstate ^:dynamic *telegram*
  :start (start)
  :stop  (stop *telegram*))
