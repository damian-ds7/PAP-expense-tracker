package pw.edu.pl.pap.screenComponents

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import pw.edu.pl.pap.api.ApiService

open class BaseScreenComponentImpl(
    componentContext: ComponentContext,
    override val apiService: ApiService,
    override val coroutineScope: CoroutineScope,
) : BaseScreenComponent, ComponentContext by componentContext