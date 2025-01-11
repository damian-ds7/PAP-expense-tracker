package pw.edu.pl.pap.screenComponents.chartsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.Category
import pw.edu.pl.pap.data.databaseAssociatedData.PaymentMethod
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.ChartsRepository
import pw.edu.pl.pap.repositories.data.ConfigRepository
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.util.listFunctions.getIndicesFromItems
import pw.edu.pl.pap.util.listFunctions.getItemsFromIndices

class ChartsFilterScreenComponent(
    baseComponent: BaseComponent, val onDismiss: () -> Unit, val onSave: () -> Unit
) : BaseComponent by baseComponent {

    private val chartsRepository: ChartsRepository by inject()
    private val groupRepository: GroupRepository by inject()
    private val configRepository: ConfigRepository by inject()


    private val _inputFieldsData = MutableStateFlow<List<InputFieldData>>(emptyList())
    val inputFieldsData: StateFlow<List<InputFieldData>> get() = _inputFieldsData

    private var selectedCategories: SnapshotStateList<Int>? = null
    private var selectedEmails: SnapshotStateList<Int>? = null
    private var selectedMethods: SnapshotStateList<Int>? = null
    private lateinit var selectedKeyPattern: MutableState<Int>

    private lateinit var users: List<User>
    private val methods: List<PaymentMethod> = configRepository.paymentMethods.value
    private val categories: List<Category> = configRepository.categories.value
    private val patternKeys: List<String> = configRepository.keyPatterns.value


    private val currentKeyPattern = chartsRepository.keyPattern.value
    private val filterParams = chartsRepository.chartFilters

    init {
        coroutineScope.launch {
            users = groupRepository.getUsersInCurrentGroup()
            initializeSelected()
            initializeInputFieldsData()

        }
    }

    private fun initializeSelected() {
        selectedCategories =
            getIndicesFromItems(categories, filterParams.value.categoryNames, Category::name)?.toMutableStateList()
        selectedEmails = getIndicesFromItems(users, filterParams.value.emails, User::email)?.toMutableStateList()
        selectedMethods =
            getIndicesFromItems(methods, filterParams.value.methods, PaymentMethod::name)?.toMutableStateList()
        selectedKeyPattern = mutableStateOf(patternKeys.indexOf(currentKeyPattern))

    }

    private fun initializeInputFieldsData() {
        _inputFieldsData.value = listOf(
            InputFieldData.DropdownListData(
                title = "Group by",
                itemList = patternKeys,
                selectedIndex = selectedKeyPattern,
                onItemClick = { selectedKeyPattern.value = it }),
            InputFieldData.CheckboxData(
                title = "Categories",
                itemList = categories.map { it.name },
                selectedIndices = selectedCategories,
                onConfirm = { selectedCategories = it?.toMutableStateList() }),
            InputFieldData.CheckboxData(
                title = "Users",
                itemList = users.map { "${it.name} ${it.surname}" },
                selectedIndices = selectedEmails,
                onConfirm = { selectedEmails = it?.toMutableStateList() }),
            InputFieldData.CheckboxData(
                title = "Payment methods",
                itemList = methods.map { it.name },
                selectedIndices = selectedMethods,
                onConfirm = { selectedMethods = it?.toMutableStateList() })
        )
    }

    fun confirm() {
        val newFilterParams = filterParams.value.copy(
            categoryNames = getItemsFromIndices(categories, selectedCategories, Category::name),
            emails = getItemsFromIndices(users, selectedEmails, User::email),
            methods = getItemsFromIndices(methods, selectedMethods, PaymentMethod::name)
        )
        chartsRepository.updateFilterParams(newFilterParams)
        chartsRepository.updateKeyPattern(patternKeys[selectedKeyPattern.value])
        onSave()
    }

    //TODO find a way to force recomposition after reset
//    fun reset() {
//        chartsRepository.resetFilters()
//        initializeSelected()
//        initializeInputFieldsData()
//    }
}