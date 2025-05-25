package org.iesharia.domain.services

import org.iesharia.data.repositories.UserRepository
import org.iesharia.domain.models.dto.UserDto
import org.iesharia.domain.models.UserRole

class UserService(
    private val userRepository: UserRepository
) {
    suspend fun getCurrentUser(userId: String): UserDto? {
        val user = userRepository.findById(userId) ?: return null
        return UserDto(
            id = user.id,
            email = user.email,
            name = user.name,
            surname = user.surname,
            phone = user.phone,
            role = user.role.name,
            permissions = user.permissions,
            associatedTeamId = user.associatedTeamId,
            isActive = user.isActive
        )
    }
    
    suspend fun updateUserRole(userId: String, role: UserRole): Boolean {
        return userRepository.updateRole(userId, role.name)
    }
    
    suspend fun assignTeamToCoach(userId: String, teamId: String?): Boolean {
        val user = userRepository.findById(userId) 
            ?: throw NoSuchElementException("User not found")
            
        if (user.role != UserRole.COACH) {
            throw SecurityException("Only coaches can be assigned to teams")
        }
        
        return userRepository.updateTeamAssociation(userId, teamId)
    }
}