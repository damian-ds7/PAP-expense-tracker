package pw.edu.pl.pap.repositories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pw.edu.pl.pap.api.data.ChartApi
import pw.edu.pl.pap.data.databaseAssociatedData.ChartFilterParams
import pw.edu.pl.pap.data.databaseAssociatedData.toMap
import pw.edu.pl.pap.util.charts.ChartData
import pw.edu.pl.pap.util.charts.FilterTimeFrames
import pw.edu.pl.pap.util.dateFunctions.getCurrentDate
import pw.edu.pl.pap.util.dateFunctions.getFirstAndLastDayOfAMonth
import pw.edu.pl.pap.util.dateFunctions.getFirstAndLastDayOfAYear
import java.util.*

class ChartsRepository(private val api: ChartApi) : KoinComponent {
    private val configRepository: ConfigRepository by inject()

    private val _chartFilters = MutableStateFlow(ChartFilterParams())
    val chartFilters: StateFlow<ChartFilterParams> get() = _chartFilters

    private val _plotData = MutableStateFlow<ChartData>(TreeMap())
    val plotData: StateFlow<ChartData> get() = _plotData

    private val _currentTimeFrame = MutableStateFlow(FilterTimeFrames.MONTH)
    val currentTimeFrame: StateFlow<FilterTimeFrames> get() = _currentTimeFrame

    private val _keyPattern = MutableStateFlow(configRepository.keyPatterns.value.first())
    val keyPattern: StateFlow<String> get() = _keyPattern

    private val _currentTimeBounds = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(
        getFirstAndLastDayOfAMonth(
            getCurrentDate()
        )
    )
    val currentTimeBounds: StateFlow<Pair<LocalDate?, LocalDate?>> get() = _currentTimeBounds

    fun resetFilters() {
        _chartFilters.value = ChartFilterParams()
        _keyPattern.value = configRepository.keyPatterns.value.first()
    }

    suspend fun getPlotData(group: String) {
        try {
            val beginDate = currentTimeBounds.value.first
            val endDate = currentTimeBounds.value.second

            _chartFilters.value = _chartFilters.value.copy(beginDate = beginDate, endDate = endDate)

            println("get plot data")
            _plotData.value = api.getData(
                group, keyPattern.value, _chartFilters.value.toMap()
            )
            println(_plotData.value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun changeTimeFrame(newTimeFrame: FilterTimeFrames) {
        when (newTimeFrame) {
            FilterTimeFrames.MONTH -> {
                _currentTimeBounds.value = getFirstAndLastDayOfAMonth(getCurrentDate())
            }

            FilterTimeFrames.YEAR -> {
                _currentTimeBounds.value = getFirstAndLastDayOfAYear(getCurrentDate())
            }

            FilterTimeFrames.CUSTOM -> {
                _currentTimeBounds.value = Pair(null, null)
            }
        }
        _currentTimeFrame.value = newTimeFrame
    }

    fun modifyTimeBounds(
        amount: Int,
    ) {
        val curDate = _currentTimeBounds.value.first!!
        val unit: DateTimeUnit
        val newDate: LocalDate

        when (_currentTimeFrame.value) {
            FilterTimeFrames.MONTH -> {
                unit = DateTimeUnit.MONTH
                newDate = curDate.plus(amount, unit)
                _currentTimeBounds.value = getFirstAndLastDayOfAMonth(newDate)
            }

            FilterTimeFrames.YEAR -> {
                unit = DateTimeUnit.YEAR
                newDate = curDate.plus(amount, unit)
                _currentTimeBounds.value = getFirstAndLastDayOfAYear(newDate)
            }

            FilterTimeFrames.CUSTOM -> return
        }
    }

    fun modifyTimeBounds(beginDate: LocalDate?, endDate: LocalDate?) {
        val newTimeBounds = Pair(beginDate, endDate)
        _currentTimeBounds.value = newTimeBounds
    }

    fun updateKeyPattern(newKeyPattern: String) {
        _keyPattern.value = newKeyPattern
    }

    fun updateFilterParams(newParams: ChartFilterParams) {
        _chartFilters.value = newParams
    }

}