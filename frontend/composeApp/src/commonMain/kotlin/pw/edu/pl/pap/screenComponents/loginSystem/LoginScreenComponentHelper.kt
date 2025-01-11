package pw.edu.pl.pap.screenComponents.loginSystem

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope

open class LoginScreenComponentHelper(
    componentContext: ComponentContext,
    override val coroutineScope: CoroutineScope,
    override val onConfirm: () -> Unit,
    override val onBack: () -> Unit,
) : BaseLoginScreenComponent, ComponentContext by componentContext