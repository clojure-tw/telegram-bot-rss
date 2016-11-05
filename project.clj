(defproject telegram-bot-rss "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [morse "0.2.2"]
                 [mount "0.1.10"]
                 [com.taoensso/timbre "4.7.4"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]
                 [coldnew/config "1.2.0"]
                 [com.layerware/hugsql "0.4.7"]
                 [org.xerial/sqlite-jdbc "3.14.2.1"]
                 [conman "0.6.2"]
                 [conman "0.6.2"]
                 [clj-http "3.3.0"]
                 [org.clojure/core.async "0.2.395"]
                 [im.chit/hara.io.scheduler "2.4.7"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]]

  :jvm-opts ["-Dclojure.compiler.direct-linking=true"]

  :profiles {:uberjar {:aot :all}}
  :main ^:skip-aot telegram-bot-rss.core)
