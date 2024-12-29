package pw.edu.pl.pap.screenComponents.loginSystem

import com.arkivanov.decompose.ComponentContext
import pw.edu.pl.pap.api.authApi.UserAuthApi
import pw.edu.pl.pap.screenComponents.BaseComponent

interface BaseLoginScreenComponent : BaseComponent, ComponentContext {
    val apiClient: UserAuthApi
    val onConfirm: () -> Unit
    val onBack: () -> Unit
    val setToken: (String) -> Unit

    fun setupInputFields() {}
    fun confirm() {}
}