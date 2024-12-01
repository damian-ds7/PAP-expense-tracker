package pw.edu.pl.pap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.*

class HomeViewModel(private val apiClient: ApiClient) : ViewModel() {
    private val _expensesInfo = MutableStateFlow<InitialExpenses?>(null)
    val expensesInfo: StateFlow<InitialExpenses?> = _expensesInfo

    private val _records = MutableStateFlow<List<Record>>(emptyList())
    val records: StateFlow<List<Record>> = _records

    fun fetchHomeInfo() {
        viewModelScope.launch {
            try {
                val homeData = apiClient.getHome("herkules1@gmail.com")
                _expensesInfo.value = homeData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRecords() {
        viewModelScope.launch {
            try {
                apiClient.getRecords().collect { recordList ->
                    _records.value = recordList
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