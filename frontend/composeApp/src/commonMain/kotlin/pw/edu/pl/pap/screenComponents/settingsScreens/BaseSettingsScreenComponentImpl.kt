package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.screenComponents.mainScreens.BaseScreenComponent


open class BaseSettingsScreenComponentImpl(
    baseSettingsScreenComponent: BaseSettingsScreenComponent
) : BaseSettingsScreenComponent by baseSettingsScreenComponent {

    protected val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    var showConfirmationDialog: MutableState<Boolean> = mutableStateOf(false)

    open lateinit var confirmationData: ConfirmationDialogConfig

    open override fun setupInputFields() {
        throw NotImplementedError("Subclasses must override confirm")
    }

    open override fun onConfirmClicked() {
        showConfirmationDialog.value = true
    }

    protected open fun postChanges() {
        throw NotImplementedError("Subclasses must override confirm")
    }
}