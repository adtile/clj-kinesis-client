(ns clj-kinesis-client.core
  (:import [com.amazonaws.services.kinesis AmazonKinesisClient]
           [com.amazonaws.services.kinesis.model PutRecordsRequest PutRecordsRequestEntry]
           [com.amazonaws ClientConfiguration]
           [com.amazonaws.regions Regions]
           [java.util UUID]
           [java.nio ByteBuffer]))

(defn create-client [& {:keys [max-connections max-error-retry endpoint region tcp-keep-alive]
                        :or {max-connections 50 max-error-retry 1 tcp-keep-alive false}}]
  (let [configuration (-> (ClientConfiguration.)
                          (.withMaxErrorRetry max-error-retry)
                          (.withMaxConnections max-connections)
                          (.withTcpKeepAlive tcp-keep-alive))
        client (AmazonKinesisClient. configuration)]
    (when endpoint
      (.setEndpoint client endpoint))
    (if region
      (.withRegion client (Regions/fromName region))
      client)))

(defn- uuid [] (str (UUID/randomUUID)))
(defn- str->byte-buffer [str]
  (ByteBuffer/wrap (.getBytes str "UTF-8")))

(defn- put-record-response->map [response]
  {:sequence-number (.getSequenceNumber response)
   :shard-id (.getShardId response)})

(defn- put-records-response->map [response]
  {:failed-record-count (.getFailedRecordCount response)
   :records (map put-record-response->map (.getRecords response))})

(defn put-record [client stream-name event]
  (let [response (.putRecord client stream-name (str->byte-buffer event) (uuid))]
    (put-record-response->map response)))

(defn put-records [client stream-name events]
  (let [str->put-entry (fn [entry] (-> (PutRecordsRequestEntry.)
                                       (.withData (str->byte-buffer entry))
                                       (.withPartitionKey (uuid))))
        request (-> (PutRecordsRequest.)
                    (.withStreamName stream-name)
                    (.withRecords (map str->put-entry events)))
        response (.putRecords client request)]
    (put-records-response->map response)))

