-- src/telegram_bot_rss/db/sql/feed.sql

-- :name create-table :!
-- :result :raw
-- :doc Create feeds table
CREATE TABLE IF NOT EXISTS feeds (
       url         VARCHAR,
       title       VARCHAR,
       hash        VARCHAR,
       error_count INTEGER
)

-- :name add :! :n
INSERT OR REPLACE INTO feeds (url, title, hash, error_count)
VALUES (:url, :title, :hash, :error_count)

-- :name delete :! :n
DELETE FROM feeds
WHERE url = :url AND hash = :hash

-- :name drop-table :!
-- :doc Drop feeds table if exists
DROP TABLE if exists feeds

-- :name get-info :? :*
-- :doc Get feed's info according to url
SELECT * FROM feeds
WHERE url = :url