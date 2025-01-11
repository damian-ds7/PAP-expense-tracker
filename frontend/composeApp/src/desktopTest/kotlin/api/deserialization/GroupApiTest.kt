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
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import kotlin.test.Test
import kotlin.test.assertEquals

class GroupApiTest {

    @Test
    fun `test deserialization of user group data with single item`() {
        val mockResponse = """
        [
            {
                "id": 9007199254740991,
                "name": "string"
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
            val userGroups: List<UserGroup> = client.get("https://api.example.com/user-groups").body()

            assertEquals(1, userGroups.size)
            assertEquals(9007199254740991, userGroups[0].id)
            assertEquals("string", userGroups[0].name)
        }
    }

    @Test
    fun `test deserialization of user group data with three items`() {
        val mockResponse = """
        [
            {
                "id": 9007199254740991,
                "name": "string"
            },
            {
                "id": 9007199254740992,
                "name": "string2"
            },
            {
                "id": 9007199254740993,
                "name": "string3"
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
            val userGroups: List<UserGroup> = client.get("https://api.example.com/user-groups").body()

            assertEquals(3, userGroups.size)
            assertEquals(9007199254740991, userGroups[0].id)
            assertEquals("string", userGroups[0].name)
            assertEquals(9007199254740992, userGroups[1].id)
            assertEquals("string2", userGroups[1].name)
            assertEquals(9007199254740993, userGroups[2].id)
            assertEquals("string3", userGroups[2].name)
        }
    }

}