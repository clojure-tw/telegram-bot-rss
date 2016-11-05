-- src/telegram_bot_rss/db/sql/subscribers.sql

-- :name create-table :!
-- :result :raw
-- :doc Create subscribers table
CREATE TABLE IF NOT EXISTS subscribers (
       feed         VARCHAR,
       subscriber   VARCHAR
)

-- :name subscribe :! :n
-- :doc Add feed to subscriber
INSERT INTO subscribers (feed, subscriber)
VALUES (:feed, :subscriber)

-- :name unsubscribe :! :n
-- :doc Delete feed from subscriber
DELETE FROM subscribers
WHERE feed = :feed AND subscriber = :subscriber

-- :name drop-table :!
-- :doc Drop subscribers table if exists
DROP TABLE if exists subscribers

-- :name get-subscribe-list :? :*
-- :doc Retrive subscriber's feed lists
SELECT feed FROM subscribers
WHERE subscriber = :subscriber
