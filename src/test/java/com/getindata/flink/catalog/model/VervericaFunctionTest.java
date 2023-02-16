package com.getindata.flink.catalog.model;

import com.getindata.ververica.client.model.Function;
import com.getindata.ververica.client.model.Function.FunctionLanguageEnum;
import org.apache.flink.table.catalog.FunctionLanguage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VervericaFunctionTest {

    private static final String CLASS_NAME = "com.getindata.functions.Max";
    private static final String DESCRIPTION = "description";

    @Test
    void shouldParseFunctionWithJavaLanguage() {
        var function = new Function()
                .className(CLASS_NAME)
                .description(DESCRIPTION)
                .functionLanguage(FunctionLanguageEnum.FUNCTION_LANGUAGE_JAVA);

        var vvpFunction = new VervericaFunction(function);

        assertEquals(CLASS_NAME, vvpFunction.getClassName());
        assertEquals(DESCRIPTION, vvpFunction.getDescription().get());
        assertEquals(FunctionLanguage.JAVA, vvpFunction.getFunctionLanguage());

    }

    @Test
    void shouldThrowExceptionForUnknownLanguage() {
        var function = new Function()
                .className(CLASS_NAME)
                .description(DESCRIPTION)
                .functionLanguage(FunctionLanguageEnum.UNRECOGNIZED);

        Assertions.assertThrows(UnsupportedVervericaLanguageException.class, () -> new VervericaFunction(function));
    }

}
