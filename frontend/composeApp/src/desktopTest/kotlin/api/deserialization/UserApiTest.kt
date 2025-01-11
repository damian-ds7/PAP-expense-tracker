package api.deserialization

import api.createMockClient
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.databaseAssociatedData.User
import kotlin.test.*

class UserApiTest {

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


}