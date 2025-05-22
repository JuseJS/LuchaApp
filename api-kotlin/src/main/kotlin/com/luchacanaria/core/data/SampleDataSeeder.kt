package com.luchacanaria.core.data

import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.auth.domain.model.Role
import com.luchacanaria.features.auth.domain.model.User
import com.luchacanaria.features.auth.domain.repository.UserRepository
import com.luchacanaria.features.competitions.domain.model.DivisionCategory
import com.luchacanaria.features.teams.domain.model.Team
import com.luchacanaria.features.teams.domain.repository.TeamRepository
import com.luchacanaria.features.wrestlers.domain.model.Wrestler
import com.luchacanaria.features.wrestlers.domain.model.WrestlerCategory
import com.luchacanaria.features.wrestlers.domain.model.WrestlerClassification
import com.luchacanaria.features.wrestlers.domain.repository.WrestlerRepository
import kotlinx.datetime.LocalDate
import java.util.UUID

class SampleDataSeeder(
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository,
    private val wrestlerRepository: WrestlerRepository
) {
    suspend fun seedData() {
        try {
            // Check if data already exists
            if (userRepository.getAllUsers().isNotEmpty()) {
                println("📊 Sample data already exists, skipping seeding")
                return
            }

            println("🌱 Seeding sample data...")

            // Create admin user
            val adminUser = User(
                id = UUID.randomUUID().toString(),
                email = "admin@luchacanaria.com",
                name = "Admin",
                surname = "System",
                role = Role.ADMIN
            )
            userRepository.createUser(adminUser, "admin123")

            // Create sample teams
            val teams = listOf(
                Team(
                    id = UUID.randomUUID().toString(),
                    name = "Acero Tenerife",
                    imageUrl = "https://example.com/acero.png",
                    island = Island.TENERIFE,
                    venue = "Terrero La Esperanza",
                    divisionCategory = DivisionCategory.PRIMERA
                ),
                Team(
                    id = UUID.randomUUID().toString(),
                    name = "Roque Nublo Gran Canaria",
                    imageUrl = "https://example.com/roque.png",
                    island = Island.GRAN_CANARIA,
                    venue = "Terrero San Mateo",
                    divisionCategory = DivisionCategory.PRIMERA
                ),
                Team(
                    id = UUID.randomUUID().toString(),
                    name = "Benahoare La Palma",
                    imageUrl = "https://example.com/benahoare.png",
                    island = Island.LA_PALMA,
                    venue = "Terrero Los Llanos",
                    divisionCategory = DivisionCategory.SEGUNDA
                )
            )

            val createdTeams = teams.map { teamRepository.createTeam(it) }
            
            // Create sample wrestlers
            val wrestlers = listOf(
                Wrestler(
                    id = UUID.randomUUID().toString(),
                    licenseNumber = "TF001",
                    name = "Juan",
                    surname = "Pérez García",
                    imageUrl = null,
                    teamId = createdTeams[0].id,
                    category = WrestlerCategory.REGIONAL,
                    classification = WrestlerClassification.PUNTAL_A,
                    height = 180,
                    weight = 85,
                    birthDate = LocalDate(1995, 3, 15),
                    nickname = "El Tinerfeño"
                ),
                Wrestler(
                    id = UUID.randomUUID().toString(),
                    licenseNumber = "GC001",
                    name = "Pedro",
                    surname = "Rodríguez Mendoza",
                    imageUrl = null,
                    teamId = createdTeams[1].id,
                    category = WrestlerCategory.REGIONAL,
                    classification = WrestlerClassification.DESTACADO_A,
                    height = 175,
                    weight = 80,
                    birthDate = LocalDate(1992, 7, 22),
                    nickname = "Roque"
                ),
                Wrestler(
                    id = UUID.randomUUID().toString(),
                    licenseNumber = "LP001",
                    name = "Miguel",
                    surname = "González López",
                    imageUrl = null,
                    teamId = createdTeams[2].id,
                    category = WrestlerCategory.JUVENIL,
                    classification = WrestlerClassification.PUNTAL_B,
                    height = 170,
                    weight = 75,
                    birthDate = LocalDate(2000, 11, 8),
                    nickname = "Palmero"
                )
            )

            wrestlers.forEach { wrestlerRepository.createWrestler(it) }

            println("✅ Sample data seeded successfully!")
            println("👤 Admin user: admin@luchacanaria.com / admin123")
            println("🏟️ Teams created: ${createdTeams.size}")
            println("🤼 Wrestlers created: ${wrestlers.size}")
        } catch (e: Exception) {
            println("❌ Error seeding sample data: ${e.message}")
        }
    }
}