package org.apache.pulsar.ecosystem.io.questdb;

import org.apache.pulsar.io.core.annotations.Connector;
import org.apache.pulsar.io.core.annotations.IOType;
import org.apache.pulsar.io.jdbc.BaseJdbcAutoSchemaSink;
import org.apache.pulsar.io.jdbc.JdbcSinkConfig;
import org.apache.pulsar.io.jdbc.JdbcUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple JDBC sink for QuestDB that writes pulsar messages to a database
 * table.
 */
@Connector(name = "jdbc-questdb", type = IOType.SINK, help = "A simple JDBC sink for QuestDB that writes Pulsar messages to a database table.", configClass = JdbcSinkConfig.class)
public class QuestDbJdbcAutoSchemaSink extends BaseJdbcAutoSchemaSink {
    /**
     * Generates the upsert query statement for the sink.
     *
     * @return The upsert query statement.
     * @throws IllegalStateException if 'key' config is not set.
     */
    @Override
    public String generateUpsertQueryStatement() {
        final List<JdbcUtils.ColumnId> keyColumns = tableDefinition.getKeyColumns();

        if (keyColumns.isEmpty()) {
            throw new IllegalStateException("UPSERT is not supported if 'key' config is not set.");

        }

        final String keys = keyColumns.stream().map(JdbcUtils.ColumnId::getName)
                .collect(Collectors.joining(","));

        return JdbcUtils.buildInsertSql(tableDefinition)
                + " ON CONFLICT(" + keys + ") "
                + "DO UPDATE SET " + JdbcUtils.buildUpdateSqlSetPart(tableDefinition);
    }

    /**
     * Returns a list of column IDs for performing upsert operations.
     *
     * @return a list of column IDs for upsert operations
     */
    @Override
    public List<JdbcUtils.ColumnId> getColumnsForUpsert() {
        final List<JdbcUtils.ColumnId> columns = new ArrayList<>();

        columns.addAll(tableDefinition.getColumns());
        columns.addAll(tableDefinition.getNonKeyColumns());

        return columns;
    }
}