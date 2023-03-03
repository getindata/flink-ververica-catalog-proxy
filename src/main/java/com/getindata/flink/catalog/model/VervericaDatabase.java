package com.getindata.flink.catalog.model;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.apache.flink.table.catalog.CatalogDatabase;

import com.getindata.ververica.client.model.Database;

@AllArgsConstructor
public class VervericaDatabase implements CatalogDatabase {

    private final String comment;
    private final Map<String, String> properties;

    public VervericaDatabase(Database database) {
        comment = database.getComment();
        properties = database.getProperties();
    }

    @Override
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public VervericaDatabase copy() {
        return new VervericaDatabase(comment, Map.copyOf(properties));
    }

    /**
     * Returns a copy of this {@code CatalogDatabase} with the given properties.
     *
     * @return a new copy of this database with replaced properties
     */
    @Override
    public VervericaDatabase copy(Map<String, String> properties) {
        return new VervericaDatabase(comment, properties);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getDetailedDescription() {
        return Optional.empty();
    }
}
