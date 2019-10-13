package io.github.spacanowski.ad.etl.loader

import io.github.spacanowski.ad.etl.model.AdvertisingData
import io.github.spacanowski.ad.etl.model.AdvertisingMetric

import java.text.SimpleDateFormat

class CsvLoader {

    static def load(lines) {
        def advertisingData = new AdvertisingData()
        def dateFormatter = new SimpleDateFormat("dd.MM.yyyy")

        // TODO if becomes more complicated use dedicated CSV parser
        lines.remove(0)
        lines.each { line ->
            def fields = line.split(",")

            def date = dateFormatter.parse(fields[0])
            def datasource = fields[1]
            def campainName = fields[2]
            def clicks = fields.length >= 4 ? fields[3] as Integer : 0
            def impressions = fields.length >= 5 ? fields[4] as Integer : 0

            advertisingData.data.add(new AdvertisingMetric(
                    date: date,
                    datasource: datasource,
                    campainName: campainName,
                    clicks: clicks,
                    impressions: impressions
            ))

            advertisingData.dates.add(date)
            advertisingData.datasources.add(datasource)

            def datasourceCampain = advertisingData.campains.get(datasource)
            if (datasourceCampain) {
                datasourceCampain.add(campainName)
            } else {
                advertisingData.campains.put(datasource, [campainName])
            }
        }

        advertisingData.dates.sort { it }

        advertisingData
    }
}
