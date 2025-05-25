package org.iesharia.presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.iesharia.domain.models.dto.CreateFavoriteRequest
import org.iesharia.domain.models.dto.FavoritesResponse
import org.iesharia.domain.services.FavoriteService
import org.koin.ktor.ext.inject

fun Route.favoriteRoutes() {
    val favoriteService by inject<FavoriteService>()
    
    authenticate("auth-jwt") {
        route("/favorites") {
            // Obtener todos los favoritos del usuario
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.subject
                
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Usuario no autenticado"))
                    return@get
                }
                
                val type = call.request.queryParameters["type"]
                
                val favorites = if (type != null) {
                    favoriteService.getUserFavoritesByType(userId, type)
                } else {
                    favoriteService.getUserFavorites(userId)
                }
                
                call.respond(favorites)
            }
            
            // Verificar si una entidad es favorita
            get("/check/{entityType}/{entityId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.subject
                
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Usuario no autenticado"))
                    return@get
                }
                
                val entityType = call.parameters["entityType"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Tipo de entidad requerido")
                )
                
                val entityId = call.parameters["entityId"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "ID de entidad requerido")
                )
                
                val isFavorite = favoriteService.isFavorite(userId, entityId, entityType)
                call.respond(mapOf("isFavorite" to isFavorite))
            }
            
            // Agregar favorito
            post {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.subject
                
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Usuario no autenticado"))
                    return@post
                }
                
                try {
                    val request = call.receive<CreateFavoriteRequest>()
                    val favorite = favoriteService.addFavorite(userId, request)
                    call.respond(HttpStatusCode.Created, favorite)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Datos inválidos")
                    )
                }
            }
            
            // Eliminar favorito
            delete("/{entityType}/{entityId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.subject
                
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Usuario no autenticado"))
                    return@delete
                }
                
                val entityType = call.parameters["entityType"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Tipo de entidad requerido")
                )
                
                val entityId = call.parameters["entityId"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "ID de entidad requerido")
                )
                
                try {
                    val deleted = favoriteService.removeFavorite(userId, entityId, entityType)
                    if (deleted) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Favorito no encontrado"))
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }
            
            // Eliminar todos los favoritos del usuario
            delete {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.subject
                
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Usuario no autenticado"))
                    return@delete
                }
                
                val deleted = favoriteService.removeAllUserFavorites(userId)
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "No se encontraron favoritos"))
                }
            }
        }
    }
}