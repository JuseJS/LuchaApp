package org.iesharia.config

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import java.io.File

object EnvironmentConfig {
    val dotenv: Dotenv by lazy {
        // Primero verificar si ya tenemos variables de entorno cargadas por systemd
        if (System.getenv("DOTENV_PATH") != null) {
            println("Variables de entorno ya cargadas por systemd")
            // Crear una instancia que solo use variables del sistema
            dotenv {
                ignoreIfMissing = true
                systemProperties = true
            }
        } else {
            // Modo desarrollo: buscar archivo .env
            val envLocations = listOf(
                "../.env",
                ".env",
                "../../.env"
            )

            var loadedDotenv: Dotenv? = null

            for (location in envLocations) {
                val file = File(location)
                if (file.exists() && file.canRead()) {
                    println("Loading .env from: ${file.absolutePath}")
                    loadedDotenv = dotenv {
                        directory = file.parentFile?.absolutePath ?: "."
                        filename = file.name
                        ignoreIfMissing = false
                    }
                    break
                }
            }

            if (loadedDotenv == null) {
                println("Warning: .env file not found, using system environment variables")
                loadedDotenv = dotenv {
                    ignoreIfMissing = true
                    systemProperties = true
                }
            }

            loadedDotenv
        }
    }

    fun getEnv(key: String, default: String? = null): String? {
        // Primero buscar en variables del sistema (cargadas por systemd)
        return System.getenv(key) ?: dotenv[key] ?: default
    }

    fun requireEnv(key: String): String {
        return getEnv(key) ?: throw IllegalStateException("$key not found in environment")
    }
}