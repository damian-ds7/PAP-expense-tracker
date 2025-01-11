package api

import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.databaseAssociatedData.Category
import pw.edu.pl.pap.data.databaseAssociatedData.Currency
import pw.edu.pl.pap.data.databaseAssociatedData.PaymentMethod
import pw.edu.pl.pap.data.databaseAssociatedData.User
import kotlin.test.*

class ApiSerializationTest {

    @Test
    fun `test deserialization of user data`() {
        val mockResponse = """
            {
                "id": 1,
                "name": "Jurek",
                "surname": "Ogórek",
                "email": "jurekogorek@gmail.com"
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
            val user: User = client.get("https://api.example.com/user/1").body()
            assertEquals(1, user.id)
            assertEquals("Jurek", user.name)
            assertEquals("Ogórek", user.surname)
            assertEquals("jurekogorek@gmail.com", user.email)
        }
    }

    @Test
    fun `test deserialization of list of users`() {
        val mockResponse = """
            [
                {
                    "id": 1,
                    "name": "Jurek",
                    "surname": "Ogórek",
                    "email": "jurekogorek@gmail.com"
                },
                {
                    "id": 2,
                    "name": "Anna",
                    "surname": "Kowalska",
                    "email": "anna.kowalska@gmail.com"
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
            val users: List<User> = client.get("https://api.example.com/users").body()

            assertEquals(2, users.size)

            val user1 = users[0]
            assertEquals(1, user1.id)
            assertEquals("Jurek", user1.name)
            assertEquals("Ogórek", user1.surname)
            assertEquals("jurekogorek@gmail.com", user1.email)

            val user2 = users[1]
            assertEquals(2, user2.id)
            assertEquals("Anna", user2.name)
            assertEquals("Kowalska", user2.surname)
            assertEquals("anna.kowalska@gmail.com", user2.email)
        }
    }

    @Test
    fun `test deserialization of category list with 3 items`() {
        val mockResponse = """
            [
                {
                    "id": 9007199254740991,
                    "name": "Category 1"
                },
                {
                    "id": 9007199254740992,
                    "name": "Category 2"
                },
                {
                    "id": 9007199254740993,
                    "name": "Category 3"
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
            val categories: List<Category> = client.get("https://api.example.com/categories").body()

            assertEquals(3, categories.size)

            val category1 = categories[0]
            assertEquals(9007199254740991, category1.id)
            assertEquals("Category 1", category1.name)

            val category2 = categories[1]
            assertEquals(9007199254740992, category2.id)
            assertEquals("Category 2", category2.name)

            val category3 = categories[2]
            assertEquals(9007199254740993, category3.id)
            assertEquals("Category 3", category3.name)
        }
    }

    @Test
    fun `test deserialization of payment methods list with 2 items`() {
        val mockResponse = """
            [
                {
                    "id": 9007199254740991,
                    "name": "cash"
                },
                {
                    "id": 9007199254740992,
                    "name": "card"
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
            val paymentMethods: List<PaymentMethod> = client.get("https://api.example.com/payment-methods").body()

            assertEquals(2, paymentMethods.size)

            val cash = paymentMethods[0]
            assertEquals(9007199254740991, cash.id)
            assertEquals("cash", cash.name)

            val card = paymentMethods[1]
            assertEquals(9007199254740992, card.id)
            assertEquals("card", card.name)
        }
    }

    @Test
    fun `test deserialization of currencies list with 3 items`() {
        val mockResponse = """
        [
            {
                "id": 9007199254740991,
                "symbol": "USD",
                "exchangeRate": 0.1
            },
            {
                "id": 9007199254740992,
                "symbol": "EUR",
                "exchangeRate": 0.85
            },
            {
                "id": 9007199254740993,
                "symbol": "GBP",
                "exchangeRate": 0.75
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
            val currencies: List<Currency> = client.get("https://api.example.com/currencies").body()

            assertEquals(3, currencies.size)

            val usd = currencies[0]
            assertEquals(9007199254740991, usd.id)
            assertEquals("USD", usd.symbol)
            assertEquals(0.1f, usd.exchangeRate)

            val eur = currencies[1]
            assertEquals(9007199254740992, eur.id)
            assertEquals("EUR", eur.symbol)
            assertEquals(0.85f, eur.exchangeRate)

            val gbp = currencies[2]
            assertEquals(9007199254740993, gbp.id)
            assertEquals("GBP", gbp.symbol)
            assertEquals(0.75f, gbp.exchangeRate)
        }
    }
}