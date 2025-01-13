package pw.edu.pl.pap.di

import org.koin.dsl.module
import pw.edu.pl.pap.api.ServiceCreator
import pw.edu.pl.pap.api.data.*

val apiModule = module {
    single { ServiceCreator() }
    single { DataServiceCreator(get(), getProperty("baseUrl")) }

    single { get<DataServiceCreator>().createExpenseApi() }
    single { get<DataServiceCreator>().createChartApi() }
    single { get<DataServiceCreator>().createGroupApi() }
    single { get<DataServiceCreator>().createUserApi() }
    single { get<DataServiceCreator>().createMembershipApi() }
    single { get<DataServiceCreator>().createTemporaryMembershipApi() }
}