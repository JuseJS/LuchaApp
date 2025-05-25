package org.iesharia.core.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.iesharia.core.network.TokenStorage
import org.iesharia.core.network.mapper.AuthMappers.toDomain
import org.iesharia.core.network.service.AuthService
import org.iesharia.features.auth.domain.model.User

class AuthenticationManager(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage
) {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    sealed class AuthState {
        object Loading : AuthState()
        data class Authenticated(val user: User) : AuthState()
        object Unauthenticated : AuthState()
        data class Error(val message: String) : AuthState()
    }
    
    init {
        // Check if user is already logged in
        checkAuthenticationStatus()
    }
    
    private fun checkAuthenticationStatus() {
        if (tokenStorage.hasValidToken()) {
            val userId = tokenStorage.getUserId()
            val email = tokenStorage.getUserEmail()
            val role = tokenStorage.getUserRole()
            
            if (userId != null && email != null && role != null) {
                val user = User(
                    id = userId,
                    name = "", // Will be loaded from API if needed
                    surname = "",
                    email = email,
                    role = org.iesharia.features.auth.domain.model.Role.valueOf(role),
                    associatedTeamId = null
                )
                _currentUser.value = user
                _authState.value = AuthState.Authenticated(user)
            } else {
                logout()
            }
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            _authState.value = AuthState.Loading
            
            val response = authService.login(email, password)
            val user = response.user.toDomain()
            
            // Save token and user info
            tokenStorage.saveToken(response.token)
            tokenStorage.saveUserInfo(
                userId = user.id,
                email = user.email,
                role = user.role.name
            )
            
            _currentUser.value = user
            _authState.value = AuthState.Authenticated(user)
            
            Result.success(user)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Login failed")
            Result.failure(e)
        }
    }
    
    suspend fun register(
        name: String,
        surname: String,
        email: String,
        password: String
    ): Result<User> {
        return try {
            _authState.value = AuthState.Loading
            
            val userDto = authService.register(name, surname, email, password)
            val user = userDto.toDomain()
            
            // After registration, user still needs to login
            _authState.value = AuthState.Unauthenticated
            
            Result.success(user)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Registration failed")
            Result.failure(e)
        }
    }
    
    fun logout() {
        tokenStorage.clearAll()
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
    }
    
    fun getToken(): String? = tokenStorage.getToken()
    
    fun isAuthenticated(): Boolean = _authState.value is AuthState.Authenticated
    
    fun getCurrentUser(): User? = _currentUser.value
}