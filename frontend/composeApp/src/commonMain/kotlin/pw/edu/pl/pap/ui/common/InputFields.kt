package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import pw.edu.pl.pap.data.InputFieldData



@Composable
fun InputFields(inputFieldsData: List<InputFieldData>) {
    Box(
        modifier = Modifier.offset(x = 0.dp, y = 100.dp)
    ){
        LazyColumn {
            items(inputFieldsData) {
                data -> createField(data)
            }
        }
    }
}

@Composable
fun createField(data: InputFieldData) {
//    println("Parameter passed: " + data.expense.value.price)
    println("Composing $data")
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(50.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = data.title)
            TextField(
                value = data.parameter.value,
                onValueChange = {newParameter -> data.onChange(newParameter)},
                keyboardOptions = data.keyboardOptions ?: KeyboardOptions.Default
            )
        }
    }
}