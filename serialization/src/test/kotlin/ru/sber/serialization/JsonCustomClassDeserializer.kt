package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class JsonCustomClassDeserializer {

    @Test
    fun `Необходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""

        val module = SimpleModule().addDeserializer(Client7::class.java, JsonCustomClassDeserializerClient7())
        val objectMapper = ObjectMapper().registerModule(module)

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }
}

class JsonCustomClassDeserializerClient7 : JsonDeserializer<Client7>() {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): Client7 {

        val node = jp.codec.readTree<JsonNode>(jp)
        val stringAfterParse = node["client"].toString().trim('"').split(" ")

        return Client7(firstName = stringAfterParse[1], lastName = stringAfterParse[0], middleName = stringAfterParse[2])
    }
}
