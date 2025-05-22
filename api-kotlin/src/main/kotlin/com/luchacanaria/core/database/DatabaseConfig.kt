package com.luchacanaria.core.database

import com.luchacanaria.features.auth.data.document.UserDocument
import com.luchacanaria.features.competitions.data.document.CompetitionDocument
import com.luchacanaria.features.matches.data.document.MatchDocument
import com.luchacanaria.features.teams.data.document.TeamDocument
import com.luchacanaria.features.wrestlers.data.document.WrestlerDocument
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

object DatabaseConfig {
    private val connectionString = System.getenv("MONGODB_URI")
        ?: "mongodb://localhost:27017"

    private val databaseName = System.getenv("DATABASE_NAME")
        ?: "lucha_canaria"

    private val codecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    private val clientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .codecRegistry(codecRegistry)
        .build()

    private val client = MongoClient.create(clientSettings)
    val database = client.getDatabase(databaseName)

    // Colecciones tipadas
    val wrestlersCollection = database.getCollection<WrestlerDocument>("wrestlers")
    val teamsCollection = database.getCollection<TeamDocument>("teams")
    val competitionsCollection = database.getCollection<CompetitionDocument>("competitions")
    val matchesCollection = database.getCollection<MatchDocument>("matches")
    val usersCollection = database.getCollection<UserDocument>("users")

    fun init() {
        println("🗄️ Base de datos MongoDB inicializada")
        println("📍 URI: $connectionString")
        println("🏷️ Base de datos: $databaseName")
    }
}
