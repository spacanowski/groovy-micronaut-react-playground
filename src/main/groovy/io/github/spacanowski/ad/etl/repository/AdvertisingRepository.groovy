package io.github.spacanowski.ad.etl.repository

interface AdvertisingRepository {

    def getFilteredData(Optional<List<String>> datasources, Optional<List<String>> campains)

    def getCampains(Optional<List<String>> datasources)

    def getDataSources()
}
