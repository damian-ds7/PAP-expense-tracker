package pw.edu.pl.pap.screenComponents

import androidx.compose.runtime.collectAsState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.api.authApi.LoginApi
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.screenComponents.groupScreens.MemberScreenComponent
import pw.edu.pl.pap.screenComponents.loginSystem.*
import pw.edu.pl.pap.screenComponents.mainScreens.*
import pw.edu.pl.pap.screenComponents.settingsScreens.*
import pw.edu.pl.pap.screenComponents.singleExpense.ExpenseDetailsScreenComponent
import pw.edu.pl.pap.screenComponents.singleExpense.NewExpenseScreenComponent
import pw.edu.pl.pap.ui.navBar.NavBarItem

class RootComponent(
    componentContext: ComponentContext, private val baseUrl: String
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    private lateinit var apiService: ApiService
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 3000
        }
        install(HttpCache)
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
        data class GroupScreen(val userGroup: UserGroup) : Configuration()

        @Serializable
        data class MemberScreen(val userGroup: UserGroup, val user: User) : Configuration()

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
    ): BaseLoginScreenComponent = LoginScreenComponentHelper(
        componentContext = componentContext,
        coroutineScope = coroutineScope,
        apiClient = LoginApi(baseUrl, httpClient),
        onConfirm = { navigation.replaceAll(Configuration.HomeScreen) },
        onBack = { navigation.pop() },
        setToken = { newToken -> apiService = ApiService(newToken, httpClient, baseUrl) }
    )

    private fun createMainScreenComponent(
        componentContext: ComponentContext
    ): BaseScreenComponent = BaseScreenComponentImpl(
        componentContext = componentContext,
        apiService = apiService,
        coroutineScope = coroutineScope
    )

    private fun createSettingsScreenComponent(
        componentContext: ComponentContext
    ): BaseSettingsScreenComponent = SettingsScreenComponentHelper(
        componentContext = componentContext,
        apiService = apiService,
        coroutineScope = coroutineScope,
        onBack = {
            navigation.pop()
            navBarItemClicked(NavBarItem.Settings)
        }
    )

    sealed class Child {
        data class LogInSignUpSelectionScreen(val component: SelectionLoginSignupScreenComponent) : Child()
        data class LogInScreen(val component: LoginScreenComponent) : Child()
        data class SignUpScreen(val component: SignupScreenComponent) : Child()

        data class HomeScreen(val component: HomeScreenComponent) : Child()
        data class NewExpenseScreen(val component: NewExpenseScreenComponent) : Child()
        data class ExpenseDetailsScreen(val component: ExpenseDetailsScreenComponent) : Child()

        data class ChartsScreen(val component: ChartsScreenComponent) : Child()
        data class GroupScreen(val component: GroupScreenComponent) : Child()
        data class SettingsScreen(val component: SettingsScreenComponent) : Child()

        data class MemberScreen(val component: MemberScreenComponent) : Child()

        data class ServerAddressScreen(val component: ServerAdressScreenComponent) : Child()
        data class UserPersonalDataScreen(val component: UserPersonalDataScreenComponent) : Child()
        data class ChangePasswordScreen(val component: ChangePasswordScreenComponent) : Child()
        data class PreferencesScreen(val component: PreferencesScreenComponent) : Child()
    }


    private val _activeNavBarItem = MutableStateFlow<NavBarItem>(NavBarItem.Home)
    val activeNavBarItem: StateFlow<NavBarItem> get() = _activeNavBarItem

    // TODO add new screens when ready
    fun navBarItemClicked(item: NavBarItem) {
        _activeNavBarItem.value = item
        when (item) {
            NavBarItem.Home -> navigation.bringToFront(Configuration.HomeScreen)
            NavBarItem.Charts -> navigation.bringToFront(Configuration.ChartsScreen)
            NavBarItem.Groups -> {
                val activeInstance = childStack.value.active.instance
                if (activeInstance is Child.HomeScreen) {
                    val homeComponent = activeInstance.component
                    val userGroup = homeComponent.currentUserGroup.value!!
                    navigation.bringToFront(Configuration.GroupScreen(userGroup))
                } else {
                    // never happens
                }
            }
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
                    onDismiss = { navigation.pop() },
                    onSave = {
                        navigation.pop()
                        (childStack.value.active.instance as Child.HomeScreen).component.updateNavigationState(
                            HomeScreenComponent.NavigationState.FromNewExpenseScreen
                        )
                    },
                    currentUserGroup = configuration.userGroup
                )
            )

            is Configuration.ExpenseDetailsScreen -> Child.ExpenseDetailsScreen(
                component = ExpenseDetailsScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onDismiss = { navigation.pop() },
                    onSave = {
                        navigation.pop()
                        (childStack.value.active.instance as Child.HomeScreen).component.updateNavigationState(
                            HomeScreenComponent.NavigationState.FromExpenseDetailsScreenEdit(configuration.expense)
                        )
                    },
                    onDelete = {
                        navigation.pop()
                        (childStack.value.active.instance as Child.HomeScreen).component.updateNavigationState(
                            HomeScreenComponent.NavigationState.FromExpenseDetailsScreenDelete(configuration.expense)
                        )
                    },
                    expense = configuration.expense
                )
            )

            is Configuration.ChartsScreen -> Child.ChartsScreen(
                ChartsScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                )
            )

            is Configuration.GroupScreen -> Child.GroupScreen(
                component = GroupScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onUserClicked = { userGroup, user ->
                        navigation.pushNew(Configuration.MemberScreen(userGroup, user))
                    },
                    currentUserGroup = configuration.userGroup,
                )
            )

            is Configuration.MemberScreen -> Child.MemberScreen(
                component = MemberScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext),
                    onBack = { navigation.pop() },
                    user = configuration.user,
                    currentUserGroup = configuration.userGroup
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
                        baseSettingsScreenComponent = createSettingsScreenComponent(componentContext)
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