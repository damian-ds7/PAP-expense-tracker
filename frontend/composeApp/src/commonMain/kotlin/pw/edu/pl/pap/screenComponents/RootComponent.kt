package pw.edu.pl.pap.screenComponents

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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.api.authApi.LoginApi
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.screenComponents.loginSystem.*
import pw.edu.pl.pap.screenComponents.mainScreens.*
import pw.edu.pl.pap.screenComponents.settingsScreens.ChangePasswordScreenComponent
import pw.edu.pl.pap.screenComponents.settingsScreens.PreferencesScreenComponent
import pw.edu.pl.pap.screenComponents.settingsScreens.UserPersonalDataScreenComponent
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
        data object NewExpenseScreen : Configuration()

        @Serializable
        data class ExpenseDetailsScreen(val expense: Expense) : Configuration()

        @Serializable
        data object DataScreen : Configuration()

        @Serializable
        data object SettingsScreen : Configuration()

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

    sealed class Child {
        data class LogInSignUpSelectionScreen(val component: SelectionLoginSignupScreenComponent) : Child()
        data class LogInScreen(val component: LoginScreenComponent) : Child()
        data class SignUpScreen(val component: SignupScreenComponent) : Child()

        data class HomeScreen(val component: HomeScreenComponent) : Child()
        data class NewExpenseScreen(val component: NewExpenseScreenComponent) : Child()
        data class ExpenseDetailsScreen(val component: ExpenseDetailsScreenComponent) : Child()

        data class DataScreen(val component: DataScreenComponent) : Child()
        data class SettingsScreen(val component: SettingsScreenComponent) : Child()

        data class UserPersonalDataScreen(val component: UserPersonalDataScreenComponent) : Child()
        data class ChangePasswordScreen(val component: ChangePasswordScreenComponent) : Child()
        data class PreferencesScreen(val component: PreferencesScreenComponent) : Child()
    }

    // TODO add new screens when ready
    fun navBarItemClicked(item: NavBarItem) {
        when (item) {
            NavBarItem.Home -> navigation.bringToFront(Configuration.HomeScreen)
            NavBarItem.Data -> navigation.bringToFront(Configuration.DataScreen)
            NavBarItem.Groups -> navigation.bringToFront(Configuration.HomeScreen)
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
                        onAddExpenseButtonClicked = {
                            navigation.pushNew(Configuration.NewExpenseScreen)
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
                    })
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

            is Configuration.DataScreen -> Child.DataScreen(
                DataScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext)
                )
            )

            is Configuration.SettingsScreen -> Child.SettingsScreen(
                SettingsScreenComponent(
                    onLogOut = { navigation.replaceAll(Configuration.LogInSignUpSelectionScreen) },
                    onUserPersonalsClicked = { navigation.pushNew(Configuration.UserPersonalDataScreen) },
                    onChangePasswordClicked = { navigation.pushNew(Configuration.ChangePasswordScreen) },
                    onEditPreferencesClicked = { navigation.pushNew(Configuration.PreferencesScreen) },
                    baseComponent = createMainScreenComponent(componentContext)
                )
            )

            is Configuration.UserPersonalDataScreen -> Child.UserPersonalDataScreen(
                UserPersonalDataScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext)
                )
            )

            is Configuration.ChangePasswordScreen -> Child.ChangePasswordScreen(
                ChangePasswordScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext)
                )
            )

            is Configuration.PreferencesScreen -> Child.PreferencesScreen(
                PreferencesScreenComponent(
                    baseComponent = createMainScreenComponent(componentContext)
                )
            )
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