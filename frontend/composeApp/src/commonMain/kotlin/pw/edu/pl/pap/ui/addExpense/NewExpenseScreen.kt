package pw.edu.pl.pap.ui.addExpense

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.singleExpense.NewExpenseScreenComponent
import pw.edu.pl.pap.ui.common.ConfirmOrBackButtonRow
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields

@Composable
fun NewExpenseScreen(
    component: NewExpenseScreenComponent
    ){
    val scope = rememberCoroutineScope()

    Header("New expense")
    InputFields(component.inputFieldsData)

    ConfirmOrBackButtonRow(
        text = "ADD",
        onBack = { scope.launch { component.onBack() } },
        onConfirm = { scope.launch { component.confirm() } },
        isConfirmEnabled = component.canConfirm
    )
}