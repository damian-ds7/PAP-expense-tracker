package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.*
import io.ktor.client.statement.*
import pw.edu.pl.pap.data.databaseAssociatedData.*

interface ExpenseApi {

    @GET("expense/get/{id}")
    suspend fun getExpense(@Path("id") id: Long): Expense

    @GET("expense/recent/{group}")
    suspend fun getRecentExpense(@Path("group") group: String): Expense

    @GET("expense/state/{group}")
    suspend fun getTotalExpenses(@Path("group") group: String): TotalExpenses

    @GET("expense/dateMap/group/{group}")
    suspend fun getExpenseDateMap(
        @Path("group") group: String,
        @QueryMap paramsMap: Map<String, String?>
    ): DateKeyExpensePage

    @GET("expense/categoryMap/group/{group}")
    suspend fun getExpenseCatMap(
        @Path("group") group: String,
        @QueryMap paramsMap: Map<String, String?>
    ): StringKeyExpensePage

    @POST("expense/create")
    suspend fun postNewExpense(@Body newExpense: NewExpense): HttpResponse

    @PUT("expense/modify/{id}")
    suspend fun updateExpense(@Path("id") id: Long, @Body expense: Expense): HttpResponse

    @DELETE("expense/delete/{id}")
    suspend fun deleteExpense(@Path("id") id: Long): HttpResponse
}