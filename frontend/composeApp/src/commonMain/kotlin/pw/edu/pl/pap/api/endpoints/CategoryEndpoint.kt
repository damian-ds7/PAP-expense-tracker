package pw.edu.pl.pap.api.endpoints

open class CategoryEndpoint(relativePath: String) : BaseEndpoint("/category", relativePath) {
    data object AllCategories : CategoryEndpoint("/all")
}