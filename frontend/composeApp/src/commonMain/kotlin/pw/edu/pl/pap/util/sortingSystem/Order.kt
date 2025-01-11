package pw.edu.pl.pap.util.sortingSystem
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object OrderSerializer : KSerializer<Order> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Order", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Order) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Order {
        val name = decoder.decodeString()
        return Order.entries.first { it.name == name }
    }
}

@Serializable(with = OrderSerializer::class)
enum class Order(val paramName: String) {
    ASCENDING("asc"), DESCENDING("desc")
}