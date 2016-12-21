# clj-kinesis-client

[![Build Status](https://travis-ci.org/adtile/clj-kinesis-client.svg?branch=master)](https://travis-ci.org/adtile/clj-kinesis-client)

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
$ lein test
```

## License

Copyright © 2015 Adtile Technologies Inc.

Distributed under the Eclipse Public License version 1.0.
