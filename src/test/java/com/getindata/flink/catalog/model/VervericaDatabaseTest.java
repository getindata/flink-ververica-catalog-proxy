package com.getindata.flink.catalog.model;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.getindata.ververica.client.model.Database;

class VervericaDatabaseTest {

    private static final String COMMENT = "comment";
    private static final Map<String, String> PROPERTIES = Map.of("k1", "v1");

    @Test
    void shouldProperlyCreateDatabase() {
        var database = new Database().comment(COMMENT).properties(PROPERTIES);

        var vvpDatabase = new VervericaDatabase(database);

        Assertions.assertEquals(COMMENT, vvpDatabase.getComment());
        Assertions.assertEquals(PROPERTIES, vvpDatabase.getProperties());
    }

    @Test
    void shouldProperlyCopyDatabase() {
        var vvpDatabase = new VervericaDatabase(COMMENT, PROPERTIES);
        var properties2 = Map.of("k2", "v2");

        var vvpDatabase2 = vvpDatabase.copy();
        var vvpDatabase3 = vvpDatabase.copy(properties2);

        Assertions.assertEquals(COMMENT, vvpDatabase2.getComment());
        Assertions.assertEquals(PROPERTIES, vvpDatabase2.getProperties());
        Assertions.assertEquals(COMMENT, vvpDatabase3.getComment());
        Assertions.assertEquals(properties2, vvpDatabase3.getProperties());

    }

}
