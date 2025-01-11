package pw.edu.pl.pap.di

import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module
import pw.edu.pl.pap.api.auth.AuthServiceCreator
import pw.edu.pl.pap.api.data.*

val apiModule = module {
    single { DataServiceCreator(get(), getProperty("baseUrl")) }

    single { get<DataServiceCreator>().createExpenseApi() }
    single { get<DataServiceCreator>().createChartApi() }
    single { get<DataServiceCreator>().createGroupApi() }
    single { get<DataServiceCreator>().createConfigApi() }
}