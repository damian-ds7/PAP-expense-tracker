package pw.edu.pl.pap.screenComponents.settingsScreens

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope


open class SettingsScreenComponentHelper(
    componentContext: ComponentContext,
    override val coroutineScope: CoroutineScope,
    override val onBack: () -> Unit
) : BaseSettingsScreenComponent, ComponentContext by componentContext