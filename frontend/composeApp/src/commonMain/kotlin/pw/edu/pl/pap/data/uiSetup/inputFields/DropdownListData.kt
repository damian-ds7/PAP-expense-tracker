package pw.edu.pl.pap.data.uiSetup.inputFields

import androidx.compose.runtime.MutableState

data class DropdownListData (
    val itemList: List<String>,
    val selectedIndex: MutableState<Int>,
    val onItemClick: (Int) -> Unit
)