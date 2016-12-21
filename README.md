# clj-kinesis-client

[![Build Status](https://travis-ci.org/adtile/clj-kinesis-client.svg?branch=master)](https://travis-ci.org/adtile/clj-kinesis-client)
[![Clojars Project](https://img.shields.io/clojars/v/clj-kinesis-client.svg)](https://clojars.org/clj-kinesis-client)

A minimalistic Clojure wrapper for AWS Kinesis client.

## Usage

```clojure
(use 'clj-kinesis-client.core)

(let [client (create-client)]
  (put-records client "my-stream" ["event1" "event2"]))
```

## Development

```sh
$ npm install -g kinesalite
$ kinesalite
# Disable CBOR https://github.com/mhart/kinesalite#cbor-protocol-issues-with-the-java-sdk
$ AWS_CBOR_DISABLE=1 lein test
```

## License

Copyright © 2016 Adtile Technologies Inc.

Distributed under the Eclipse Public License version 1.0.
