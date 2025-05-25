package org.iesharia.config

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.iesharia.utils.serializers.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            useArrayPolymorphism = false
            serializersModule = SerializersModule {
                contextual(ObjectIdSerializer)
                contextual(InstantSerializer)
                contextual(LocalDateSerializer)
                contextual(LocalTimeSerializer)
            }
        })
    }
}