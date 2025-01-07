package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.screenComponents.BaseScreenComponent

open class BaseGroupEditScreenComponent(
    baseComponent: BaseScreenComponent,
    val onDismiss: () -> Unit,
    val onSave: () -> Unit
) : BaseScreenComponent by baseComponent {

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    protected open var name: MutableState<String> = mutableStateOf("")

    val canConfirm by derivedStateOf { name.value.isNotEmpty() }

    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData.TextFieldData(
                    title = "Name: ",
                    parameter = name,
                    onChange = {
                        coroutineScope.launch { name.value = it }
                    }
                )
            )
        )
    }

    open fun confirm() {
        throw NotImplementedError("Subclasses must override confirm")
    }
}
