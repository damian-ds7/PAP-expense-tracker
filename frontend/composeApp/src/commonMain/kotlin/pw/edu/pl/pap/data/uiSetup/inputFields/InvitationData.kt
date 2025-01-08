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
        override val group: UserGroup,
        val onCancel: (UserGroup) -> Unit,
    ) : InvitationData(group)

    data class ReceivedInvitationData (
        val sender: User,
        override val group: UserGroup,
        val onConfirm: (User, UserGroup) -> Unit,
        val onCancel: (User, UserGroup) -> Unit,
    ) : InvitationData(group)
}