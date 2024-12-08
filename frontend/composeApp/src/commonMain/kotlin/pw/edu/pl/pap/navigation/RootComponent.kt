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
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.Expense

class RootComponent(
    componentContext: ComponentContext,
    baseUrl: String
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    private val apiClient = ApiClient(baseUrl)
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Serializable
    sealed class Configuration {
        @Serializable
        data object HomeScreen : Configuration()

        @Serializable
        data object NewExpenseScreen : Configuration()

        @Serializable
        data class ExpenseDetailsScreen(val expense: Expense) : Configuration()
    }

    sealed class Child {
        data class HomeScreen(val component: HomeScreenComponent) : Child()
        data class NewExpenseScreen(val component: NewExpenseScreenComponent) : Child()
        data class ExpenseDetailsScreen(val component: ExpenseDetailsScreenComponent) : Child()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child =
        when (configuration) {
            is Configuration.HomeScreen -> {
                Child.HomeScreen(
                    component = HomeScreenComponent(
                        componentContext = componentContext,
                        apiClient = apiClient,
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
                    apiClient = apiClient,
                    coroutineScope = coroutineScope,
                    onBack = { navigation.pop() }
                )
            )

            is Configuration.ExpenseDetailsScreen -> Child.ExpenseDetailsScreen(
                component = ExpenseDetailsScreenComponent(
                    componentContext = componentContext,
                    apiClient = apiClient,
                    coroutineScope = coroutineScope,
                    expense = configuration.expense,
                    onBack = { navigation.pop() }
                )
            )
        }

    @OptIn(ExperimentalDecomposeApi::class)
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.HomeScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )


}