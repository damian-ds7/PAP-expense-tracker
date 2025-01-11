package pw.edu.pl.pap.screenComponents.loginSystem

import com.arkivanov.decompose.ComponentContext
import pw.edu.pl.pap.screenComponents.BaseComponent

interface BaseLoginScreenComponent : BaseComponent, ComponentContext {
    val onConfirm: () -> Unit
    val onBack: () -> Unit

    fun setupInputFields() {}
    fun confirm() {}
}