package pw.edu.pl.pap.api.endpoints

sealed class GroupEndpoint(relativePath: String) : BaseEndpoint("/group", relativePath) {
    data object GroupList : GroupEndpoint("/all")
}