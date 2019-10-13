package io.github.spacanowski.ad.etl.model

import groovy.transform.ToString

@ToString(includeNames = true)
class AdvertisingData {

    def data = []
    Set datasources = []
    def campains = [:]
    Set dates = []
}
