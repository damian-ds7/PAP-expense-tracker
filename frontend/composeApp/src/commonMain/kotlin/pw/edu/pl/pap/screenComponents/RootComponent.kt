package pw.edu.pl.pap.screenComponents

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.di.apiModule
import pw.edu.pl.pap.di.loadAdditionalModules
import pw.edu.pl.pap.di.repoModule
import pw.edu.pl.pap.repositories.data.ConfigRepository
import pw.edu.pl.pap.repositories.data.UserRepository
import pw.edu.pl.pap.screenComponents.chartsScreens.ChartsFilterScreenComponent
import pw.edu.pl.pap.screenComponents.groupScreens.EditGroupScreenComponent
import pw.edu.pl.pap.screenComponents.groupScreens.InvitationsScreenComponent
import pw.edu.pl.pap.screenComponents.groupScreens.MemberScreenComponent
import pw.edu.pl.pap.screenComponents.groupScreens.NewGroupScreenComponent
import pw.edu.pl.pap.screenComponents.loginSystem.*
import pw.edu.pl.pap.screenComponents.mainScreens.ChartsScreenComponent
import pw.edu.pl.pap.screenComponents.mainScreens.GroupScreenComponent
import pw.edu.pl.pap.screenComponents.mainScreens.HomeScreenComponent
import pw.edu.pl.pap.screenComponents.mainScreens.SettingsScreenComponent
import pw.edu.pl.pap.screenComponents.settingsScreens.*
import pw.edu.pl.pap.screenComponents.singleExpense.ExpenseDetailsScreenComponent
import pw.edu.pl.pap.screenComponents.singleExpense.NewExpenseScreenComponent
import pw.edu.pl.pap.ui.navBar.NavBarItem

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<Configuration>()
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private fun loadConfig() {
        val configRepository: ConfigRepository by inject()
        val userRepository: UserRepository by inject()
        coroutineScope.launch {
            configRepository.loadConfig()
            userRepository.getCurrentUserInfo()
            userRepository.getCurrentPreferences()
        }
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object LogInSignUpSelectionScreen : Configuration()

        @Serializable
        data object LogInScreen : Configuration()

        @Serializable
        data object SignUpScreen : Configuration()

        @Serializable
        data object HomeScreen : Configuration()

        @Serializable
        data class NewExpenseScreen(val userGroup: UserGroup) : Configuration()

        @Serializable
        data class ExpenseDetailsScreen(val expense: Expense) : Configuration()

        @Serializable
        data object ChartsScreen : Configuration()

        @Serializable
        data object ChartsFilterScreen : Configuration()

        data object GroupScreen : Configuration()

        @Serializable
        data class MemberScreen(val user: User) : Configuration()

        @Serializable
        data object InvitationsScreen : Configuration()

        @Serializable
        data object NewGroupScreen : Configuration()

        @Serializable
        data object EditGroupScreen : Configuration()

        @Serializable
        data object SettingsScreen : Configuration()

        @Serializable
        data object ServerAddressScreen : Configuration()

        @Serializable
        data object UserPersonalDataScreen : Configuration()

        @Serializable
        data object ChangePasswordScreen : Configuration()

        @Serializable
        data object PreferencesScreen : Configuration()
    }

    private fun createLoginScreenComponent(
        componentContext: ComponentContext
    ): BaseLoginScreenComponent {
        return LoginScreenComponentHelper(
            componentContext = componentContext,
            coroutineScope = coroutineScope,
            onConfirm = {
                loadAdditionalModules(apiModule, repoModule)
                loadConfig()
                navigation.replaceAll(Configuration.HomeScreen)
            },
            onBack = { navigation.pop() },
        )
    }

    private fun createMainScreenComponent(
        componentContext: ComponentContext
    ): BaseComponent = BaseComponentImpl(
        componentContext = componentContext, coroutineScope = coroutineScope
    )

    private fun createSettingsScreenComponent(
        componentContext: ComponentContext
    ): BaseSettingsScreenComponent = SettingsScreenComponentHelper(
        componentContext = componentContext, coroutineScope = coroutineScope, onBack = {
            navigation.pop()
            navBarItemClicked(NavBarItem.Settings)
        })

    sealed class Child {
        data class LogInSignUpSelectionScreen(val component: SelectionLoginSignupScreenComponent) : Child()
        data class LogInScreen(val component: LoginScreenComponent) : Child()
        data class SignUpScreen(val component: SignupScreenComponent) : Child()

        data class HomeScreen(val component: HomeScreenComponent) : Child()
        data class NewExpenseScreen(val component: NewExpenseScreenComponent) : Child()
        data class ExpenseDetailsScreen(val component: ExpenseDetailsScreenComponent) : Child()

        data class ChartsScreen(val component: ChartsScreenComponent) : Child()

        data class ChartsFilterScreen(val component: ChartsFilterScreenComponent) : Child()

        data class GroupScreen(val component: GroupScreenComponent) : Child()

        data class SettingsScreen(val component: SettingsScreenComponent) : Child()

        data class MemberScreen(val component: MemberScreenComponent) : Child()
        data class NewGroupScreen(val component: NewGroupScreenComponent) : Child()
        data class EditGroupScreen(val component: EditGroupScreenComponent) : Child()
        data class InvitationsScreen(val component: InvitationsScreenComponent) : Child()

        data class ServerAddressScreen(val component: ServerAdressScreenComponent) : Child()
        data class UserPersonalDataScreen(val component: UserPersonalDataScreenComponent) : Child()
        data class ChangePasswordScreen(val component: ChangePasswordScreenComponent) : Child()
        data class PreferencesScreen(val component: PreferencesScreenComponent) : Child()
    }


    private val _activeNavBarItem = MutableStateFlow<NavBarItem>(NavBarItem.Home)
    val activeNavBarItem: StateFlow<NavBarItem> get() = _activeNavBarItem

    fun navBarItemClicked(item: NavBarItem) {
        _activeNavBarItem.value = item
        when (item) {
            NavBarItem.Home -> navigation.bringToFront(Configuration.HomeScreen)
            NavBarItem.Charts -> navigation.bringToFront(Configuration.ChartsScreen)
            NavBarItem.Groups -> navigation.bringToFront(Configuration.GroupScreen)
            NavBarItem.Settings -> navigation.bringToFront(Configuration.SettingsScreen)
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child {
        return when (configuration) {
            is Configuration.LogInSignUpSelectionScreen -> {
                Child.LogInSignUpSelectionScreen(
                    component = SelectionLoginSignupScreenComponent(
                        componentContext = componentContext,
                        onLogInButtonClicked = {
                            navigation.pushNew(Configuration.LogInScreen)
                        },
                        onSignupButtonClicked = {
                            navigation.pushNew(Configuration.SignUpScreen)
                        })
                )
            }

            is Configuration.LogInScreen -> {
                Child.LogInScreen(
                    component = LoginScreenComponent(
                        baseScreenComponent = createLoginScreenComponent(componentContext)
                    )
                )
            }

            is Configuration.SignUpScreen -> {
                Child.SignUpScreen(
                    component = SignupScreenComponent(
                        baseScreenComponent = createLoginScreenComponent(componentContext)
                    )
                )
            }

            is Configuration.HomeScreen -> {
                Child.HomeScreen(
                    component = HomeScreenComponent(
                        baseScreenComponent = createMainScreenComponent(componentContext),
                        onAddExpenseButtonClicked = { userGroup ->
                            navigation.pushNew(Configuration.NewExpenseScreen(userGroup))
                        },
                        onExpenseClick = { expense ->
                            navigation.pushNew(Configuration.ExpenseDetailsScreen(expense))
                        })
                )
            }

            is Configuration.NewExpenseScreen -> Child.NewExpenseScreen(
                component = NewExpenseScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onBack = { navigation.pop() },
                    currentUserGroup = configuration.userGroup
                )
            )

            is Configuration.ExpenseDetailsScreen -> Child.ExpenseDetailsScreen(
                component = ExpenseDetailsScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onBack = { navigation.pop() },
                    expense = configuration.expense
                )
            )

            is Configuration.ChartsScreen -> Child.ChartsScreen(
                ChartsScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onFilterClick = { navigation.pushNew(Configuration.ChartsFilterScreen)
                    })
            )

            is Configuration.ChartsFilterScreen -> {
                Child.ChartsFilterScreen(
                    ChartsFilterScreenComponent(
                        baseComponent = createMainScreenComponent(componentContext),
                        onDismiss = { navigation.pop() },
                        onSave = {
                            navigation.pop()
                            (childStack.value.active.instance as Child.ChartsScreen).component.updateNavigationState(
                                ChartsScreenComponent.NavigationState.LoadData
                            )
                        })
                )
            }

            is Configuration.GroupScreen -> Child.GroupScreen(
                component = GroupScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onUserClicked = { userGroup, user ->
                        navigation.pushNew(Configuration.MemberScreen(user))
                    },
                    onInvitationsClicked = { navigation.pushNew(Configuration.InvitationsScreen) },
                    onNewGroupClicked = { navigation.pushNew(Configuration.NewGroupScreen) },
                    onEditGroupClicked = { navigation.pushNew(Configuration.EditGroupScreen) },
                )
            )

            is Configuration.MemberScreen -> Child.MemberScreen(
                component = MemberScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onBack = { navigation.pop() },
                    user = configuration.user,
                )
            )

            is Configuration.EditGroupScreen -> Child.EditGroupScreen(
                component = EditGroupScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onDismiss = { navigation.pop() },
//                    onSave = {
//                        navigation.pop()
//                        (childStack.value.active.instance as Child.HomeScreen).component.updateNavigationState(
//                            HomeScreenComponent.NavigationState.FromExpenseDetailsScreenEdit(configuration.expense)
//                        )
//                    },
//                    onDelete = {
//                        navigation.pop()
//                        (childStack.value.active.instance as Child.HomeScreen).component.updateNavigationState(
//                            HomeScreenComponent.NavigationState.FromExpenseDetailsScreenDelete(configuration.expense)
//                        )
//                    },
                    onSave = {},
                    onDelete = {},
                    //TODO
                )
            )

            is Configuration.NewGroupScreen -> Child.NewGroupScreen(
                component = NewGroupScreenComponent(
                    baseComponent = createMainScreenComponent(
                        componentContext
                    ),
                    onDismiss = { navigation.pop() },
                    onSave = {}
//                            onSave = {
//                        navigation.pop()
//                        (childStack.value.active.instance as Child.HomeScreen).component.updateNavigationState(
//                            HomeScreenComponent.NavigationState.FromNewExpenseScreen
//                        )
//                    },
                    //TODO
                ))

            is Configuration.InvitationsScreen -> Child.InvitationsScreen(
                InvitationsScreenComponent(
                    onDismiss = { navigation.pop() },
                    baseComponent = createMainScreenComponent(componentContext)
                )
            )

            is Configuration.SettingsScreen -> Child.SettingsScreen(
                SettingsScreenComponent(
                    onLogOut = { navigation.replaceAll(Configuration.LogInSignUpSelectionScreen) },
                    onChangeServerAddressClicked = { navigation.pushNew(Configuration.ServerAddressScreen) },
                    onUserPersonalsClicked = { navigation.pushNew(Configuration.UserPersonalDataScreen) },
                    onChangePasswordClicked = { navigation.pushNew(Configuration.ChangePasswordScreen) },
                    onEditPreferencesClicked = { navigation.pushNew(Configuration.PreferencesScreen) },
                    baseComponent = createMainScreenComponent(componentContext)
                )
            )

            is Configuration.ServerAddressScreen -> {
                Child.ServerAddressScreen(
                    component = ServerAdressScreenComponent(
                        baseSettingsScreenComponent = createSettingsScreenComponent(componentContext)
                    )
                )
            }

            is Configuration.UserPersonalDataScreen -> {
                Child.UserPersonalDataScreen(
                    component = UserPersonalDataScreenComponent(
                        baseSettingsScreenComponent = createSettingsScreenComponent(componentContext),
                        logOut = { navigation.replaceAll(Configuration.LogInSignUpSelectionScreen) }
                    )
                )
            }

            is Configuration.ChangePasswordScreen -> {
                Child.ChangePasswordScreen(
                    component = ChangePasswordScreenComponent(
                        baseSettingsScreenComponent = createSettingsScreenComponent(componentContext)
                    )
                )
            }

            is Configuration.PreferencesScreen -> {
                Child.PreferencesScreen(
                    component = PreferencesScreenComponent(
                        baseSettingsScreenComponent = createSettingsScreenComponent(componentContext)
                    )
                )
            }
        }
    }

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.LogInSignUpSelectionScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )


}