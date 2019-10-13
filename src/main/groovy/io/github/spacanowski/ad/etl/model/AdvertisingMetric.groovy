package io.github.spacanowski.ad.etl.model

import groovy.transform.ToString

@ToString(includeNames = true)
class AdvertisingMetric {

    Date date
    String datasource
    String campainName
    int clicks
    int impressions
}
