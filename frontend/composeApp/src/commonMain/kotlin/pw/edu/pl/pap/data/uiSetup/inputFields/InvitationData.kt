package pw.edu.pl.pap.data.uiSetup.inputFields

import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

sealed class InvitationData(open val group: UserGroup) {
    data class NewInvitationData (
        val receiver: User,
        override val group: UserGroup,
        val onConfirm: (User) -> Unit,
    ) : InvitationData(group)

    data class SentInvitationData (
        val receiver: User,
        val id: Long,
        override val group: UserGroup,
        val onCancel: (Long) -> Unit,
    ) : InvitationData(group)

    data class ReceivedInvitationData (
        val sender: User,
        val id: Long,
        override val group: UserGroup,
        val onConfirm: (Long) -> Unit,
        val onCancel: (Long) -> Unit,
    ) : InvitationData(group)
}