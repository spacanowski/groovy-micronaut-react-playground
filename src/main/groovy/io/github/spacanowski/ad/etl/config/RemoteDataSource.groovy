package io.github.spacanowski.ad.etl.config

import io.github.spacanowski.ad.etl.client.DataClient
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Requires

@Requires(property = "app.data.source", value = "remote")
@Context
class RemoteDataSource implements DataSource {

    final def dataClient;

    RemoteDataSource(DataClient dataClient) {
        this.dataClient = dataClient
    }

    @Override
    def getData() {
        dataClient.getData().readLines()
    }
}
