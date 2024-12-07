package pw.edu.pl.pap.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import kotlinx.serialization.Serializable
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import pw.edu.pl.pap.apiclient.ApiClient

class RootComponent(
    componentContext: ComponentContext,
    baseUrl: String
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    val apiClient = ApiClient(baseUrl)
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Serializable
    sealed class Configuration {
        @Serializable
        data class HomeScreen(val onAddExpenseButtonClicked: () -> Unit) : Configuration()

        @Serializable
        data object NewExpenseScreen : Configuration()
    }

    sealed class Child {
        data class HomeScreen(val component: HomeScreenComponent) : Child()
        data class NewExpenseScreen(val component: NewExpenseScreenComponent) : Child()
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
        }

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.HomeScreen(onAddExpenseButtonClicked = {
            navigation.pushNew(Configuration.NewExpenseScreen)
        }),
        handleBackButton = true,
        childFactory = ::createChild
    )




}