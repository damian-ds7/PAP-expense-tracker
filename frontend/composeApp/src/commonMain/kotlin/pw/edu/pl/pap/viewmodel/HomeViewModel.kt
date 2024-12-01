package pw.edu.pl.pap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.*
import java.time.format.DateTimeFormatter

class HomeViewModel(private val apiClient: ApiClient) : ViewModel() {
    private val _expensesInfo = MutableStateFlow<TotalExpenses?>(null)
    val expensesInfo: StateFlow<TotalExpenses?> = _expensesInfo

    fun fetchHomeInfo() {
        viewModelScope.launch {
            try {
                val homeData = apiClient.getTotalExpenses("family", "herkules1@gmail.com")
                _expensesInfo.value = homeData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _groupedRecords = MutableStateFlow<Map<String, List<Record>>>(emptyMap())
    val groupedRecords: StateFlow<Map<String, List<Record>>> = _groupedRecords

    fun fetchRecords() {
        viewModelScope.launch {
            try {
                apiClient.getRecords().collect { recordList ->
                    _groupedRecords.value = groupRecordsByDate(recordList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun groupRecordsByDate(records: List<Record>): Map<String, List<Record>> {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return records.groupBy { record ->
            record.date.format(dateFormatter)
        }
    }
}