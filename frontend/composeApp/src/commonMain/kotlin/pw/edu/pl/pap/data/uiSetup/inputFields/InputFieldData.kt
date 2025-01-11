package pw.edu.pl.pap.data.uiSetup.inputFields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import kotlinx.datetime.LocalDate

sealed class InputFieldData(open val title: String) {
    data class DropdownListData (
        override val title: String,
        val itemList: List<String>,
        val selectedIndex: MutableState<Int>,
        val onItemClick: (Int) -> Unit,
    ) : InputFieldData(title)

    data class DatePickerData (
        override val title: String,
        val date: MutableState<LocalDate>,
        val onDateConfirm: (LocalDate) -> Unit
    ) : InputFieldData(title)

    data class TextFieldData (
        override val title: String,
        var parameter: MutableState<String>,
        val onChange: (String) -> Unit,
        val keyboardOptions: KeyboardOptions? = null,  // Optional keyboard options, used for number only keyboard
        val password: Boolean = false,
        val textAlign: TextAlign = TextAlign.Center
    ) : InputFieldData(title)

    data class ButtonData (
        override val title: String,
        val isColored: Boolean = false,
        val customColor: Color = Color.Red.copy(alpha = 0.2f),
        val onClick: () -> Unit,
    ) : InputFieldData(title)

    data class CheckboxData (
        override val title: String,
        val itemList: List<String>,
        val selectedIndices: SnapshotStateList<Int>? = null,
        val onConfirm: (List<Int>?) -> Unit
    ) : InputFieldData(title)

    data class UserBalanceButtonData (
        override val title: String,
        val balance: Float,
        val onClick: () -> Unit,
    ) : InputFieldData(title)
}
