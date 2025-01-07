package pw.edu.pl.pap.screenComponents.groupScreens.invitations

import pw.edu.pl.pap.screenComponents.BaseScreenComponent

open class NewInvitationScreenComponent(
    baseComponent: BaseScreenComponent,
    val onDismiss: () -> Unit,
    val onSave: () -> Unit
) : BaseScreenComponent by baseComponent