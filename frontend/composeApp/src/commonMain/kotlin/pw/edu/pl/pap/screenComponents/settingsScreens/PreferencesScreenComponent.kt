package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.ConfigRepository

class PreferencesScreenComponent(
    baseSettingsScreenComponent: BaseSettingsScreenComponent
) : BaseSettingsScreenComponentImpl(baseSettingsScreenComponent) {

    private val configRepository: ConfigRepository by inject()

    private val currencies = configRepository.currencies.value.map { it.symbol }

    var currencyIndex: MutableState<Int> = mutableStateOf(0)
    //TODO fetch currency

    private val methodsOfPayment = configRepository.paymentMethods.value.map { it.name }

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
                InputFieldData.DropdownListData(
                    title = "Currency",
                    itemList = currencies,
                    selectedIndex = currencyIndex,
                    onItemClick = {
                        currencyIndex.value = it
                    }
                ),
                InputFieldData.DropdownListData(
                    title = "Method of payment: ",
                    itemList = methodsOfPayment,
                    selectedIndex = methodOfPaymentIndex,
                    onItemClick = {
                        coroutineScope.launch { methodOfPaymentIndex.value = it }
                    }
                ),
            )
        )
    }
}