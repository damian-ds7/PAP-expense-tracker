package pw.edu.pl.pap.api.endpoints

sealed class GroupEndpoint(relativePath: String) : BaseEndpoint("/group", relativePath) {
    data object GroupList : GroupEndpoint("/all")
    data class UpdateGroup(val id: Int) : GroupEndpoint("/update/$id")
    data class DeleteGroup(val id: Int) : GroupEndpoint("/delete/$id")
    data object postNewGroup : GroupEndpoint("/insert")
}