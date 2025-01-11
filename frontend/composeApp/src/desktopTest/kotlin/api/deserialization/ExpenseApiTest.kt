package api.deserialization

import api.createMockClient
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.TotalExpenses
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpenseApiTest {

    @Test
    fun `test deserialization of a single expense`() {
        val mockResponse = """
        {
            "id": 9007199254740991,
            "title": "string",
            "price": 0.1,
            "expenseDate": "2025-01-11",
            "category": {
                "id": 9007199254740991,
                "name": "string"
            },
            "methodOfPayment": {
                "id": 9007199254740991,
                "name": "string"
            },
            "currency": {
                "id": 9007199254740991,
                "symbol": "string",
                "exchangeRate": 0.1
            },
            "user": {
                "id": 9007199254740991,
                "name": "string",
                "surname": "string",
                "email": "string"
            },
            "groupName": "string"
        }
    """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }

        val client = createMockClient(mockEngine)

        runBlocking {
            val expense: Expense = client.get("https://api.example.com/expense/1").body()

            assertEquals(9007199254740991, expense.id)
            assertEquals("string", expense.title)
            assertEquals(0.1f, expense.price)
            assertEquals(LocalDate.parse("2025-01-11"), expense.expenseDate)
            assertEquals("string", expense.category.name)
            assertEquals("string", expense.methodOfPayment.name)
            assertEquals("string", expense.currency.symbol)
            assertEquals(0.1f, expense.currency.exchangeRate)
            assertEquals("string", expense.user.name)
            assertEquals("string", expense.user.surname)
            assertEquals("string", expense.user.email)
            assertEquals("string", expense.groupName)
        }
    }

    @Test
    fun `test deserialization of a list of expenses with 3 items`() {
        val mockResponse = """
        [
            {
                "id": 9007199254740991,
                "title": "Expense 1",
                "price": 10.0,
                "expenseDate": "2025-01-11",
                "category": {
                    "id": 9007199254740991,
                    "name": "Food"
                },
                "methodOfPayment": {
                    "id": 9007199254740991,
                    "name": "Cash"
                },
                "currency": {
                    "id": 9007199254740991,
                    "symbol": "USD",
                    "exchangeRate": 0.1
                },
                "user": {
                    "id": 9007199254740991,
                    "name": "John",
                    "surname": "Doe",
                    "email": "john.doe@example.com"
                },
                "groupName": "Group A"
            },
            {
                "id": 9007199254740992,
                "title": "Expense 2",
                "price": 20.0,
                "expenseDate": "2025-01-12",
                "category": {
                    "id": 9007199254740992,
                    "name": "Transport"
                },
                "methodOfPayment": {
                    "id": 9007199254740992,
                    "name": "Card"
                },
                "currency": {
                    "id": 9007199254740992,
                    "symbol": "EUR",
                    "exchangeRate": 0.85
                },
                "user": {
                    "id": 9007199254740992,
                    "name": "Jane",
                    "surname": "Doe",
                    "email": "jane.doe@example.com"
                },
                "groupName": "Group B"
            },
            {
                "id": 9007199254740993,
                "title": "Expense 3",
                "price": 30.0,
                "expenseDate": "2025-01-13",
                "category": {
                    "id": 9007199254740993,
                    "name": "Entertainment"
                },
                "methodOfPayment": {
                    "id": 9007199254740993,
                    "name": "Online"
                },
                "currency": {
                    "id": 9007199254740993,
                    "symbol": "GBP",
                    "exchangeRate": 0.75
                },
                "user": {
                    "id": 9007199254740993,
                    "name": "Alice",
                    "surname": "Smith",
                    "email": "alice.smith@example.com"
                },
                "groupName": "Group C"
            }
        ]
    """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }

        val client = createMockClient(mockEngine)

        runBlocking {
            val expenses: List<Expense> = client.get("https://api.example.com/expenses").body()

            assertEquals(3, expenses.size)

            val expense1 = expenses[0]
            assertEquals(9007199254740991, expense1.id)
            assertEquals("Expense 1", expense1.title)
            assertEquals(10.0f, expense1.price)
            assertEquals(LocalDate.parse("2025-01-11"), expense1.expenseDate)
            assertEquals("Food", expense1.category.name)
            assertEquals("Cash", expense1.methodOfPayment.name)
            assertEquals("USD", expense1.currency.symbol)
            assertEquals(0.1f, expense1.currency.exchangeRate)
            assertEquals("John", expense1.user.name)
            assertEquals("Doe", expense1.user.surname)
            assertEquals("john.doe@example.com", expense1.user.email)
            assertEquals("Group A", expense1.groupName)

            val expense2 = expenses[1]
            assertEquals(9007199254740992, expense2.id)
            assertEquals("Expense 2", expense2.title)
            assertEquals(20.0f, expense2.price)
            assertEquals(LocalDate.parse("2025-01-12"), expense2.expenseDate)
            assertEquals("Transport", expense2.category.name)
            assertEquals("Card", expense2.methodOfPayment.name)
            assertEquals("EUR", expense2.currency.symbol)
            assertEquals(0.85f, expense2.currency.exchangeRate)
            assertEquals("Jane", expense2.user.name)
            assertEquals("Doe", expense2.user.surname)
            assertEquals("jane.doe@example.com", expense2.user.email)
            assertEquals("Group B", expense2.groupName)

            val expense3 = expenses[2]
            assertEquals(9007199254740993, expense3.id)
            assertEquals("Expense 3", expense3.title)
            assertEquals(30.0f, expense3.price)
            assertEquals(LocalDate.parse("2025-01-13"), expense3.expenseDate)
            assertEquals("Entertainment", expense3.category.name)
            assertEquals("Online", expense3.methodOfPayment.name)
            assertEquals("GBP", expense3.currency.symbol)
            assertEquals(0.75f, expense3.currency.exchangeRate)
            assertEquals("Alice", expense3.user.name)
            assertEquals("Smith", expense3.user.surname)
            assertEquals("alice.smith@example.com", expense3.user.email)
            assertEquals("Group C", expense3.groupName)
        }
    }

    @Test
    fun `test deserialization of total expenses`() {
        val mockResponse = """
        {
            "userExpenses": 0.1,
            "groupExpenses": 0.1
        }
    """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }

        val client = createMockClient(mockEngine)

        runBlocking {
            val totalExpenses: TotalExpenses = client.get("https://api.example.com/total-expenses").body()

            assertEquals(0.1f, totalExpenses.userExpenses)
            assertEquals(0.1f, totalExpenses.groupExpenses)
        }
    }
}