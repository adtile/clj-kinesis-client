(ns clj-kinesis-client.core
  (:import [com.amazonaws.services.kinesis AmazonKinesisClient]
           [com.amazonaws.services.kinesis.model PutRecordsRequest PutRecordsRequestEntry]
           [com.amazonaws ClientConfiguration]
           [com.amazonaws.regions Regions]
           [java.util UUID]
           [java.nio ByteBuffer]))

(defn create-client [& {:keys [max-connections max-error-retry endpoint region]
                        :or {max-connections 50 max-error-retry 1}}]
  (let [configuration (-> (ClientConfiguration.)
                          (.withMaxErrorRetry max-error-retry)
                          (.withMaxConnections max-connections))
        client (AmazonKinesisClient. configuration)]
    (when endpoint
      (.setEndpoint client endpoint))
    (if region
      (.withRegion client (Regions/fromName region))
      client)))

(defn- uuid [] (str (UUID/randomUUID)))

(defn- put-records-response->map [response]
  {:failed-record-count (.getFailedRecordCount response)
   :records (map (fn [r] {:sequence-number (.getSequenceNumber r)
                          :shard-id (.getShardId r)})
                 (.getRecords response))})

(defn put-records [client stream-name events]
  (let [str->put-entry #(-> (PutRecordsRequestEntry.)
                            (.withData (ByteBuffer/wrap (.getBytes % "UTF-8")))
                            (.withPartitionKey (uuid)))
        request (-> (PutRecordsRequest.)
                    (.withStreamName stream-name)
                    (.withRecords (map str->put-entry events)))
        response (.putRecords client request)]
    (put-records-response->map response)))
