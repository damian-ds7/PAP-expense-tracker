package pw.edu.pl.pap.screenComponents.loginSystem

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import pw.edu.pl.pap.api.authApi.UserAuthApi

open class LoginScreenComponentHelper(
    componentContext: ComponentContext,
    override val apiClient: UserAuthApi,
    override val coroutineScope: CoroutineScope,
    override val onConfirm: () -> Unit,
    override val onBack: () -> Unit,
    override val setToken: (String) -> Unit
) : BaseLoginScreenComponent, ComponentContext by componentContext