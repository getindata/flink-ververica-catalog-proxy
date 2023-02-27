package com.getindata.flink.catalog;

import org.apache.flink.table.api.ValidationException;
import org.apache.flink.table.factories.CatalogFactory.Context;
import org.apache.flink.table.factories.FactoryUtil.DefaultCatalogContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


class VervericaCatalogFactoryTest {

    @Test
    void testMissingUrl() {
        Assertions.assertThrows(ValidationException.class, () -> {
            Context context = new DefaultCatalogContext("name", Map.of(), null, null);
            new VervericaCatalogFactory().createCatalog(context);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void testNullUrl(String url, Class<NullPointerException> exceptionClass) {
        Map<String, String> map = new HashMap<>();
        map.put("vvp-url", url);
        Assertions.assertThrows(exceptionClass, () -> {
            Context context = new DefaultCatalogContext("name", map, null, null);
            new VervericaCatalogFactory().createCatalog(context);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void testNullCatalog(String catalog, Class<NullPointerException> exceptionClass) {
        Map<String, String> map = new HashMap<>();
        map.put("vvp-url", "url");
        map.put("vvp-catalog", catalog);
        Assertions.assertThrows(exceptionClass, () -> {
            Context context = new DefaultCatalogContext("name", map, null, null);
            new VervericaCatalogFactory().createCatalog(context);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void testNullNamespace(String namespace, Class<NullPointerException> exceptionClass) {
        Map<String, String> map = new HashMap<>();
        map.put("vvp-url", "url");
        map.put("vvp-namespace", namespace);
        Assertions.assertThrows(exceptionClass, () -> {
            Context context = new DefaultCatalogContext("name", map, null, null);
            new VervericaCatalogFactory().createCatalog(context);
        });
    }

    private static Stream<? extends Arguments> provideArguments() {
        return Stream.of(
                Arguments.of(null, NullPointerException.class),
                Arguments.of("", IllegalArgumentException.class),
                Arguments.of("   ", IllegalArgumentException.class)
        );
    }

}
