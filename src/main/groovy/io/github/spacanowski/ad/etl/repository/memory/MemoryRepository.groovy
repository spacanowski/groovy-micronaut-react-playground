package io.github.spacanowski.ad.etl.repository.memory

import io.github.spacanowski.ad.etl.config.MemoryDataConfiguration
import io.github.spacanowski.ad.etl.model.output.CampainOutput
import io.github.spacanowski.ad.etl.repository.AdvertisingRepository
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Requires

@Requires(notEnv = 'db')
@Context
class MemoryRepository implements AdvertisingRepository {

    final def dataConfiguration

    MemoryRepository(MemoryDataConfiguration dataConfiguration) {
        this.dataConfiguration = dataConfiguration;
    }

    def getFilteredData(Optional<List<String>> datasources, Optional<List<String>> campains) {
        def data = dataConfiguration.getData().data

        data.findAll {containsValue(datasources, it.datasource)}
            .findAll {containsValue(campains, it.campainName)}
            .groupBy{it.date}
            .collectEntries { k, v -> [k, [v.clicks.sum(), v.impressions.sum()]] }
            .collect{k, v -> new CampainOutput(day: k.format( 'dd-MM-yyyy' ),
                                              clicks:v[0],
                                              impressions:v[1])}
    }

    def getCampains(Optional<List<String>> datasources) {
        if (datasources.isPresent()) {
            datasources.get()
                        .collectMany{dataConfiguration.getData().campains.get(it)}
                        .unique()
        } else {
            dataConfiguration.getData().campains.values().collectMany{it}
        }
    }

    def getDataSources() {
        dataConfiguration.getData().datasources
    }

    private def containsValue(list, value) {
        list.isPresent() ? list.get().contains(value) : true
    }
}
