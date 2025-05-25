import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    application
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.iesharia"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    // Ktor - Versión compatible con Koin 4.0.0
    val ktorVersion = "2.3.12"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    // Routing está incluido en ktor-server-core en Ktor 2.x
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")

    // ⚠️ CORRECCIÓN DE VULNERABILIDADES DE SEGURIDAD ⚠️
    // Forzar versiones seguras de Netty para resolver CVE-2025-24970 y CVE-2025-25193
    val nettyVersion = "4.1.121.Final"
    implementation("io.netty:netty-common:$nettyVersion")
    implementation("io.netty:netty-handler:$nettyVersion")
    implementation("io.netty:netty-buffer:$nettyVersion")
    implementation("io.netty:netty-transport:$nettyVersion")
    implementation("io.netty:netty-codec:$nettyVersion")
    implementation("io.netty:netty-codec-http:$nettyVersion")
    implementation("io.netty:netty-codec-http2:$nettyVersion")
    implementation("io.netty:netty-resolver:$nettyVersion")
    implementation("io.netty:netty-transport-native-epoll:$nettyVersion")
    implementation("io.netty:netty-transport-native-unix-common:$nettyVersion")

    // Forzar versión segura de commons-codec para resolver WS-2019-0379
    implementation("commons-codec:commons-codec:1.18.0")

    // MongoDB - Versión actual
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.5.0")
    implementation("org.mongodb:bson-kotlinx:5.5.0")

    // Koin for Dependency Injection - Compatible con Ktor 3.x
    val koinVersion = "3.5.3"
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    // BCrypt for password hashing
    implementation("at.favre.lib:bcrypt:0.10.2")

    // JWT
    implementation("com.auth0:java-jwt:4.4.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.18")

    // Environment variables
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")

    // Testing
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.1.20")
    testImplementation("io.mockk:mockk:1.13.14")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")
}

application {
    mainClass.set("org.iesharia.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-opt-in=kotlin.RequiresOptIn"
        )
    }
}

// Configuración de resolución de conflictos para garantizar versiones seguras
configurations.all {
    resolutionStrategy {
        // Forzar versiones específicas para resolver vulnerabilidades de seguridad
        force(
            "io.netty:netty-common:4.1.121.Final",
            "io.netty:netty-handler:4.1.121.Final",
            "io.netty:netty-buffer:4.1.121.Final",
            "io.netty:netty-transport:4.1.121.Final",
            "io.netty:netty-codec:4.1.121.Final",
            "io.netty:netty-codec-http:4.1.121.Final",
            "io.netty:netty-codec-http2:4.1.121.Final",
            "io.netty:netty-resolver:4.1.121.Final",
            "commons-codec:commons-codec:1.18.0"
        )

        // Registro de cambios de versión para auditoría
        eachDependency {
            if (requested.group == "io.netty") {
                useVersion("4.1.121.Final")
                because("Resolving CVE-2025-24970 and CVE-2025-25193")
            }
            if (requested.group == "commons-codec" && requested.name == "commons-codec") {
                useVersion("1.18.0")
                because("Resolving WS-2019-0379")
            }
        }
    }
}

tasks.shadowJar {
    archiveBaseName.set("luchaapp-api")
    archiveClassifier.set("all")
    archiveVersion.set("")
    mergeServiceFiles()

    // Asegurar que todas las dependencias se incluyan
    configurations = listOf(project.configurations.runtimeClasspath.get())

    // Manifest con Main-Class
    manifest {
        attributes["Main-Class"] = "org.iesharia.ApplicationKt"
    }

    // Incluir recursos
    from(sourceSets.main.get().resources)

    // Configuraciones adicionales para evitar conflictos
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
}

tasks.test {
    useJUnitPlatform()
}

// Tarea para verificar versiones de dependencias (útil para auditoría de seguridad)
tasks.register("checkSecurityVersions") {
    doLast {
        println("=== VERIFICACIÓN DE VERSIONES DE SEGURIDAD ===")
        configurations.runtimeClasspath.get().resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
            val moduleVersion = artifact.moduleVersion.id
            when {
                moduleVersion.group == "io.netty" -> {
                    println("✅ Netty: ${moduleVersion.group}:${moduleVersion.name}:${moduleVersion.version}")
                }
                moduleVersion.group == "commons-codec" && moduleVersion.name == "commons-codec" -> {
                    println("✅ Commons Codec: ${moduleVersion.group}:${moduleVersion.name}:${moduleVersion.version}")
                }
            }
        }
        println("===============================================")
    }
}