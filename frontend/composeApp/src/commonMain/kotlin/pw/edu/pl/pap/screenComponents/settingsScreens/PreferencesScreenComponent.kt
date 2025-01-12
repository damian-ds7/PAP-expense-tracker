package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.Preferences
import pw.edu.pl.pap.data.databaseAssociatedData.UpdatedUserData
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.ConfigRepository
import pw.edu.pl.pap.repositories.data.UserRepository

class PreferencesScreenComponent(
    baseSettingsScreenComponent: BaseSettingsScreenComponent
) : BaseSettingsScreenComponentImpl(baseSettingsScreenComponent) {

    private val configRepository: ConfigRepository by inject()
    private val userRepository: UserRepository by inject()

    private val currencies = configRepository.currencies.value.map { it.symbol }
    private var currencyIndex: MutableState<Int> = mutableStateOf(configRepository.currencies.value.map { it.symbol }.indexOf(userRepository.currentPreferences.value!!.currencySymbol))

    private val methodsOfPayment = configRepository.paymentMethods.value.map { it.name }
    private var methodOfPaymentIndex: MutableState<Int> = mutableStateOf(configRepository.paymentMethods.value.map { it.name }.indexOf(userRepository.currentPreferences.value!!.methodOfPayment))

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

    override suspend fun postChanges() {
        val updatedPreferences = Preferences(
            currencies[currencyIndex.value],
            methodsOfPayment[methodOfPaymentIndex.value],
            userRepository.currentPreferences.value!!.language
        )
        userRepository.updatePreferences(updatedPreferences)
    }


    override fun setupInputFields() {
        _inputFieldsData.clear()
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