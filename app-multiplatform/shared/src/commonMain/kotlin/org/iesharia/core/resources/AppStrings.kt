package org.iesharia.core.resources

/**
 * Objeto centralizado para todos los textos de la aplicación
 */
object AppStrings {
    /**
     * Strings comunes en toda la aplicación
     */
    object Common {
        val appName = "Lucha Canaria"
        val appSubtitle = "Tradición y Deporte"
        val appLogoDesc = "Logo de Lucha Canaria"

        // Botones y acciones
        val accept = "Aceptar"
        val cancel = "Cancelar"
        val apply = "Aplicar"
        val clear = "Limpiar"
        val save = "Guardar"
        val edit = "Editar"
        val delete = "Eliminar"
    }

    /**
     * Strings para la sección de autenticación
     */
    object Auth {
        val login = "Iniciar Sesión"
        val register = "Registrarse"
        val createAccount = "Crear Cuenta"
        val email = "Correo electrónico"
        val password = "Contraseña"
        val confirmPassword = "Confirmar contraseña"
        val name = "Nombre"
        val surname = "Apellidos"
        val showPassword = "Mostrar contraseña"
        val hidePassword = "Ocultar contraseña"

        // Mensajes sobre cuenta
        val noAccount = "¿No tienes cuenta? Regístrate"
        val alreadyHaveAccount = "¿Ya tienes cuenta? Inicia sesión"

        // Mensajes de error
        val emailRequired = "El correo electrónico es obligatorio"
        val invalidEmail = "Correo electrónico inválido"
        val passwordRequired = "La contraseña es obligatoria"
        val passwordTooShort = "La contraseña debe tener al menos 6 caracteres"
        val passwordsDontMatch = "Las contraseñas no coinciden"
        val nameRequired = "El nombre es obligatorio"
        val surnameRequired = "Los apellidos son obligatorios"
        val loginError = "Error al iniciar sesión: "
        val registerError = "Error al registrarse: "
    }

    /**
     * Strings para la pantalla de inicio
     */
    object Home {
        val favorites = "Favoritos"
        val competitions = "Competiciones"
        val teams = "Equipos"
        val wrestlers = "Luchadores"
        val all = "Todos"

        // Texto de estado vacío
        val noFavorites = "No tienes %s en favoritos"
        val noCompetitionsWithFilter = "No hay competiciones activas con los filtros seleccionados"
    }

    /**
     * Strings para la sección de competiciones
     */
    object Competitions {
        val activeCompetitions = "Competiciones Activas"
        val filterCompetitions = "Filtrar competiciones"
        val clearFilters = "Limpiar filtros"
        val category = "Categoría"
        val division = "División"
        val island = "Isla"
        val season = "Temporada %s"
        val lastMatchDay = "Última jornada (%d)"
        val nextMatchDay = "Próxima jornada (%d)"
        val favoriteCompetitions = "Competiciones Favoritas"
    }

    /**
     * Strings para la sección de equipos
     */
    object Teams {
        val vs = "vs"
        val favoriteTeams = "Equipos Favoritos"
        val islandLabel = "Isla: %s"
        val lastMatches = "Últimos Enfrentamientos"
        val nextMatches = "Próximos Enfrentamientos"
        val noRecentMatches = "No hay enfrentamientos recientes"
    }

    /**
     * Strings para la sección de luchadores
     */
    object Wrestlers {
        val vs = "VS"
        val favoriteWrestlers = "Luchadores Favoritos"
        val recentMatches = "Últimos enfrentamientos"
        val agarradas = "Agarradas"
        val foulsFormat = "%d faltas"
        val separated = "Separada"
        val noResult = "Sin resultado"

        // Resultados de enfrentamientos
        object Results {
            val win = "Victoria"
            val loss = "Derrota"
            val draw = "Empate"
            val expelled = "Expulsión por faltas"
            val separated = "Separación"
        }
    }
}