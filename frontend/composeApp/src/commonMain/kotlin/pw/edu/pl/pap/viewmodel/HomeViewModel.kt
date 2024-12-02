package pw.edu.pl.pap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.*

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

    private val _groupedRecords = MutableStateFlow<Map<LocalDate, List<Record>>>(emptyMap())
    val groupedRecords: StateFlow<Map<LocalDate, List<Record>>> = _groupedRecords

    fun fetchRecords() {
        viewModelScope.launch {
            try {
                apiClient.getRecords().collect { records ->
                    _groupedRecords.value = records
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateRecentRecord() {
        viewModelScope.launch {
            try {
                apiClient.getRecentRecord().collect { record: Record ->
                    val currentMap = _groupedRecords.value
                    val currentList = currentMap[record.date] ?: emptyList()
                    _groupedRecords.value = currentMap + (record.date to currentList + record)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun passApiClient(): ApiClient {
        return apiClient
    }
}