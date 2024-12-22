package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import pw.edu.pl.pap.data.uiSetup.inputFields.DropdownListData
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.TextFieldData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.window.Popup
import kotlinx.datetime.LocalDate
import androidx.compose.ui.res.painterResource
import pw.edu.pl.pap.data.uiSetup.inputFields.DatePickerData


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
            if(data.isDropdownList){
                createDropdownList(data.dropdownListData!!)
            } else if(data.isDatePicker) {
                createDatePicker(data.datePickerData!!)
            } else if(data.isPassword) {
                createPasswordField(data.textFieldData!!)
            } else {
                createTextField(data.textFieldData!!)
            }
        }
    }
}

@Composable
fun createTextField(
    data: TextFieldData
){
    TextField(
        value = data.parameter.value,
        onValueChange = {newParameter -> data.onChange(newParameter)},
        keyboardOptions = data.keyboardOptions ?: KeyboardOptions.Default
    )
}

@Composable
fun createDropdownList(
    data: DropdownListData
){
    var showDropdown by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .width(250.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDropdown = true },
            contentAlignment = Alignment.Center
        )
        {
            Text(text = data.itemList[data.selectedIndex.value], modifier = Modifier.padding(3.dp))
        }

        // dropdown list
        Box() {
            if (showDropdown) {
                Popup(
                    alignment = Alignment.TopCenter,
                    onDismissRequest = { showDropdown = false }
                ) {

                    Column(
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .width(250.dp)
                            .background(Color.Gray)
                            .verticalScroll(state = scrollState)
                            .border(width = 1.dp, color = Color.Gray),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        data.itemList.onEachIndexed { index, item ->
                            if (index != 0) {
                                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        data.onItemClick(index)
                                        showDropdown = !showDropdown
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item)
                            }
                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createDatePicker(
    data: DatePickerData
){
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate>(data.date.value) }
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = Modifier
            .width(250.dp)
            .clickable { showDatePicker = true },
        contentAlignment = Alignment.Center
    )
    {
        Text(text = data.date.value.toString(), modifier = Modifier.padding(3.dp))
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {showDatePicker = false},
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        data.onDateConfirm(millis)
                    }
                    showDatePicker = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            },
            content = {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = true // Allows toggling between calendar and text input
                )
            }
        )
    }

}

@Composable
fun createPasswordField(
    data: TextFieldData
){
    var visibility by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = data.parameter.value,
        onValueChange = {newParameter -> data.onChange(newParameter)},
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        keyboardOptions = data.keyboardOptions ?: KeyboardOptions.Default,
        trailingIcon = {
            TextButton(onClick = { visibility = !visibility }) {
                Text(if (visibility) "Hide" else "Show")
            }
        }
    )
}