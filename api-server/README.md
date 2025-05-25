# LuchaApp API Server

API REST para la aplicación de gestión de Lucha Canaria, construida con Kotlin y Ktor.

## Tecnologías

- **Kotlin** 2.1.20
- **Ktor** 3.0.3 - Framework web
- **MongoDB** - Base de datos NoSQL
- **Koin** - Inyección de dependencias
- **JWT** - Autenticación
- **Kotlinx Serialization** - Serialización JSON

## Requisitos

- Java 17+
- MongoDB 7.0+
- Ubuntu Server 24.04 (para producción)

## Configuración

1. Copia el archivo `.env.example` a `.env` en el directorio padre:
   ```bash
   cp ../.env.example ../.env
   ```

2. Edita el archivo `.env` con tus configuraciones:
   - Credenciales de MongoDB
   - Secreto JWT
   - Configuración de CORS

## Desarrollo Local

1. Inicia MongoDB localmente (sin SSL):
   ```bash
   mongod --bind_ip 127.0.0.1 --port 27017
   ```

2. Inicializa la base de datos:
   ```bash
   mongosh < scripts/init-mongodb.js
   ```

3. Ejecuta la API:
   ```bash
   ./gradlew run
   ```

La API estará disponible en `http://localhost:8080`

## Construcción

Para construir el JAR ejecutable:
```bash
./gradlew shadowJar
```

El archivo JAR se generará en `build/libs/lucha-api-server-1.0.0.jar`

## Despliegue en Ubuntu Server

1. Ejecuta el script de configuración en el servidor:
   ```bash
   sudo ./scripts/setup-ubuntu.sh
   ```

2. Desde tu máquina local, despliega la aplicación:
   ```bash
   SERVER_HOST=tu-servidor-ip ./scripts/deploy.sh
   ```

## Endpoints Principales

### Autenticación
- `POST /api/v1/auth/login` - Iniciar sesión
- `POST /api/v1/auth/register` - Registrar usuario

### Usuarios
- `GET /api/v1/users/current` - Usuario actual
- `PUT /api/v1/users/{id}/role` - Actualizar rol

### Luchadores
- `GET /api/v1/wrestlers` - Listar luchadores
- `GET /api/v1/wrestlers/{id}` - Detalle de luchador
- `POST /api/v1/wrestlers` - Crear luchador
- `PUT /api/v1/wrestlers/{id}` - Actualizar luchador

### Equipos
- `GET /api/v1/teams` - Listar equipos
- `GET /api/v1/teams/{id}` - Detalle de equipo
- `POST /api/v1/teams` - Crear equipo
- `PUT /api/v1/teams/{id}` - Actualizar equipo

### Competiciones
- `GET /api/v1/competitions` - Listar competiciones
- `GET /api/v1/competitions/{id}` - Detalle de competición

### Enfrentamientos
- `GET /api/v1/matches/{id}` - Detalle de enfrentamiento
- `GET /api/v1/matches/{id}/details` - Información detallada

### Actas
- `GET /api/v1/match-acts/{id}` - Obtener acta
- `POST /api/v1/match-acts` - Guardar acta
- `PUT /api/v1/match-acts/{id}/complete` - Completar acta

## Estructura del Proyecto

```
api-server/
├── src/main/kotlin/org/iesharia/
│   ├── config/         # Configuración de Ktor
│   ├── data/          # Capa de datos
│   │   ├── models/    # Modelos de MongoDB
│   │   └── repositories/
│   ├── domain/        # Lógica de negocio
│   │   ├── models/    # DTOs
│   │   └── services/
│   ├── presentation/  # Capa de presentación
│   │   └── routes/    # Endpoints
│   └── utils/         # Utilidades
├── scripts/           # Scripts de despliegue
└── build.gradle.kts
```

## Seguridad

- Autenticación mediante JWT
- Contraseñas hasheadas con BCrypt
- CORS configurado
- Sin SSL (para desarrollo/entornos controlados)

## Monitoreo

- Health check: `GET /api/v1/health`
- Logs en `/var/log/luchaapp/` (producción)
- Rotación de logs configurada

## Usuario por Defecto

- **Username**: admin
- **Password**: Admin123!
- **Rol**: ADMIN

Cambia la contraseña después del primer inicio de sesión.