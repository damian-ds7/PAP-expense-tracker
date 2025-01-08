package pw.edu.pl.pap.api.endpoints

sealed class UserEndpoints(relativePath: String) : BaseEndpoint("/user", relativePath) {
    data class Search(val group: String): UserEndpoints("/search/$group")
}