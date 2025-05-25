package org.iesharia.presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import org.iesharia.config.DatabaseConfig
import org.iesharia.domain.models.dto.DatabaseStatus
import org.iesharia.domain.models.dto.HealthCheckResponse
import org.koin.ktor.ext.get
import com.mongodb.kotlin.client.coroutine.MongoClient

fun Route.healthRoutes() {
    get("/health") {
        val mongoClient: MongoClient = call.application.get()
        
        val databaseStatus = try {
            val database = DatabaseConfig.getDatabase(mongoClient)
            database.listCollectionNames().collect { }
            DatabaseStatus(
                connected = true,
                name = database.name
            )
        } catch (e: Exception) {
            DatabaseStatus(
                connected = false,
                error = e.message
            )
        }
        
        val response = HealthCheckResponse(
            status = if (databaseStatus.connected) "healthy" else "unhealthy",
            version = "1.0.0",
            timestamp = Clock.System.now().toString(),
            database = databaseStatus
        )
        
        if (databaseStatus.connected) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.ServiceUnavailable, response)
        }
    }
}