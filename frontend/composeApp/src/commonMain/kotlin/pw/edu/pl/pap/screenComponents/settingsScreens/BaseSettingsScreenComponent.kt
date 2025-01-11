package pw.edu.pl.pap.screenComponents.settingsScreens

import com.arkivanov.decompose.ComponentContext
import pw.edu.pl.pap.screenComponents.BaseComponent

interface BaseSettingsScreenComponent : ComponentContext, BaseComponent {
    val onBack: () -> Unit

    fun setupInputFields() {}
    fun onConfirmClicked() {}
}