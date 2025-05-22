plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("io.ktor.plugin") version "3.1.1"
    application
}

group = "com.luchacanaria"
version = "1.0.0"

application {
    mainClass.set("com.luchacanaria.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor BOM para gestión centralizada de versiones
    implementation(platform("io.ktor:ktor-bom:3.1.1"))

    // Ktor Core - Usando la última versión 3.1.1 (Febrero 2025)
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-compression-jvm")
    implementation("io.ktor:ktor-server-rate-limit-jvm")
    implementation("io.ktor:ktor-server-websockets-jvm")

    // Forzar versión segura de Netty (corrige CVE-2025-24970)
    implementation("io.netty:netty-all:4.1.118.Final")

    // MongoDB - Driver oficial de Kotlin (v5.5) - KMongo está deprecado
    implementation(platform("org.mongodb:mongodb-driver-bom:5.5.0"))
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine")
    implementation("org.mongodb:mongodb-driver-kotlin-extensions")

    // Serialización JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    // Validación - Konform actualizado
    implementation("io.konform:konform:0.7.0")

    // JWT y autenticación
    implementation("com.auth0:java-jwt:4.4.0")

    // Password hashing - bcrypt actualizado
    implementation("at.favre.lib:bcrypt:0.10.2")

    // Logging - Logback sin vulnerabilidades recientes
    implementation("ch.qos.logback:logback-classic:1.5.14")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Configuración
    implementation("com.typesafe:config:1.4.3")

    // Forzar versiones seguras de dependencias transitivas
    implementation("commons-codec:commons-codec:1.17.1") // Corrige CVE en 1.11
    implementation("org.apache.commons:commons-compress:1.27.1") // Corrige CVE-2024-25710, CVE-2024-26308

    // Testing - Versiones más recientes y seguras
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("io.ktor:ktor-client-content-negotiation-jvm")

    // Testcontainers - Última versión (1.21.0)
    testImplementation("org.testcontainers:testcontainers:1.21.0")
    testImplementation("org.testcontainers:mongodb:1.21.0")
    testImplementation("org.testcontainers:junit-jupiter:1.21.0")

    // JUnit y testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.1.20")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    // MockK para mocking en Kotlin
    testImplementation("io.mockk:mockk:1.13.14")

    // AssertJ para mejores assertions
    testImplementation("org.assertj:assertj-core:3.26.3")
}

// Configuración de tasks
tasks.test {
    useJUnitPlatform()

    // Configuración de memoria para tests
    jvmArgs("-Xmx2g", "-XX:MaxMetaspaceSize=512m")

    // Configuración para Testcontainers
    systemProperty("testcontainers.reuse.enable", "true")
    systemProperty("testcontainers.ryuk.disabled", "false")

    // Variables de entorno para tests
    environment("MONGODB_URI", "mongodb://localhost:27017")
    environment("JWT_SECRET", "test-secret-key")
    environment("DATABASE_NAME", "lucha_canaria_test")
}

// Configuración específica para desarrollo
tasks.withType<JavaExec> {
    systemProperty("io.ktor.development", "true")
    environment("MONGODB_URI", "mongodb://localhost:27017")
    environment("JWT_SECRET", "development-secret-key")
    environment("DATABASE_NAME", "lucha_canaria_dev")
}

// Configuración para generar JAR ejecutable
tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Main-Class" to "com.luchacanaria.ApplicationKt"
        )
    }
}

// Configuración para verificación de código - Nueva sintaxis sin deprecations
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-opt-in=io.ktor.server.plugins.CannotCreatePluginException"
        )
    }
}

// Configuración Java - Usar el Java 24 disponible en el sistema
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

// Configurar resolución de dependencias para evitar vulnerabilidades
configurations.all {
    resolutionStrategy {
        // Forzar versión segura de Netty
        force("io.netty:netty-all:4.1.118.Final")
        force("io.netty:netty-handler:4.1.118.Final")
        force("io.netty:netty-common:4.1.118.Final")
        force("io.netty:netty-codec:4.1.118.Final")
        force("io.netty:netty-transport:4.1.118.Final")

        // Forzar versiones seguras de otras dependencias
        force("commons-codec:commons-codec:1.17.1")
        force("org.apache.commons:commons-compress:1.27.1")
        force("ch.qos.logback:logback-core:1.5.14")
        force("ch.qos.logback:logback-classic:1.5.14")
    }
}