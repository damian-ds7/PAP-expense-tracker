package pw.edu.pl.pap.api

import io.ktor.client.call.*
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.api.endpoints.ChartEndpoint
import java.util.SortedMap

class ChartsApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

    suspend fun getGroupYearData(year: Number): SortedMap<String, Number> {
        return get(ChartEndpoint.GroupYearData(year)).body()
    }

    suspend fun getGroupCategoriesPeriod(begin: LocalDate, end: LocalDate): SortedMap<String, Number> {
        return get(ChartEndpoint.GroupCategoriesPeriod(begin, end)).body()
    }

    suspend fun getUserYearData(year: Number): SortedMap<String, Number> {
        return get(ChartEndpoint.UserYearData(year)).body()
    }

    suspend fun getUserCategoriesPeriod(begin: LocalDate, end: LocalDate): SortedMap<String, Number> {
        return get(ChartEndpoint.UserCategoriesPeriod(begin, end)).body()
    }
}