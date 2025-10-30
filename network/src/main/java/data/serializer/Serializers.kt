package data.serializer

import domain.model.AssetType
import domain.model.LiabilityType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object AssetTypeSerializer : KSerializer<AssetType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("AssetType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AssetType) {
        encoder.encodeString(value.label)
    }

    override fun deserialize(decoder: Decoder): AssetType {
        val label = decoder.decodeString()
        return AssetType.entries.find { it.label == label }
            ?: throw IllegalArgumentException("Unknown AssetType label: $label")
    }
}

object LiabilityTypeSerializer : KSerializer<LiabilityType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LiabilityType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LiabilityType) {
        encoder.encodeString(value.label)
    }

    override fun deserialize(decoder: Decoder): LiabilityType {
        val label = decoder.decodeString()
        return LiabilityType.entries.find { it.label == label }
            ?: throw IllegalArgumentException("Unknown Liability label: $label")
    }
}
