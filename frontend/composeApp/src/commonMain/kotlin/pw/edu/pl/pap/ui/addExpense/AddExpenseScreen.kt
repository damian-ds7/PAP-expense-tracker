package pw.edu.pl.pap.ui.addExpense

import androidx.compose.runtime.*

@Composable
fun AddExpenseScreen(onClose: () -> Unit){
    var addButtonClicked by remember { mutableStateOf(false) }



    AddButton(addButtonClicked, onUpdate = {addButtonClicked = !addButtonClicked})


    if (addButtonClicked) {
        //add to database
        onClose()
    }
}