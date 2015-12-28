(ns clj-kinesis-client.core
  (:import [com.amazonaws.services.kinesis AmazonKinesisClient]
           [com.amazonaws.services.kinesis.model PutRecordsRequest PutRecordsRequestEntry]
           [com.amazonaws ClientConfiguration]
           [java.util UUID]
           [java.nio ByteBuffer]))

(defn create-client [& {:keys [max-error-retry endpoint] :or {max-error-retry 1}}]
  (let [configuration (.withMaxErrorRetry (ClientConfiguration.) max-error-retry)
        client (AmazonKinesisClient. configuration)]
    (when endpoint
      (.setEndpoint client endpoint))
    client))

(defn- uuid [] (str (UUID/randomUUID)))

(defn put-records [client stream-name events]
  (let [str->put-entry #(-> (PutRecordsRequestEntry.)
                            (.withData (ByteBuffer/wrap (.getBytes % "UTF-8")))
                            (.withPartitionKey (uuid)))
        request (-> (PutRecordsRequest.)
                    (.withStreamName stream-name)
                    (.withRecords (map str->put-entry events)))
        response (.putRecords client request)]
    {:failed-record-count (.getFailedRecordCount response)}))
