package io.github.spacanowski.ad.etl.repository.db

import io.github.spacanowski.ad.etl.model.output.CampainOutput
import io.github.spacanowski.ad.etl.repository.AdvertisingRepository
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Requires
import io.reactiverse.reactivex.pgclient.PgPool
import io.reactiverse.reactivex.pgclient.Tuple

import java.sql.Date
import java.time.ZoneId

@Requires(env = 'db')
@Context
class DatabaseRepository implements AdvertisingRepository {

    private PgPool client;

    DatabaseRepository(PgPool client) {
        this.client = client;
    }

    def isDataLoaded() {
        client.rxQuery("SELECT is_data_loaded FROM data_loaded WHERE id ='1'")
                .map({ pgRowSet ->
                    def iterator = pgRowSet.iterator()
                    def result = false

                    if (iterator.hasNext()) {
                        result = iterator.next().getBoolean('is_data_loaded')
                    }

                    result
                })
                .blockingGet()
    }

    def setDataLoaded() {
        client.query("UPDATE data_loaded SET is_data_loaded='t' WHERE id ='1'",
                    { ar ->
                        if (!ar.succeeded()) {
                            println("Failure setting load status: ${ar.cause().getMessage()}")
                        }
                    })
    }

    def insertAdvertisingData(id, date, datasource, campainName, clicks, impressions) {
        client.preparedQuery('INSERT INTO campain(id, campain_date, datasource, campain_name, clicks, impressions)' +
                                    'VALUES ($1, $2, $3, $4, $5, $6)',
                                Tuple.of(id,
                                        date.toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate(),
                                        datasource,
                                        campainName,
                                        clicks,
                                        impressions),
                                { ar ->
                                    if (!ar.succeeded()) {
                                        println("Failure inserting data: ${ar.cause().getMessage()}")
                                    }
                                })
    }

    @Override
    def getFilteredData(Optional<List<String>> datasources, Optional<List<String>> campains) {
        def query

        if (datasources.isEmpty() && campains.isEmpty()) {
            query = client.rxQuery('SELECT campain_date, sum(clicks) as clicks, sum(impressions) as impressions ' +
                                        'FROM campain group by campain_date')
        } else if (!datasources.isEmpty() && campains.isEmpty()) {
            query = client.rxPreparedQuery('SELECT campain_date, sum(clicks) as clicks, sum(impressions) as impressions ' +
                                                'FROM campain WHERE datasource = ANY ($1) group by campain_date',
                                            Tuple.tuple().addStringArray(datasources.get() as String[]))
        } else {
            query = client.rxPreparedQuery('SELECT campain_date, sum(clicks) as clicks, sum(impressions) as impressions ' +
                                                'FROM campain WHERE datasource = ANY ($1) AND campain_name = ANY ($2) group by campain_date',
                                            Tuple.tuple().addStringArray(datasources.get() as String[])
                                                        .addStringArray(campains.get() as String[]))
        }

        query.map({ pgRowSet ->
                        def result = []
                        def iterator = pgRowSet.iterator()

                        while (iterator.hasNext()) {
                            def row = iterator.next()
                            result.add(new CampainOutput(
                                day: Date.valueOf(row.getLocalDate("campain_date")).format( 'dd-MM-yyyy' ),
                                clicks: row.getInteger("clicks"),
                                impressions: row.getInteger("impressions")
                            ))
                        }

                        result
                    })
                .blockingGet()
    }

    @Override
    getCampains(Optional<List<String>> datasources) {
        def query

        if (datasources.isEmpty()) {
            query = client.rxQuery('SELECT DISTINCT campain_name FROM campain')
        } else {
            query = client.rxPreparedQuery('SELECT DISTINCT campain_name FROM campain WHERE datasource = ANY ($1)',
                                            Tuple.tuple().addStringArray(datasources.get() as String[]))

        }

        query.map({ pgRowSet ->
                            def result = []
                            def iterator = pgRowSet.iterator()

                            while (iterator.hasNext()) {
                                result.add(iterator.next().getString("campain_name"))
                            }

                            result
                        })
                .blockingGet()
    }

    @Override
    public Object getDataSources() {
        client.rxPreparedQuery('SELECT DISTINCT datasource FROM campain')
                .map({ pgRowSet ->
                    def result = []
                    def iterator = pgRowSet.iterator()

                    while (iterator.hasNext()) {
                        result.add(iterator.next().getString("datasource"))
                    }

                    result
                })
                .blockingGet()
    }
}
