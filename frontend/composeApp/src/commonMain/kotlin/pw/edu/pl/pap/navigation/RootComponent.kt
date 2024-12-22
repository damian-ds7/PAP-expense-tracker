package pw.edu.pl.pap.navigation

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
import pw.edu.pl.pap.api.authApi.SignUpApi
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.navigation.loginSystem.LoginScreenComponent
import pw.edu.pl.pap.navigation.loginSystem.SelectionLoginSignupScreenComponent
import pw.edu.pl.pap.navigation.loginSystem.SignupScreenComponent
import pw.edu.pl.pap.navigation.singleExpense.ExpenseDetailsScreenComponent
import pw.edu.pl.pap.navigation.singleExpense.NewExpenseScreenComponent

class RootComponent(
    componentContext: ComponentContext,
    private val baseUrl: String
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
        data object HomeScreen : Configuration()

        @Serializable
        data object NewExpenseScreen : Configuration()

        @Serializable
        data class ExpenseDetailsScreen(val expense: Expense) : Configuration()

        @Serializable
        data object LogInSignUpSelectionScreen : Configuration()

        @Serializable
        data object LogInScreen : Configuration()

        @Serializable
        data object SignUpScreen : Configuration()
    }

    sealed class Child {
        data class HomeScreen(val component: HomeScreenComponent) : Child()
        data class NewExpenseScreen(val component: NewExpenseScreenComponent) : Child()
        data class ExpenseDetailsScreen(val component: ExpenseDetailsScreenComponent) : Child()
        data class LogInSignUpSelectionScreen(val component: SelectionLoginSignupScreenComponent) : Child()
        data class LogInScreen(val component: LoginScreenComponent) : Child()
        data class SignUpScreen(val component: SignupScreenComponent) : Child()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child =
        when (configuration) {
            is Configuration.LogInSignUpSelectionScreen -> {
                Child.LogInSignUpSelectionScreen(
                    component = SelectionLoginSignupScreenComponent(
                        componentContext = componentContext,
                        onLogInButtonClicked = {
                            navigation.pushNew(Configuration.LogInScreen)
                        },
                        onSignupButtonClicked = {
                            navigation.pushNew(Configuration.SignUpScreen)
                        }
                    )
                )
            }

            is Configuration.LogInScreen -> {
                Child.LogInScreen(
                    component = LoginScreenComponent(
                        componentContext = componentContext,
                        coroutineScope = coroutineScope,
                        apiClient = LoginApi(baseUrl, httpClient),
                        onConfirm = {
                            navigation.replaceAll(Configuration.HomeScreen)
                        },
                        setToken = { newToken ->
                            apiService = ApiService(newToken, httpClient, baseUrl)
                        }
                    )
                )
            }

            is Configuration.SignUpScreen -> {
                Child.SignUpScreen(
                    component = SignupScreenComponent(
                        componentContext = componentContext,
                        coroutineScope = coroutineScope,
                        apiClient = SignUpApi(baseUrl, httpClient),
                        onConfirm = {
                            navigation.replaceAll(Configuration.HomeScreen)
                        },
                        setToken = { newToken ->
                            apiService = ApiService(newToken, httpClient, baseUrl)
                        }
                    )
                )
            }

            is Configuration.HomeScreen -> {
                Child.HomeScreen(
                    component = HomeScreenComponent(
                        componentContext = componentContext,
                        apiService = apiService,
                        coroutineScope = coroutineScope,
                        onAddExpenseButtonClicked = {
                            navigation.pushNew(Configuration.NewExpenseScreen)
                        },
                        onExpenseClick = { expense ->
                            navigation.pushNew(Configuration.ExpenseDetailsScreen(expense))
                        }
                    )
                )
            }

            is Configuration.NewExpenseScreen -> Child.NewExpenseScreen(
                component = NewExpenseScreenComponent(
                    componentContext = componentContext,
                    apiService = apiService,
                    coroutineScope = coroutineScope,
                    onDismiss = { navigation.pop() },
                    onSave = {
                        navigation.pop()
                        (childStack.value.active.instance as Child.HomeScreen).component.updateNavigationState(
                            HomeScreenComponent.NavigationState.FromNewExpenseScreen
                        )
                    }
                )
            )

            is Configuration.ExpenseDetailsScreen -> Child.ExpenseDetailsScreen(
                component = ExpenseDetailsScreenComponent(
                    componentContext = componentContext,
                    apiService = apiService,
                    coroutineScope = coroutineScope,
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
        }

    @OptIn(ExperimentalDecomposeApi::class)
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.LogInSignUpSelectionScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )


}