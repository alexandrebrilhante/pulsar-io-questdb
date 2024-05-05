# pulsar-io-questdb

The JDBC sink connectors allow pulling messages from Pulsar topics and persist the messages to QuestDB.

This is a *work in progress* and not ready for production.

## Usage

### Configuration

Before using the JDBC QuestDB sink connector, you need to create the `pulsar-questdb-jdbc-sink.yaml` configuration file:

```yaml
tenant: "public"
namespace: "default"
name: "jdbc-questdb-sink"
inputs: [ "persistent://public/default/jdbc-questdb-topic" ]
sinkType: "jdbc-questdb"
configs:
    userName: "admin"
    password: "quest"
    jdbcUrl: "jdbc:postgresql://127.0.0.1:8812/qdb?useSSL=false/pulsar_questdb_jdbc_sink"
    tableName: "pulsar_questdb_jdbc_sink"
```

### Running

One the configuration file has been created, you can try running the sink connection:

```bash
pulsar-admin sinks create \
    --archive $PWD/connectors/pulsar-io-jdbc-postgres-3.2.2.nar \
    --inputs jdbc-questdb-topic \
    --name jdbc-questdb-sink \
    --sink-config-file $PWD/connectors/pulsar-questdb-jdbc-sink.yaml \
    --parallelism 1
```