package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.util.constants.horizontalPadding
import pw.edu.pl.pap.util.constants.verticalPadding
import pw.edu.pl.pap.util.dateFunctions.dateToMillis


@Composable
fun InputFields(inputFieldsData: List<InputFieldData>) {
    Box(
        modifier = Modifier.offset(x = 0.dp, y = 100.dp)
    ) {
        LazyColumn {
            items(inputFieldsData) { data ->
                createField(data)
            }
        }
    }
}

@Composable
private fun createField(data: InputFieldData) {
    if (data is InputFieldData.ButtonData) {
        createClickableCard(data)
    } else {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
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
                when (data) {
                    is InputFieldData.DropdownListData -> createDropdownList(data)
                    is InputFieldData.DatePickerData -> createDatePicker(data)
                    is InputFieldData.TextFieldData -> {
                        if (data.password) {
                            createPasswordField(data)
                        } else {
                            createTextField(data)
                        }
                    }

                    is InputFieldData.CheckboxData -> createCheckBox(data)
                    else -> return@Row
                }
            }
        }
    }
}

@Composable
private fun createTextField(
    data: InputFieldData.TextFieldData
) {
    TextField(
        value = data.parameter.value,
        onValueChange = { newParameter -> data.onChange(newParameter) },
        keyboardOptions = data.keyboardOptions ?: KeyboardOptions.Default
    )
}

@Composable
private fun createDropdownList(
    data: InputFieldData.DropdownListData
) {
    var showDropdown by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .width(250.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDropdown = true },
            contentAlignment = Alignment.Center
        )
        {
            Text(text = data.itemList[data.selectedIndex.value], modifier = Modifier.padding(3.dp))

            Row(modifier = Modifier.align(Alignment.Center)) {
                // dropdown list
                DropdownMenu(
                    expanded = showDropdown, onDismissRequest = { showDropdown = false }) {
                    data.itemList.forEachIndexed { idx, item ->
                        DropdownMenuItem(text = { Text(item) }, onClick = {
                            data.onItemClick(idx)
                            showDropdown = false
                        })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun createDatePicker(
    data: InputFieldData.DatePickerData
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDate by remember { mutableStateOf(data.date.value) }
    val datePickerState = rememberDatePickerState(dateToMillis(selectedDate))

    Box(
        modifier = Modifier
            .width(250.dp)
            .clickable { showDatePicker = true },
        contentAlignment = Alignment.Center
    )
    {
        Text(text = selectedDate.toString(), modifier = Modifier.padding(3.dp))
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
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
private fun createPasswordField(
    data: InputFieldData.TextFieldData
) {
    var visibility by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = data.parameter.value,
        onValueChange = { newParameter -> data.onChange(newParameter) },
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

@Composable
private fun createClickableCard(data: InputFieldData.ButtonData) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .height(50.dp)
            .clickable { data.onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = if (data.isColored) {
            CardDefaults.cardColors(containerColor = data.customColor)
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = data.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun createCheckBox(data: InputFieldData.CheckboxData) {
    val checkedStates = remember {
        mutableStateListOf<Boolean>().apply {
            val result = List(data.itemList.size) { index ->
                data.selectedIndices?.contains(index) ?: true
            }
            addAll(result)
        }
    }

    val parentStateAndText by derivedStateOf {
        when {
            checkedStates.all { it } -> ToggleableState.On to "All"
            checkedStates.none { it } -> ToggleableState.Off to "None"
            else -> ToggleableState.Indeterminate to "Selected ${checkedStates.count { it }}"
        }
    }

    val (parentState, text) = parentStateAndText

//    val (parentState, text) by derivedStateOf {
//        when {
//            checkedStates.all { it } -> ToggleableState.On to "All"
//            checkedStates.none { it } -> ToggleableState.Off to "None"
//            else -> ToggleableState.Indeterminate to "Selected ${checkedStates.count { it }}"
//        }
//    }

    var showDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(250.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDropdown = true },
            contentAlignment = Alignment.Center
        )
        {
            Text(text = text, modifier = Modifier.padding(3.dp))

            DropdownMenu(
                expanded = showDropdown,
                modifier = Modifier.padding(8.dp),
                onDismissRequest = {
                    val selectedIndices = when (parentState) {
                        ToggleableState.On -> null
                        ToggleableState.Off -> {
                            checkedStates.fill(true)
                            null
                        }
                        else -> checkedStates.mapIndexedNotNull { idx, value ->
                            if (value) idx else null
                        }
                    }
                    data.onConfirm(selectedIndices)
                    showDropdown = false
                }) {

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        TriStateCheckbox(
                            state = parentState,
                            onClick = {
                                val newState = parentState != ToggleableState.On
                                checkedStates.forEachIndexed { index, _ ->
                                    checkedStates[index] = newState
                                }
                            }
                        )
                        Text("Select all")
                    }
                    checkedStates.forEachIndexed { index, checked ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { isChecked ->
                                    checkedStates[index] = isChecked
                                }
                            )
                            Text(data.itemList[index])
                        }
                    }
                }
            }
        }
    }

}