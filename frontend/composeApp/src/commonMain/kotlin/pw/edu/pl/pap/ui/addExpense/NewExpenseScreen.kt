package pw.edu.pl.pap.ui.addExpense

import androidx.compose.runtime.*
//import pw.edu.pl.pap.data.Record
//import pw.edu.pl.pap.data.User

@Composable
fun NewExpenseScreen(onClose: () -> Unit){
    var addButtonClicked by remember { mutableStateOf(false) }
//    var newRecord by remember { mutableStateOf(Record(0, 0.toFloat(),0))}


    Header()
    InputFields()

    AddButton(addButtonClicked, onUpdate = {addButtonClicked = !addButtonClicked})


    if (addButtonClicked) {
        //add to database
        onClose()
    }
}