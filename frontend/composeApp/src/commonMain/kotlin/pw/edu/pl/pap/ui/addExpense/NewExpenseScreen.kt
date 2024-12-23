package pw.edu.pl.pap.ui.addExpense

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.singleExpense.NewExpenseScreenComponent
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields

@Composable
fun NewExpenseScreen(
    component: NewExpenseScreenComponent
    ){
    val scope = rememberCoroutineScope()

    Header("New expense")
    component.setupInputFields()
    InputFields(component.inputFieldsData)

    NewExpenseButtonRow(
        onBack = { scope.launch { component.onDismiss() } },
        onConfirm = { scope.launch { component.confirm() } },
        isConfirmEnabled = component.canConfirm
    )

}