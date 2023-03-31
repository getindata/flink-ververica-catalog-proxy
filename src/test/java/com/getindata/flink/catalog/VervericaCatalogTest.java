package com.getindata.flink.catalog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.apache.commons.io.IOUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.catalog.CatalogBaseTable.TableKind;
import org.apache.flink.table.catalog.FunctionLanguage;
import org.apache.flink.table.catalog.ObjectPath;
import org.apache.flink.table.catalog.exceptions.CatalogException;
import org.apache.flink.table.catalog.exceptions.DatabaseNotExistException;
import org.apache.flink.table.catalog.exceptions.FunctionNotExistException;
import org.apache.flink.table.catalog.exceptions.TableNotExistException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.getindata.flink.catalog.httpclient.JavaNetHttpClientFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class VervericaCatalogTest {

    private static final String SAMPLES_FOLDER = "/http/";

    private static WireMockServer wireMockServer;

    private static VervericaCatalog ververicaCatalog;

    @BeforeAll
    public static void setUpAll() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        ververicaCatalog = new VervericaCatalog("vvp", wireMockServer.baseUrl(), "default", "vvp",
                JavaNetHttpClientFactory.createClient(new Configuration()));
        setupStubs();
    }

    @AfterAll
    public static void cleanUpAll() {
        wireMockServer.stop();
    }

    private static void setupStubs() {
        // listDatabases
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:listDatabases", "listDatabases/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default2/catalogs/vvp:listDatabases", "listDatabases/403.json", 403);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp2:listDatabases", "listDatabases/404.json", 404);

        //getDatabase
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getDatabase?database=default", "getDatabase/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getDatabase?database=default2", "getDatabase/404.json", 404);

        // listViews
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:listViews?database=default", "listViews/200.json", 200);

        // listTables
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:listTables?database=default", "listTables/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:listTables?database=default2", "listTables/404.json", 404);

        // getTable
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getTable?database=default&table=my_table", "getTable/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getTable?database=default&table=my_table2", "getTable/404.json", 404);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getTable?table=my_table&database=default", "getTable/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getTable?table=my_table2&database=default", "getTable/404.json", 404);

        // getView
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getView?database=default&view=my_view", "getView/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getView?database=default&view=my_view2", "getView/404.json", 404);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getView?view=my_view&database=default", "getView/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getView?view=my_view2&database=default", "getView/404.json", 404);

        // listFunctions
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:listFunctions?database=default", "listFunctions/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default2/catalogs/vvp:listFunctions?database=default", "listFunctions/403.json", 403);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp2:listFunctions?database=default", "listFunctions/404.json", 404);

        // getFunction
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getFunction?function=AddOneUDF&database=default", "getFunction/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getFunction?function=AddOneUDF2&database=default", "getFunction/404.json", 404);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getFunction?database=default&function=AddOneUDF", "getFunction/200.json", 200);
        setupServerStub("/catalog/v1beta2/namespaces/default/catalogs/vvp:getFunction?database=default&function=AddOneUDF2", "getFunction/404.json", 404);
    }

    private static StubMapping setupServerStub(String paramsPath, String output, int status) {
        return wireMockServer.stubFor(
                get(urlEqualTo(paramsPath))
                        .willReturn(
                                aResponse()
                                        .withStatus(status)
                                        .withBody(readTestFile(SAMPLES_FOLDER + output))));
    }

    private static String readTestFile(String pathToFile) {
        try {
            return IOUtils.resourceToString(pathToFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error when reading test file path: " + pathToFile, e);
        }
    }

    @Test
    void shouldListDatabases() {
        var databases = ververicaCatalog.listDatabases();

        assertEquals(List.of("default"), databases);
    }

    @Test
    void shouldThrowExceptionWhenNamespaceOrCatalogInvalid() {
        var vvpCatalog1 = new VervericaCatalog("vvp", wireMockServer.baseUrl(), "default2", "vvp",
                JavaNetHttpClientFactory.createClient(new Configuration()));
        var vvpCatalog2 = new VervericaCatalog("vvp", wireMockServer.baseUrl(), "default", "vvp2",
                JavaNetHttpClientFactory.createClient(new Configuration()));

        assertThrows(CatalogException.class, () -> {
            vvpCatalog1.listDatabases();
        });
        assertThrows(CatalogException.class, () -> {
            vvpCatalog2.listDatabases();
        });
    }

    @Test
    void shouldGetDatabaseOrThrowException() throws DatabaseNotExistException {
        var database = ververicaCatalog.getDatabase("default");

        assertEquals("Default Database", database.getComment());
        assertEquals(Collections.emptyMap(), database.getProperties());
    }

    @Test
    void shouldThrowDatabaseNotExist() {
        assertThrows(DatabaseNotExistException.class, () -> {
            ververicaCatalog.getDatabase("default2");
        });
    }

    @Test
    void shouldCheckIfDatabaseExists() {
        assertTrue(ververicaCatalog.databaseExists("default"));
        assertFalse(ververicaCatalog.databaseExists("default2"));
    }

    @Test
    void shouldListViews() throws DatabaseNotExistException {
        var views = ververicaCatalog.listViews("default");

        assertEquals(List.of("my_view"), views);
    }

    @Test
    void shouldListTables() throws DatabaseNotExistException {
        var tables = ververicaCatalog.listTables("default");

        assertEquals(List.of("my_table", "my_view"), tables);
    }

    @Test
    void shouldThrowDatabaseNotExistForListTables() {
        assertThrows(DatabaseNotExistException.class, () -> {
            ververicaCatalog.listTables("default2");
        });
    }

    @Test
    void shouldGetTable() throws TableNotExistException {
        var table = ververicaCatalog.getTable(new ObjectPath("default", "my_table"));

        assertEquals("", table.getComment());
        assertEquals(Map.of("connector", "datagen"), table.getOptions());
        assertEquals(TableKind.TABLE, table.getTableKind());
    }

    @Test
    void shouldGetView() throws TableNotExistException {
        var table = ververicaCatalog.getTable(new ObjectPath("default", "my_view"));

        assertEquals("", table.getComment());
        assertEquals(Map.of(), table.getOptions());
        assertEquals(TableKind.VIEW, table.getTableKind());
    }

    @Test
    void shouldCheckIfTableExists() {
        assertTrue(ververicaCatalog.tableExists(new ObjectPath("default", "my_view")));
        assertTrue(ververicaCatalog.tableExists(new ObjectPath("default", "my_table")));
        assertFalse(ververicaCatalog.tableExists(new ObjectPath("default", "my_view2")));
    }

    @Test
    void shouldThrowTableNotExist() {
        assertThrows(TableNotExistException.class, () -> {
            ververicaCatalog.getTable(new ObjectPath("default", "my_table2"));
        });
    }

    @Test
    void shouldListFunctions() throws DatabaseNotExistException {
        var functions = ververicaCatalog.listFunctions("default");

        assertEquals(List.of("AddOneUDF"), functions);
    }

    @Test
    void shouldThrowExceptionWhenFunctionNamespaceOrCatalogInvalid() {
        var vvpCatalog1 = new VervericaCatalog("vvp", wireMockServer.baseUrl(), "default2", "vvp",
                JavaNetHttpClientFactory.createClient(new Configuration()));
        var vvpCatalog2 = new VervericaCatalog("vvp", wireMockServer.baseUrl(), "default", "vvp2",
                JavaNetHttpClientFactory.createClient(new Configuration()));

        assertThrows(CatalogException.class, () -> {
            vvpCatalog1.listFunctions("default");
        });
        assertThrows(CatalogException.class, () -> {
            vvpCatalog2.listFunctions("default");
        });
    }

    @Test
    void shouldGetFunction() throws FunctionNotExistException {
        var function = ververicaCatalog.getFunction(new ObjectPath("default", "AddOneUDF"));

        assertEquals("com.getindata.function.AddOneUDF", function.getClassName());
        assertEquals(FunctionLanguage.JAVA, function.getFunctionLanguage());
    }

    @Test
    void shouldThrowFunctionNotExist() {
        assertThrows(FunctionNotExistException.class, () -> {
            ververicaCatalog.getFunction(new ObjectPath("default", "AddOneUDF2"));
        });
    }

    @Test
    void shouldCheckIfFunctionExists() {
        assertTrue(ververicaCatalog.functionExists(new ObjectPath("default", "AddOneUDF")));
        assertFalse(ververicaCatalog.functionExists(new ObjectPath("default", "AddOneUDF2")));
    }
}
