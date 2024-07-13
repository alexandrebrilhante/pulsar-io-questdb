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
pulsar-admin sinks create \
  --tenant public \
  --namespace default \
  --name questdb-sink \
  --sink-type jdbc-postgres \
  --sinking-topic-name persistent://public/default/your-topic \
  --sink-config-file pulsar-questdb-sink.yaml
```

#### Schema Evolution

If your data schema changes, you may need to update the QuestDB table schema manually or recreate the Sink with `autoCreateTable` set to `true`.
