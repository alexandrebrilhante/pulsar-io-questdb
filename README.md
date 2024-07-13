# pulsar-io-questdb

This is a quick guide on how to use Apache Pulsar's PostgreSQL sink connector with QuestDB. The JDBC sink connector allow pulling messages from Pulsar topics and persist the messages to QuestDB.

## Usage

### Configuration

Before using the JDBC QuestDB sink connector, you need to create the `pulsar-questdb-sink.yaml` configuration file:

```yaml
configs:
  userName: "admin"
  password: "quest"
  jdbcUrl: "jdbc:postgresql://questdb-host:8812/qdb"
  tableName: "your_table_name"
  autoCreateTable: "true"
  batchSize: 200
  maxRetries: 5
  retryBackoffMs: 500
```

### Running

One the configuration file has been created, you can try running the sink connection:

```bash
pulsar-admin schemas upload questdb-sink-topic -f $PWD/pulsar/schema.avsc

pulsar-admin sinks create \
    --archive $PWD/pulsar/connector/pulsar-io-jdbc-postgres-3.2.2.nar \
    --inputs questdb-sink-topic \
    --name questdb-sink \
    --sink-config-file $PWD/pulsar/connector/pulsar-questdb-sink.yaml \
    --parallelism 1
```

### Sending Messages
```bash
pulsar-client produce questdb-sink-topic -m '{"x": "foo", "y": "bar"}' -s "\n" -n 100
```

#### Schema Evolution

If your data schema changes, you may need to update the QuestDB table schema manually or recreate the Sink with `autoCreateTable` set to `true`.
