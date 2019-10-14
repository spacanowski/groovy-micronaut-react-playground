package io.github.spacanowski.ad.etl.service

import io.github.spacanowski.ad.etl.repository.AdvertisingRepository
import io.micronaut.context.annotation.Context

@Context
class DataService {

    final def advertisingRepository

    DataService(AdvertisingRepository advertisingRepository) {
        this.advertisingRepository = advertisingRepository;
    }

    def getFilteredData(Optional<List<String>> datasources, Optional<List<String>> campains) {
        advertisingRepository.getFilteredData(datasources, campains)
    }

    def getCampains(Optional<List<String>> datasources) {
        advertisingRepository.getCampains(datasources)
    }

    def getDataSources() {
        advertisingRepository.getDataSources()
    }
}
