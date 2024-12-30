package pw.edu.pl.pap.screenComponents.settingsScreens

import com.arkivanov.decompose.ComponentContext
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.screenComponents.BaseComponent

interface BaseSettingsScreenComponent : ComponentContext, BaseComponent {
    val apiService: ApiService
    val onBack: () -> Unit

    fun setupInputFields() {}
    fun onConfirmClicked() {}
}