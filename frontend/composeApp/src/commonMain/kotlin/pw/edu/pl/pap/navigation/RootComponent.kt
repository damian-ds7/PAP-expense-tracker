package pw.edu.pl.pap.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.Serializable
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.data.Expense
import pw.edu.pl.pap.navigation.loginSystem.LoginScreenComponent
import pw.edu.pl.pap.navigation.loginSystem.SelectionLoginSignupScreenComponent
import pw.edu.pl.pap.navigation.loginSystem.SignupScreenComponent

class RootComponent(
    componentContext: ComponentContext,
    baseUrl: String
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    private val apiService = ApiService(baseUrl)
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var user_token: String = ""
    //TODO (optional in the future) remember user token from previous session and fetch here

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
                    component= SelectionLoginSignupScreenComponent(
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
                        apiService = apiService,
                        coroutineScope = coroutineScope,
                        onConfirm = {
                            navigation.pushNew(Configuration.HomeScreen)
                        },
                        setToken = {
                            newToken -> user_token = newToken
                        }
                    )
                )
            }

            is Configuration.SignUpScreen -> {
                Child.SignUpScreen(
                    component = SignupScreenComponent(
                        componentContext = componentContext,
                        apiService = apiService,
                        coroutineScope = coroutineScope,
                        onConfirm = {
                            navigation.pushNew(Configuration.HomeScreen)
                        },
                        setToken = {
                                newToken -> user_token = newToken
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