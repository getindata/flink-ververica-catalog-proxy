package com.getindata.flink.catalog.model;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.apache.flink.table.catalog.CatalogFunction;
import org.apache.flink.table.catalog.FunctionLanguage;

import com.getindata.ververica.client.model.Function;
import com.getindata.ververica.client.model.Function.FunctionLanguageEnum;

@AllArgsConstructor
public class VervericaFunction implements CatalogFunction {

    private String className;
    private FunctionLanguage language;
    private String description;

    public VervericaFunction(Function function) {
        className = function.getClassName();
        language = parseVvpLanguage(function.getFunctionLanguage());
        description = function.getDescription();
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public CatalogFunction copy() {
        return new VervericaFunction(className, language, description);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Optional<String> getDetailedDescription() {
        return Optional.empty();
    }

    @Override
    public boolean isGeneric() {
        return false;
    }

    @Override
    public FunctionLanguage getFunctionLanguage() {
        return language;
    }

    private FunctionLanguage parseVvpLanguage(FunctionLanguageEnum languageEnum) {
        switch (languageEnum) {
            case FUNCTION_LANGUAGE_JAVA:
                return FunctionLanguage.JAVA;
            case FUNCTION_LANGUAGE_SCALA:
                return FunctionLanguage.SCALA;
            case FUNCTION_LANGUAGE_PYTHON:
                return FunctionLanguage.PYTHON;
        }
        throw new UnsupportedVervericaLanguageException(String.format("Unsupported language: %s", languageEnum.toString()));
    }
}
