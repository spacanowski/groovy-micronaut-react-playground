package io.github.spacanowski.ad.etl.client

import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Retryable

@Requires(property = "app.data.source", value = "remote")
@Client(
        value = '${app.data.remote.url}',
        path = '${app.data.remote.path}')
@Retryable(
        attempts = '${app.data.remote.retry.attempts}',
        delay = '${app.data.remote.retry.delay}')
interface DataClient {

    @Get
    String getData()
}
