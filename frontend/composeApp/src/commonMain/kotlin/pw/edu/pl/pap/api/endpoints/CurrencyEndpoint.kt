package pw.edu.pl.pap.api.endpoints

open class CurrencyEndpoint(relativePath: String) : BaseEndpoint("/currency", relativePath) {
    data object AllCurrencies : CurrencyEndpoint("/all")
}