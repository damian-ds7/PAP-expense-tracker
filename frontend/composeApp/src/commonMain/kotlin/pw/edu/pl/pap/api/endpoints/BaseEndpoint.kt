package pw.edu.pl.pap.api.endpoints

abstract class BaseEndpoint(
    private val basePath: String,
    private val relativePath: String
) : ApiEndpoint {
    override val path: String
        get() = "$basePath$relativePath"
}