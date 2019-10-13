package io.github.spacanowski.ad.etl.config

import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value

@Requires(property = "app.data.source", value = "local")
@Context
class LocalDataSource implements DataSource {

    @Value('${app.data.local.path}')
    def filePath

    @Override
    def getData() {
        new File(filePath).readLines()
    }
}
