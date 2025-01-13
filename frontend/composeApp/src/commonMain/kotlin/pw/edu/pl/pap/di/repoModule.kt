package pw.edu.pl.pap.di

import org.koin.dsl.module
import pw.edu.pl.pap.repositories.data.*

val repoModule = module {
    single { ExpenseRepository(get()) }
    single { GroupRepository(get()) }
    single { ChartsRepository(get()) }
    single { UserRepository(get()) }
    single { MembershipRepository(get()) }
    single { TemporaryMembershipRepository(get()) }
}