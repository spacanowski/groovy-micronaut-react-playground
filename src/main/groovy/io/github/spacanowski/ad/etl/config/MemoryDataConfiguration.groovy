package io.github.spacanowski.ad.etl.config

import io.github.spacanowski.ad.etl.loader.CsvLoader
import io.github.spacanowski.ad.etl.model.AdvertisingData
import io.github.spacanowski.ad.etl.model.AdvertisingMetric
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Requires

import javax.annotation.PostConstruct

@Requires(notEnv = 'db')
@Context
class MemoryDataConfiguration {

    def data;
    final def dataSource;

    MemoryDataConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    def init() {
        data = new AdvertisingData()

        CsvLoader.load(dataSource.getData(), { date, datasource, campainName, clicks, impressions ->
            data.data.add(new AdvertisingMetric(
                date: date,
                datasource: datasource,
                campainName: campainName,
                clicks: clicks,
                impressions: impressions
            ))

            data.dates.add(date)
            data.datasources.add(datasource)

            def datasourceCampain = data.campains.get(datasource)
            if (datasourceCampain) {
                datasourceCampain.add(campainName)
            } else {
                data.campains.put(datasource, [campainName])
            }
        })

        data.dates.sort { it }
    }
}
