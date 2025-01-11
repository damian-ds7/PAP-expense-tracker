package pw.edu.pl.pap.di

import org.koin.dsl.module
import pw.edu.pl.pap.repositories.data.ChartsRepository
import pw.edu.pl.pap.repositories.data.ConfigRepository
import pw.edu.pl.pap.repositories.data.ExpenseRepository
import pw.edu.pl.pap.repositories.data.GroupRepository

val repoModule = module {
    single { ExpenseRepository(get()) }
    single { GroupRepository(get()) }
    single { ChartsRepository(get()) }
    single { ConfigRepository(get()) }
}