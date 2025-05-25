package org.iesharia.domain.services

import kotlinx.datetime.Clock
import org.bson.types.ObjectId
import org.iesharia.data.models.UserDocument
import org.iesharia.data.repositories.UserRepository
import org.iesharia.domain.models.dto.*
import org.iesharia.utils.JwtManager
import org.iesharia.utils.PasswordUtils
import org.iesharia.domain.models.UserRole

class AuthService(
    private val userRepository: UserRepository,
    private val jwtManager: JwtManager
) {
    suspend fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Invalid email or password")
        
        if (!user.isActive) {
            throw SecurityException("Account is disabled")
        }
        
        // Usar verificación compatible que detecta automáticamente el formato
        if (!PasswordUtils.verifyPasswordCompatible(request.password, user.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }
        
        // Update last login
        userRepository.updateLastLogin(user.id)
        
        val token = jwtManager.generateToken(user)
        
        return LoginResponse(
            token = token,
            user = user.toDto(),
            expiresIn = jwtManager.getExpirationTime()
        )
    }
    
    suspend fun register(request: RegisterRequest): LoginResponse {
        // Validate password strength
        val (isValid, message) = PasswordUtils.validatePasswordStrength(request.password)
        if (!isValid) {
            throw IllegalArgumentException(message ?: "Invalid password")
        }
        
        // Username is no longer used, only email
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already exists")
        }
        
        // Create user
        val now = Clock.System.now()
        // Usar hash compatible que detecta automáticamente el formato
        val hashedPassword = PasswordUtils.hashPasswordCompatible(request.password)
        
        val newUser = UserDocument(
            _id = ObjectId(),
            email = request.email,
            password = hashedPassword,
            name = request.name,
            surname = request.surname,
            phone = request.phone,
            role = request.role,
            permissions = getDefaultPermissionsForRole(request.role),
            associatedTeamId = null,
            isActive = true,
            createdAt = now,
            updatedAt = now,
            lastLogin = now
        )
        
        val createdUser = userRepository.create(newUser)
        val token = jwtManager.generateToken(createdUser)
        
        return LoginResponse(
            token = token,
            user = createdUser.toDto(),
            expiresIn = jwtManager.getExpirationTime()
        )
    }
    
    private fun getDefaultPermissionsForRole(role: UserRole): List<String> {
        return when (role) {
            UserRole.FEDERATIVE_DELEGATE -> listOf(
                "MANAGE_USERS",
                "MANAGE_TEAMS", 
                "MANAGE_WRESTLERS",
                "MANAGE_COMPETITIONS",
                "MANAGE_MATCHES",
                "MANAGE_REFEREES",
                "VIEW_ALL",
                "MANAGE_FAVORITES"
            )
            UserRole.COACH -> listOf(
                "MANAGE_OWN_TEAM",
                "VIEW_COMPETITIONS",
                "VIEW_MATCHES",
                "SUBMIT_MATCH_ACTS",
                "MANAGE_FAVORITES"
            )
            UserRole.REGISTERED_USER -> listOf(
                "VIEW_COMPETITIONS",
                "VIEW_TEAMS",
                "VIEW_WRESTLERS",
                "VIEW_MATCHES",
                "MANAGE_FAVORITES"
            )
            UserRole.GUEST -> listOf(
                "VIEW_COMPETITIONS",
                "VIEW_TEAMS",
                "VIEW_WRESTLERS",
                "VIEW_MATCHES"
            )
        }
    }
    
    private fun UserDocument.toDto() = UserDto(
        id = this.id,
        email = this.email,
        name = this.name,
        surname = this.surname,
        phone = this.phone,
        role = this.role.name,
        permissions = this.permissions,
        associatedTeamId = this.associatedTeamId,
        isActive = this.isActive
    )
}