package pw.edu.pl.pap.data.inputFields

data class InputFieldData(
    val title: String,
    val isDropdownList: Boolean = false,
    val isDatePicker: Boolean = false,
    val isPassword: Boolean = false,

    val datePickerData: DatePickerData? = null,
    val textFieldData: TextFieldData? = null,
    val dropdownListData: DropdownListData? = null,
)