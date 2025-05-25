package org.iesharia.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.codecs.pojo.Conventions
import java.util.concurrent.TimeUnit

object DatabaseConfig {
    val mongoUri = EnvironmentConfig.getEnv("MONGODB_URI", "mongodb://localhost:27017")!!
    val databaseName = EnvironmentConfig.getEnv("DATABASE_NAME", "lucha_canaria")!!
    
    fun createMongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoUri)
        
        val pojoCodecProvider = PojoCodecProvider.builder()
            .automatic(true)
            .conventions(listOf(
                Conventions.ANNOTATION_CONVENTION,
                Conventions.CLASS_AND_PROPERTY_CONVENTION,
                Conventions.SET_PRIVATE_FIELDS_CONVENTION
            ))
            .build()
            
        val codecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(pojoCodecProvider)
        )
        
        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyToConnectionPoolSettings { builder ->
                builder
                    .maxSize(50)
                    .minSize(10)
                    .maxWaitTime(60, TimeUnit.SECONDS)
                    .maxConnectionLifeTime(30, TimeUnit.MINUTES)
                    .maxConnectionIdleTime(10, TimeUnit.MINUTES)
            }
            .applyToSocketSettings { builder ->
                builder
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
            }
            .retryWrites(true)
            .build()
            
        return MongoClient.create(settings)
    }
    
    fun getDatabase(client: MongoClient = createMongoClient()): MongoDatabase {
        return client.getDatabase(databaseName)
    }
}