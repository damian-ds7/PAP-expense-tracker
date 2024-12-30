package pw.edu.pl.pap.screenComponents.settingsScreens

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.api.authApi.UserAuthApi
import pw.edu.pl.pap.screenComponents.loginSystem.BaseLoginScreenComponent


open class SettingsScreenComponentHelper(
    componentContext: ComponentContext,
    override val apiService: ApiService,
    override val coroutineScope: CoroutineScope,
    override val onBack: () -> Unit
) : BaseSettingsScreenComponent, ComponentContext by componentContext