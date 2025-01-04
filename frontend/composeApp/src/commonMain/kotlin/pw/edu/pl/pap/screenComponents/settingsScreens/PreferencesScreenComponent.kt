package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.DropdownListData
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData

class PreferencesScreenComponent (
    baseSettingsScreenComponent: BaseSettingsScreenComponent
) : BaseSettingsScreenComponentImpl(baseSettingsScreenComponent) {

    private val currencies = listOf("PLN", "EUR", "USD")
    //TODO fetch currencies
    var currencyIndex: MutableState<Int> = mutableStateOf(0)
    //TODO fetch currency

    private val methodsOfPayment = listOf("Cash", "Card", "W naturze")
    //TODO fetch methods of payment
    var methodOfPaymentIndex: MutableState<Int> = mutableStateOf(0)
    //TODO fetch method of payment

    override var confirmationData = ConfirmationDialogConfig(
        mainText = "Change Preferences",
        subText = "Are you sure you want to change your preferences?",
        onNo = { showConfirmationDialog.value = false },
        onYes = {
            showConfirmationDialog.value = false
            coroutineScope.launch { postChanges() }
            onBack()
        }
    )

    private fun fetchUserPreferences() {
        runBlocking {
            //TODO
        }
    }

    override fun postChanges() {
        //TODO
    }


    override fun setupInputFields() {
        _inputFieldsData.clear()
        fetchUserPreferences()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Currency",
                    isDropdownList = true,
                    dropdownListData = DropdownListData(
                        itemList = currencies,
                        selectedIndex = currencyIndex,
                        onItemClick = {
                            currencyIndex.value = it
                        }
                    )
                ),
                InputFieldData(
                    title = "Method of payment: ",
                    isDropdownList = true,
                    dropdownListData = DropdownListData(
                        itemList = methodsOfPayment,
                        selectedIndex = methodOfPaymentIndex,
                        onItemClick = {
                            coroutineScope.launch { methodOfPaymentIndex.value = it }
                        }
                    )
                ),
            )
        )
    }
}