package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.GET
import pw.edu.pl.pap.data.databaseAssociatedData.Category
import pw.edu.pl.pap.data.databaseAssociatedData.Currency
import pw.edu.pl.pap.data.databaseAssociatedData.PaymentMethod
import pw.edu.pl.pap.data.databaseAssociatedData.User

interface ConfigApi {

    @GET("currency/all")
    suspend fun getCurrencies(): List<Currency>

    @GET("method/all")
    suspend fun getMethods(): List<PaymentMethod>

    @GET("category/all")
    suspend fun getCategories(): List<Category>

    @GET("chart/keys")
    suspend fun getChartKeys(): List<String>

    @GET("role/all")
    suspend fun getRoles(): List<String>
}