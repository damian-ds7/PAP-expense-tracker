package pw.edu.pl.pap.repositories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.edu.pl.pap.api.data.ConfigApi
import pw.edu.pl.pap.data.databaseAssociatedData.Category
import pw.edu.pl.pap.data.databaseAssociatedData.Currency
import pw.edu.pl.pap.data.databaseAssociatedData.PaymentMethod
import pw.edu.pl.pap.data.databaseAssociatedData.User

class ConfigRepository(private val api: ConfigApi) {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    private val _currencies = MutableStateFlow<List<Currency>>(emptyList())
    val currencies: StateFlow<List<Currency>> get() = _currencies

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> get() = _paymentMethods

    private val _keyPatterns = MutableStateFlow<List<String>>(emptyList())
    val keyPatterns: StateFlow<List<String>> get() = _keyPatterns

    private val _roles = MutableStateFlow<List<String>>(emptyList())
    val roles: StateFlow<List<String>> get() = _roles


    suspend fun loadConfig() {
        getCategories()
        getCurrencies()
        getPaymentMethods()
        getChartKeys()
        getRoles()
    }

    private suspend fun getCategories() {
        _categories.value = api.getCategories()
    }

    private suspend fun getCurrencies() {
        _currencies.value = api.getCurrencies()
    }

    private suspend fun getPaymentMethods() {
        _paymentMethods.value = api.getMethods()
    }

    private suspend fun getChartKeys() {
        _keyPatterns.value = api.getChartKeys()
    }

    private suspend fun getRoles() {
        _roles.value = api.getRoles()
    }
}