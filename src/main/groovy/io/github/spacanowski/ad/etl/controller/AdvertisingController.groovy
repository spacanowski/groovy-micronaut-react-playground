package io.github.spacanowski.ad.etl.controller

import io.github.spacanowski.ad.etl.service.DataService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller('/data')
class AdvertisingController {

    final def dataService

    AdvertisingController(DataService dataService) {
        this.dataService = dataService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    def getData(Optional<List<String>> datasources, Optional<List<String>> campains) {
        dataService.getFilteredData(datasources, campains)
    }

    @Get(value = '/datasources', produces = MediaType.APPLICATION_JSON)
    def getDatasources() {
        dataService.getDataSources()
    }

    @Get(value = '/campains', produces = MediaType.APPLICATION_JSON)
    def getCampains(Optional<List<String>> datasources) {
        dataService.getCampains(datasources)
    }
}
