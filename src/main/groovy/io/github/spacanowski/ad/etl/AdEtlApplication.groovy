package io.github.spacanowski.ad.etl

import io.micronaut.runtime.Micronaut

import groovy.transform.CompileStatic

@CompileStatic
class AdEtlApplication {

    static void main(String[] args) {
        Micronaut.run(AdEtlApplication)
    }
}