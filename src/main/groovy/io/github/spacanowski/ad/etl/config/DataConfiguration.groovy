package io.github.spacanowski.ad.etl.config

import io.github.spacanowski.ad.etl.loader.CsvLoader
import io.micronaut.context.annotation.Context

import javax.annotation.PostConstruct

@Context
class DataConfiguration {

    def data;
    final def dataSource;

    DataConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    def init() {
        data = CsvLoader.load(dataSource.getData())
    }
}
