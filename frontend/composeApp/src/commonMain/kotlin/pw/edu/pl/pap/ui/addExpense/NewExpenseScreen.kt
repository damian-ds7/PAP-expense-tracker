package pw.edu.pl.pap.ui.addExpense

import androidx.compose.runtime.*
import pw.edu.pl.pap.data.Record
import pw.edu.pl.pap.data.User
import pw.edu.pl.pap.data.InputFieldData
import pw.edu.pl.pap.viewmodel.NewExpenseViewModel
import pw.edu.pl.pap.apiclient.NewExpenseApiClient

@Composable
fun NewExpenseScreen(viewModel: NewExpenseViewModel, onClose: () -> Unit){
    var addButtonClicked by remember { mutableStateOf(false) }

    Header()
    InputFields(viewModel.inputFieldsData)

    AddButton(addButtonClicked, onUpdate = {addButtonClicked = !addButtonClicked})


    if (addButtonClicked) {
        viewModel.expenseConfirmed(onClose = onClose)
    }
}