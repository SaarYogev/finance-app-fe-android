package com.saaryogev.financeapp

import kotlinx.serialization.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class Expense(
    val amount: Int,
    val type: String,
    val paymentMethod: String,
    @Serializable(with = DateSerializer::class) val paymentDate: Date,
    val userId: String
)

@Serializer(forClass = Date::class)
object DateSerializer: KSerializer<Date> {
    private val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, obj: Date) {
        encoder.encodeString(df.format(obj))
    }

    override fun deserialize(decoder: Decoder): Date {
        return df.parse(decoder.decodeString())
    }
}