package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


class ServerAdressScreenComponent (
    baseSettingsScreenComponent: BaseSettingsScreenComponent
) : BaseSettingsScreenComponentImpl(baseSettingsScreenComponent) {


    private var serverAddress: MutableState<String> = mutableStateOf(apiService.getCurrentUrl())


    private fun updateUrl() {
        apiService.updateBaseUrl(serverAddress.value)
    }
}