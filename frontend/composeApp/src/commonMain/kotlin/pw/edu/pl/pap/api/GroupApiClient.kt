package pw.edu.pl.pap.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.flow
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.TotalExpenses
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.util.sortingSystem.ExpenseMap
import pw.edu.pl.pap.util.sortingSystem.GroupMapKey
import pw.edu.pl.pap.util.sortingSystem.Order
import javax.swing.GroupLayout.Group

class GroupApiClient(baseUrl: String, httpClient: HttpClient, userToken: String) :
    BaseApiClient(baseUrl, httpClient, userToken) {

    override fun setUrl(newUrl: String) {
        baseUrl = "$newUrl/group/"
    }

    suspend fun getUserGroups(): List<UserGroup> {
        return get("all").body()
    }
}