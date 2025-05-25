package org.iesharia.core.network

class TokenStorage {
    
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ROLE = "user_role"
    }
    
    // Simple in-memory storage for now - this should be persistent in production
    private val storage = mutableMapOf<String, String>()
    
    fun saveToken(token: String) {
        storage[KEY_ACCESS_TOKEN] = token
    }
    
    fun getToken(): String? {
        return storage[KEY_ACCESS_TOKEN]
    }
    
    fun saveUserInfo(userId: String, email: String, role: String) {
        storage[KEY_USER_ID] = userId
        storage[KEY_USER_EMAIL] = email
        storage[KEY_USER_ROLE] = role
    }
    
    fun getUserId(): String? = storage[KEY_USER_ID]
    fun getUserEmail(): String? = storage[KEY_USER_EMAIL]
    fun getUserRole(): String? = storage[KEY_USER_ROLE]
    
    fun clearAll() {
        storage.remove(KEY_ACCESS_TOKEN)
        storage.remove(KEY_USER_ID)
        storage.remove(KEY_USER_EMAIL)
        storage.remove(KEY_USER_ROLE)
    }
    
    fun hasValidToken(): Boolean {
        return !getToken().isNullOrBlank()
    }
}