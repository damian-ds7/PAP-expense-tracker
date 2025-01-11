package pw.edu.pl.pap.screenComponents

import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

interface BaseComponent : KoinComponent {
    val coroutineScope: CoroutineScope
}