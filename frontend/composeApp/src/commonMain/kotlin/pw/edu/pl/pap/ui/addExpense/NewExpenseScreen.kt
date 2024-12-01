package pw.edu.pl.pap.ui.addExpense

import androidx.compose.runtime.*
import pw.edu.pl.pap.data.Record
import pw.edu.pl.pap.data.User
import pw.edu.pl.pap.data.InputFieldData
import pw.edu.pl.pap.viewmodel.NewExpenseViewModel
import pw.edu.pl.pap.apiclient.NewExpenseApiClient

@Composable
fun NewExpenseScreen(onClose: () -> Unit){
    var addButtonClicked by remember { mutableStateOf(false) }
    var viewModel by remember { mutableStateOf(NewExpenseViewModel(NewExpenseApiClient()))}




    Header()
    InputFields(viewModel.inputFieldsData)

    AddButton(addButtonClicked, onUpdate = {addButtonClicked = !addButtonClicked})


    if (addButtonClicked) {
        //add to database
        onClose()
    }
}