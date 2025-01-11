package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.QueryMap
import pw.edu.pl.pap.util.charts.ChartData

interface ChartApi {

    @GET("chart/map-result/{group}/{keyPattern}")
    suspend fun getData(
        @Path("group") group: String,
        @Path("keyPattern") keyPattern: String,
        @QueryMap paramsMap: Map<String, String?>
    ): ChartData

    @GET("chart/keys")
    suspend fun getKeys(): List<String>
}