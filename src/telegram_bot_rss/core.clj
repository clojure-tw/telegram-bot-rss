(ns telegram-bot-rss.core
  (:require [telegram-bot-rss.telegram]
            [telegram-bot-rss.scheduler]
            [telegram-bot-rss.db]
            [mount.core :as mount]
            [taoensso.timbre :as timbre]
            [coldnew.config :as conf])
  (:gen-class))

;; Make coldnew.config evaluate the config after load it
(mount/defstate ^:dynamic *env*
  :start (conf/enable-eval!))

(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (timbre/info component "stopped"))
  (shutdown-agents))

(defn start-app []
  (doseq [component (:started (mount/start))]
    (timbre/info component "started"))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main
  [& args]
  ;; FIXME: check user's env or config.edn settings
  (start-app)
  (while true ))