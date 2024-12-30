package pw.edu.pl.pap.api.endpoints

sealed class GroupEndpoint(private val relativePath: String) : ApiEndpoint {
    companion object {
        private const val BASE_PATH = "/group"
    }

    override val path: String
        get() = "$BASE_PATH$relativePath"

    data object GroupList : GroupEndpoint("/all")
}