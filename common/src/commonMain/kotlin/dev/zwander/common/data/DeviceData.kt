package dev.zwander.common.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable(with = DeviceDataSerializer::class)
data class DeviceData(
    @SerialName("device_brand")
    val brand: String?,
    @SerialName("device_model")
    val model: String?,
    @SerialName("device_sdk")
    val sdk: Int?,
    @SerialName("device_release")
    val release: String?,
    @SerialName("device_security")
    val security: String?,
    @SerialName("build_fingerprint")
    val buildFingerprint: String?,
    val arcore: ArCoreSupport?,
    val cameras: Map<String, CameraInfo> = hashMapOf()
)

@Serializable
data class ArCoreSupport(
    @SerialName("arcore_support")
    val availability: ArCoreAvailability = ArCoreAvailability.UNKNOWN_ERROR,
    @SerialName("depth_support")
    val depthSupport: Boolean? = null
)

@Serializable
data class CameraInfo(
    @SerialName("lens_facing")
    val lensFacing: String?,
    val fov: String?,
    val resolution: String?,
    @SerialName("physical_sensors")
    val physicalSensors: List<CameraInfo> = listOf(),
    @SerialName("video_qualities")
    val videoQualities: List<String> = listOf(),
    val extensions: Map<String, ExtensionInfo> = mapOf(),
)

@Serializable
data class ExtensionInfo(
    val camera2: Boolean,
    val camerax: Boolean
)

@Serializable
enum class ArCoreAvailability {
    SUPPORTED_APK_TOO_OLD,
    SUPPORTED_INSTALLED,
    SUPPORTED_NOT_INSTALLED,
    UNKNOWN_ERROR,
    UNKNOWN_TIMED_OUT,
    UNSUPPORTED_DEVICE_NOT_CAPABLE,
}

@Serializer(forClass = DeviceData::class)
internal object DeviceDataSerializer : KSerializer<DeviceData> {

    private val stringToJsonElementSerializer = MapSerializer(String.serializer(), JsonElement.serializer())

    override val descriptor: SerialDescriptor = stringToJsonElementSerializer.descriptor

    override fun deserialize(decoder: Decoder): DeviceData {
        // Decoder -> JsonInput
        require(decoder is JsonDecoder) // this class can be decoded only by Json
        val json = decoder.json
        val filtersMap = decoder.decodeSerializableValue(stringToJsonElementSerializer)

        val brand = filtersMap["device_brand"]?.let {
            json.decodeFromJsonElement<String>(it)
        }

        val model = filtersMap["device_model"]?.let {
            json.decodeFromJsonElement<String>(it)
        }

        val sdk = filtersMap["device_sdk"]?.let {
            json.decodeFromJsonElement<Int>(it)
        }

        val release = filtersMap["device_release"]?.let {
            json.decodeFromJsonElement<String>(it)
        }

        val security = filtersMap["device_security"]?.let {
            json.decodeFromJsonElement<String>(it)
        }

        val fingerprint = filtersMap["build_fingerprint"]?.let {
            json.decodeFromJsonElement<String?>(it)
        }

        val arcore = filtersMap["arcore"]?.let {
            json.decodeFromJsonElement<ArCoreSupport?>(it)
        }

        val knownKeys =
            setOf("device_brand", "device_model", "device_sdk", "device_release", "device_security", "build_fingerprint", "arcore")

        val unknownFilters = filtersMap.filter { (key, _) -> ! knownKeys.contains(key) }
            .mapValues { json.decodeFromJsonElement<CameraInfo>(it.value) }

        return DeviceData(
            brand, model, sdk, release, security, fingerprint, arcore, cameras = unknownFilters
        )
    }

    override fun serialize(encoder: Encoder, value: DeviceData) {
        // Encoder -> JsonOutput
        require(encoder is JsonEncoder) // This class can be encoded only by Json
        val json = encoder.json
        val map: MutableMap<String, JsonElement> = mutableMapOf()

        value.brand?.let { map["device_brand"] = json.encodeToJsonElement(it) }
        value.model?.let { map["device_model"] = json.encodeToJsonElement(it) }
        value.sdk?.let { map["device_sdk"] = json.encodeToJsonElement(it) }
        value.release?.let { map["device_release"] = json.encodeToJsonElement(it) }
        value.security?.let {  map["device_security"] = json.encodeToJsonElement(it) }
        value.buildFingerprint?.let { map["build_fingerprint"] = json.encodeToJsonElement(it) }
        value.arcore?.let { map["arcore"] = json.encodeToJsonElement(it) }

        value.cameras.let { c -> map.putAll(c.mapValues { json.encodeToJsonElement(it.value) }) }

        encoder.encodeSerializableValue(stringToJsonElementSerializer, map)
    }
}
