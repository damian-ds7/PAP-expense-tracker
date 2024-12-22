package pw.edu.pl.pap.util.sortingSystem

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pw.edu.pl.pap.util.formatDate
import java.util.*

@Serializable
sealed class GroupMapKey : Comparable<GroupMapKey> {
    abstract fun asString(): String

    @Serializable(with = GroupMapKeyDateSerializer::class)
    data class DateKey(val date: LocalDate) : GroupMapKey() {
        override fun asString(): String {
            return formatDate(date)
        }

        override fun compareTo(other: GroupMapKey): Int {
            return when (other) {
                is DateKey -> this.date.compareTo(other.date)
                is StringKey -> -1
            }
        }
    }

    @Serializable(with = GroupMapKeyStringSerializer::class)
    data class StringKey(val name: String) : GroupMapKey() {
        override fun asString(): String {
            return name
        }

        override fun compareTo(other: GroupMapKey): Int {
            return when (other) {
                is StringKey -> this.name.uppercase(Locale.getDefault())
                    .compareTo(other.name.uppercase(Locale.getDefault()))

                is DateKey -> 1
            }
        }
    }
}

object GroupMapKeyDateSerializer : KSerializer<GroupMapKey.DateKey> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("GroupMapKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: GroupMapKey.DateKey) {
        encoder.encodeString(value.asString())
    }

    override fun deserialize(decoder: Decoder): GroupMapKey.DateKey {
        val stringKey = decoder.decodeString()
        return GroupMapKey.DateKey(LocalDate.parse(stringKey))
    }
}

object GroupMapKeyStringSerializer : KSerializer<GroupMapKey.StringKey> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("GroupMapKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: GroupMapKey.StringKey) {
        encoder.encodeString(value.asString())
    }

    override fun deserialize(decoder: Decoder): GroupMapKey.StringKey {
        val stringKey = decoder.decodeString()
        return GroupMapKey.StringKey(stringKey)
    }
}