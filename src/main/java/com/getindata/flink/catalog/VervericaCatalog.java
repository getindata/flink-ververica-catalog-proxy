package com.getindata.flink.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getindata.flink.catalog.httpclient.CatalogHttpClient;
import com.getindata.flink.catalog.httpclient.CatalogHttpClientResponse;
import com.getindata.flink.catalog.model.VervericaDatabase;
import com.getindata.flink.catalog.model.VervericaFunction;
import com.getindata.ververica.client.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Schema.Builder;
import org.apache.flink.table.catalog.*;
import org.apache.flink.table.catalog.exceptions.CatalogException;
import org.apache.flink.table.catalog.exceptions.DatabaseNotExistException;
import org.apache.flink.table.catalog.exceptions.FunctionNotExistException;
import org.apache.flink.table.catalog.exceptions.TableNotExistException;
import org.apache.flink.table.catalog.stats.CatalogColumnStatistics;
import org.apache.flink.table.catalog.stats.CatalogTableStatistics;
import org.apache.flink.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.flink.util.Preconditions.checkArgument;

/**
 * Proxy to the internal Ververica Catalog. This class uses API exposed by Ververica to get
 * tables, views, functions definitions.
 */
@Slf4j
public class VervericaCatalog extends UnsupportedOperationAbstractCatalog {

    public static final String DEFAULT_DB = "default";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String ververicaUrl;
    private final String ververicaNamespace;
    private final String ververicaCatalog;

    private final CatalogHttpClient client;

    /**
     * @param name               Catalog name in flink
     * @param ververicaUrl       Ververica URL
     * @param ververicaNamespace Ververica Namespace
     * @param ververicaCatalog   Name of the catalog in Ververica
     * @param client             HTTP Client for sending requests
     */
    public VervericaCatalog(String name, String ververicaUrl, String ververicaNamespace, String ververicaCatalog, CatalogHttpClient client) {
        super(name, DEFAULT_DB);
        checkArgument(!StringUtils.isNullOrWhitespaceOnly(ververicaUrl),
                "The ververica URL must be set when initializing Ververica Catalog.");
        checkArgument(!StringUtils.isNullOrWhitespaceOnly(ververicaNamespace),
                "The ververica namespace must be set when initializing Ververica Catalog.");
        checkArgument(!StringUtils.isNullOrWhitespaceOnly(ververicaCatalog),
                "The ververica catalog must be set when initializing Ververica Catalog.");
        this.ververicaUrl = ververicaUrl;
        this.ververicaNamespace = ververicaNamespace;
        this.ververicaCatalog = ververicaCatalog;
        this.client = client;
    }

    @Override
    public void open() throws CatalogException {
    }

    @Override
    public void close() throws CatalogException {
    }

    @Override
    public List<String> listDatabases() throws CatalogException {
        return sendGetRequest("listDatabases", Collections.emptyMap(), ListDatabasesResponse.class)
                .getDatabases()
                .stream()
                .map(Database::getName)
                .collect(Collectors.toList());
    }

    @Override
    public CatalogDatabase getDatabase(String databaseName) throws DatabaseNotExistException, CatalogException {
        Database database = sendGetRequest("getDatabase", Map.of("database", databaseName),
                GetDatabaseResponse.class, databaseNotExistsMapper(databaseName))
                .getDatabase();
        return new VervericaDatabase(database);
    }

    @Override
    public boolean databaseExists(String databaseName) throws CatalogException {
        return listDatabases().contains(databaseName);
    }

    @Override
    public List<String> listTables(String databaseName) throws DatabaseNotExistException, CatalogException {
        List<String> tables =
                sendGetRequest("listTables", Map.of("database", databaseName),
                        ListTablesResponse.class, databaseNotExistsMapper(databaseName))
                        .getTables()
                        .stream()
                        .map(VvpTable::getName)
                        .collect(Collectors.toList());
        tables.addAll(listViews(databaseName));
        return tables;
    }

    @Override
    public List<String> listViews(String databaseName) throws DatabaseNotExistException, CatalogException {
        return sendGetRequest("listViews", Map.of("database", databaseName),
                ListViewsResponse.class, databaseNotExistsMapper(databaseName))
                .getViews()
                .stream()
                .map(VvpView::getName)
                .collect(Collectors.toList());
    }

    @Override
    public CatalogBaseTable getTable(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        ExceptionMessageMapper<TableNotExistException> tableNotExistsMapper = (String desc) -> desc.indexOf("Table") == 0
                ? Optional.of(new TableNotExistException(desc, tablePath))
                : Optional.empty();

        if (isView(tablePath)) {
            VvpView view = sendGetRequest("getView", Map.of(
                    "database", tablePath.getDatabaseName(),
                    "view", tablePath.getObjectName()),
                    GetViewResponse.class, tableNotExistsMapper).getView();
            return CatalogView.of(parseVvpSchema(view.getSchema()), view.getComment(), view.getQuery(), view.getExpandedQuery(), view.getProperties());
        } else {
            VvpTable table = sendGetRequest("getTable", Map.of(
                    "database", tablePath.getDatabaseName(),
                    "table", tablePath.getObjectName()),
                    GetTableResponse.class, tableNotExistsMapper).getTable();
            return CatalogTable.of(parseVvpSchema(table.getSchema()), table.getComment(), Collections.emptyList(), table.getProperties());
        }
    }

    @Override
    public boolean tableExists(ObjectPath tablePath) throws CatalogException {
        try {
            return listTables(tablePath.getDatabaseName()).contains(tablePath.getObjectName());
        } catch (DatabaseNotExistException e) {
            return false;
        }
    }

    @Override
    public List<String> listFunctions(String databaseName)
            throws DatabaseNotExistException, CatalogException {
        return sendGetRequest("listFunctions", Map.of("database", databaseName),
                ListFunctionsResponse.class, databaseNotExistsMapper(databaseName))
                .getFunctions()
                .stream()
                .map(Function::getName)
                .collect(Collectors.toList());
    }

    @Override
    public CatalogFunction getFunction(ObjectPath functionPath) throws FunctionNotExistException, CatalogException {
        ExceptionMessageMapper<FunctionNotExistException> functionNotExistsMapper = (String desc) -> desc.indexOf("Function") == 0
                ? Optional.of(new FunctionNotExistException(desc, functionPath))
                : Optional.empty();

        Function function = sendGetRequest("getFunction", Map.of(
                "database", functionPath.getDatabaseName(), "function",
                functionPath.getObjectName()),
                GetFunctionResponse.class, functionNotExistsMapper).getFunction();
        return new VervericaFunction(function);
    }

    @Override
    public boolean functionExists(ObjectPath functionPath) throws CatalogException {
        try {
            return listFunctions(functionPath.getDatabaseName()).contains(functionPath.getObjectName());
        } catch (DatabaseNotExistException e) {
            return false;
        }
    }

    @Override
    public CatalogTableStatistics getTableStatistics(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        return CatalogTableStatistics.UNKNOWN;
    }

    @Override
    public CatalogColumnStatistics getTableColumnStatistics(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        return CatalogColumnStatistics.UNKNOWN;
    }

    private <T> T sendGetRequest(String action, Map<String, String> params, Class<T> responseType) throws CatalogException {
        return sendGetRequest(action, params, responseType, null);
    }

    private <T, E extends Exception> T sendGetRequest(String action, Map<String, String> params, Class<T> responseType,
                                                      ExceptionMessageMapper<E> descriptionMapper)
            throws CatalogException, E {

        CatalogHttpClientResponse response = client.send(getBasePath(), action, params);

        try {
            switch (response.getStatusCode()) {
                case 200:
                    return OBJECT_MAPPER.readValue(response.getBody(), responseType);
                case 403:
                    throw new CatalogException("Forbidden - 403. Probably invalid namespace");
                case 404:
                    final String description = String.valueOf(OBJECT_MAPPER.readValue(response.getBody(), Map.class)
                            .get("description"));
                    if (descriptionMapper != null) {
                        final Optional<E> maybeMappedException = descriptionMapper.map(description);
                        if (maybeMappedException.isPresent()) {
                            throw maybeMappedException.get();
                        }
                    }
                    throw new CatalogException(description);
            }
        } catch (JsonProcessingException e) {
            throw new CatalogException("Problem with parsing response body");
        }

        throw new CatalogException("Status code: " + response.getStatusCode());
    }

    private static ExceptionMessageMapper<DatabaseNotExistException> databaseNotExistsMapper(final String databaseName) {
        return desc -> desc.indexOf("Database") == 0 ? Optional.of(new DatabaseNotExistException(desc, databaseName)) : Optional.empty();
    }

    private String getBasePath() {
        return String.format("%s/catalog/v1beta2/namespaces/%s/catalogs/%s", ververicaUrl, ververicaNamespace, ververicaCatalog);
    }

    private boolean isView(ObjectPath tablePath) {
        try {
            return listViews(tablePath.getDatabaseName()).contains(tablePath.getObjectName());
        } catch (DatabaseNotExistException e) {
            return false;
        }
    }

    private static Schema parseVvpSchema(VvpSchema vvpSchema) {
        Builder schemaBuilder = Schema.newBuilder();

        Optional.ofNullable(
                vvpSchema.getColumn())
                .orElse(Collections.emptyList())
                .forEach(vvpColumn -> addColumnToSchema(schemaBuilder, vvpColumn));

        Optional.ofNullable(
                vvpSchema.getWatermarkSpec())
                .orElse(Collections.emptyList())
                .forEach(vvpWatermark -> schemaBuilder.watermark(vvpWatermark.getTimeColumn(), vvpWatermark.getWatermarkExpression()));

        if (vvpSchema.getPrimaryKey() != null && vvpSchema.getPrimaryKey().getColumn() != null) {
            schemaBuilder.primaryKey(vvpSchema.getPrimaryKey().getColumn());
        }

        return schemaBuilder.build();
    }

    private static void addColumnToSchema(Schema.Builder schemaBuilder, VvpColumn vvpColumn) {
        if (!StringUtils.isNullOrWhitespaceOnly(vvpColumn.getExpression())) {
            schemaBuilder.columnByExpression(vvpColumn.getName(), vvpColumn.getExpression());
        } else if (vvpColumn.getMeta() != null) {
            schemaBuilder.columnByMetadata(vvpColumn.getName(), vvpColumn.getType(), vvpColumn.getMeta().getName(), vvpColumn.getMeta().isVirtual());
        } else {
            schemaBuilder.column(vvpColumn.getName(), vvpColumn.getType());
        }
    }

    @FunctionalInterface
    private interface ExceptionMessageMapper<E extends Exception> {
        Optional<E> map(String message);
    }

}
