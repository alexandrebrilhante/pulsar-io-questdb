#!/bin/zsh

docker run -p 9000:9000 \
  -p 9009:9009 \
  -p 8812:8812 \
  -p 9003:9003 \
  -v "$(PWD):/var/lib/questdb" \
  questdb/questdb:8.0.1

pulsar-admin schemas upload questdb-sink-topic -f $PWD/pulsar/schema.avsc

pulsar-admin sinks create \
    --archive $PWD/pulsar/connector/pulsar-io-jdbc-postgres-3.2.2.nar \
    --inputs questdb-sink-topic \
    --name questdb-sink \
    --sink-config-file $PWD/pulsar/connector/pulsar-questdb-sink.yaml \
    --parallelism 1

pulsar-client produce questdb-sink-topic -m '{"x": "foo", "y": "bar"}' -s "\n" -n 100