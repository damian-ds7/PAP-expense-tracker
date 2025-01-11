package pw.edu.pl.pap.screenComponents

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

open class BaseComponentImpl(
    componentContext: ComponentContext,
    override val coroutineScope: CoroutineScope,
) : BaseComponent, ComponentContext by componentContext, KoinComponent