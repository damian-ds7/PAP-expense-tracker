package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.screenComponents.BaseComponent

open class BaseGroupEditScreenComponent(
    baseComponent: BaseComponent,
    val onDismiss: () -> Unit,
    val onSave: () -> Unit
) : BaseComponent by baseComponent {

    protected val groupRepository: GroupRepository by inject()
    protected val group = groupRepository.currentUserGroup.value

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
