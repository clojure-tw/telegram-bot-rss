(ns telegram-bot-rss.scheduler
  (:require [mount.core :as mount]
            [hara.io.scheduler :as sch]))

(def ^:private scheduler
  (sch/scheduler
   ;; every 15 min dispatch feeds
   {:feeds-dispatcher {:handler (fn [_ _] )
                       :schedule "0 /15 * * * * *"
                       :params nil}}))

(defn start []
  (sch/start! scheduler))

(defn stop []
  (sch/stop! scheduler))

(mount/defstate ^:dynamic *scheduler*
  :start (start)
  :stop  (stop))