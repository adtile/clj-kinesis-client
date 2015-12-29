(ns clj-kinesis-client.core-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.clojure-test :refer :all]
            [clj-kinesis-client.core :refer :all]))

(defonce client (create-client :endpoint "http://localhost:4567"))

(defn- delete-stream-if-found [client stream-name]
  (when (some #{stream-name} (-> client .listStreams .getStreamNames))
    (.deleteStream client stream-name)
    (Thread/sleep 500))) ;Wait for stream deletion

(defn- create-stream [client stream-name]
  (.createStream client stream-name (int 1))
  (Thread/sleep 500)) ;Wait for stream creation

(deftest put-records-batch
  (delete-stream-if-found client "unit-test")
  (create-stream client "unit-test")
  (testing "returns failed count and succeeded records"
    (is (equal? (put-records client "unit-test" ["lol" "bal"])
                {:failed-record-count 0
                 :records [{:sequence-number string? :shard-id "shardId-000000000000"}
                           {:sequence-number string? :shard-id "shardId-000000000000"}]}))))
