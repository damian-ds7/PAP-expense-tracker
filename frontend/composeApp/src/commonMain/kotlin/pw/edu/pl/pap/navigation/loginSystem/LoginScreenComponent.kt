package pw.edu.pl.pap.navigation.loginSystem

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.data.inputFields.InputFieldData
import pw.edu.pl.pap.data.inputFields.TextFieldData

class LoginScreenComponent(
    componentContext: ComponentContext,
    apiService: ApiService,
    coroutineScope: CoroutineScope,
    onConfirm: () -> Unit,
    setToken: (String) -> Unit
) : BaseLoginScreenComponent(componentContext, apiService, coroutineScope, onConfirm, setToken){

    override fun confirm() {
        //TODO push and wait for response
        //TODO set token
        onConfirm()
    }
}