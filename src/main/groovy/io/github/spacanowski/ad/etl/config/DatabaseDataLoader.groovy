package io.github.spacanowski.ad.etl.config

import io.github.spacanowski.ad.etl.loader.CsvLoader
import io.github.spacanowski.ad.etl.repository.db.DatabaseRepository
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Requires

import javax.annotation.PostConstruct

@Requires(env = 'db')
@Context
class DatabaseDataLoader {

    final def databaseRepository
    final def dataSource;

    DatabaseDataLoader(DatabaseRepository databaseRepository, DataSource dataSource) {
        this.databaseRepository = databaseRepository;
        this.dataSource = dataSource;
    }

    @PostConstruct
    def init() {
        if (!databaseRepository.isDataLoaded()) {
            CsvLoader.load(dataSource.getData(), { date, datasource, campainName, clicks, impressions ->
                databaseRepository.insertAdvertisingData(UUID.randomUUID().toString(),
                                                            date,
                                                            datasource,
                                                            campainName,
                                                            clicks,
                                                            impressions)
            })

            databaseRepository.setDataLoaded();
        }
    }
}
