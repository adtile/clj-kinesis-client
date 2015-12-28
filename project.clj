(defproject clj-kinesis-client "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.amazonaws/amazon-kinesis-client "1.6.1" :exclusions [com.amazonaws/aws-java-sdk-dynamodb]]])
