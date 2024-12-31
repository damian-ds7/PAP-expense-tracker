package pw.edu.pl.pap.api

import io.ktor.client.call.*
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.api.endpoints.ChartEndpoint
import pw.edu.pl.pap.util.charts.ChartData

class ChartsApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

    suspend fun getGroupYearData(year: Number): ChartData {
        val map: Map<String, Float> = get(ChartEndpoint.GroupYearData(year)).body()
        return map.toMap(ChartData())
    }

    suspend fun getGroupCategoriesPeriod(begin: LocalDate, end: LocalDate): ChartData {
        return get(ChartEndpoint.GroupCategoriesPeriod(begin, end)).body()
    }

    suspend fun getUserYearData(year: Number): ChartData {
        return get(ChartEndpoint.UserYearData(year)).body()
    }

    suspend fun getUserCategoriesPeriod(begin: LocalDate, end: LocalDate): ChartData {
        return get(ChartEndpoint.UserCategoriesPeriod(begin, end)).body()
    }
}