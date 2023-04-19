package com.getindata.flink.catalog;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.factories.CatalogFactory;
import org.apache.flink.table.factories.FactoryUtil;

import com.getindata.flink.catalog.httpclient.JavaNetHttpClientFactory;

import static com.getindata.flink.catalog.VervericaCatalogOptions.*;

@Slf4j
public class VervericaCatalogFactory implements CatalogFactory {

    @Override
    public Catalog createCatalog(Context context) {
        final FactoryUtil.CatalogFactoryHelper helper =
                FactoryUtil.createCatalogFactoryHelper(this, context);
        helper.validate();

        return new VervericaCatalog(context.getName(),
                helper.getOptions().get(VERVERICA_URL),
                helper.getOptions().get(VERVERICA_NAMESPACE),
                helper.getOptions().get(VERVERICA_CATALOG),
                JavaNetHttpClientFactory.createClient(helper.getOptions()));
    }

    @Override
    public String factoryIdentifier() {
        return VervericaCatalogOptions.IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        final Set<ConfigOption<?>> options = new HashSet<>();
        options.add(VERVERICA_URL);
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        final Set<ConfigOption<?>> options = new HashSet<>();
        options.add(VERVERICA_NAMESPACE);
        options.add(VERVERICA_CATALOG);
        options.add(HTTP_PROXY_HEADERS);
        options.add(HTTP_ALLOW_SELF_SIGNED);
        options.add(HTTP_SERVER_TRUSTED_CERT);
        options.add(HTTP_FOLLOW_REDIRECTS);
        return options;
    }
}
