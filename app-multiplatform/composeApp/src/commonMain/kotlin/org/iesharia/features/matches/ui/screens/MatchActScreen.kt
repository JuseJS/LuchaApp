package org.iesharia.features.matches.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.WrestlingButton
import org.iesharia.core.ui.components.WrestlingButtonType
import org.iesharia.core.ui.components.WrestlingTextField
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.matches.ui.viewmodel.MatchActBout
import org.iesharia.features.matches.ui.viewmodel.MatchActViewModel
import org.iesharia.features.matches.ui.viewmodel.MatchActWrestler
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.koin.core.parameter.parametersOf
import org.iesharia.features.matches.domain.model.Referee as DomainReferee

class MatchActScreen(private val matchId: String) : BaseContentScreen() {

    private lateinit var viewModel: MatchActViewModel

    @Composable
    override fun SetupViewModel() {
        viewModel = rememberViewModel<MatchActViewModel> { parametersOf(matchId) }
    }

    @Composable
    override fun ScreenTitle(): String {
        val uiState by viewModel.uiState.collectAsState()
        return "Acta de Enfrentamiento"
    }

    @Composable
    override fun OnNavigateBack(): () -> Unit {
        return { viewModel.navigateBack() }
    }

    @Composable
    override fun TopBarActions() {
        val uiState by viewModel.uiState.collectAsState()

        IconButton(onClick = { viewModel.saveAct() }) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Guardar Acta",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    @Composable
    override fun IsLoading(): Boolean {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.isLoading
    }

    @Composable
    override fun ContentImpl() {
        val uiState by viewModel.uiState.collectAsState()

        if (uiState.match == null && !uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.errorMessage ?: "No se encontró el enfrentamiento",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(16.dp))

                WrestlingButton(
                    text = "Volver",
                    onClick = { viewModel.navigateBack() },
                    type = WrestlingButtonType.SECONDARY
                )
            }
        } else if (!uiState.isLoading) {
            MatchActContent(viewModel = viewModel)
        }
    }
}

@Composable
private fun MatchActContent(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val match = uiState.match ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = WrestlingTheme.dimensions.spacing_16),
        contentPadding = PaddingValues(bottom = 24.dp, top = 16.dp)
    ) {
        // Encabezado
        item {
            MatchActHeader(viewModel = viewModel)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Sección de luchadores - ahora con la nueva interfaz mejorada
        item {
            MatchActWrestlersImprovedSection(viewModel = viewModel)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Sección de desarrollo de la luchada
        item {
            MatchActDevelopmentSection(viewModel = viewModel)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Botón de finalizar
        item {
            WrestlingButton(
                text = "Finalizar Acta",
                onClick = { viewModel.finishAct() },
                type = WrestlingButtonType.PRIMARY,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MatchActHeader(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val match = uiState.match ?: return
    val competitions = viewModel.getAvailableCompetitions()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = WrestlingTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            SectionDivider(
                title = "Acta de la Luchada",
                type = SectionDividerType.PRIMARY
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // CAMBIO: Usar el nombre de la competición en lugar del ID
                // Dropdown para regional/insular
                val typeOptions = listOf("Insular", "Regional")
                var typeExpanded by remember { mutableStateOf(false) }
                val selectedMatchType = if (uiState.isRegional) "Regional" else "Insular"

                OutlinedTextField(
                    value = uiState.competitionName,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.weight(2f),
                    label = { Text("Competición") }
                )

                // Temporada
                WrestlingTextField(
                    value = uiState.season,
                    onValueChange = { viewModel.setSeason(it) },
                    label = "Temporada",
                    modifier = Modifier.weight(1f)
                )

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedMatchType,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("Tipo") }
                    )

                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        typeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    if (option == "Regional") {
                                        viewModel.setIsRegional(true)
                                    } else {
                                        viewModel.setIsInsular(true)
                                    }
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }

                // Dropdown para categoría
                var categoryExpanded by remember { mutableStateOf(false) }
                val categories = listOf(
                    AgeCategory.REGIONAL.displayName(),
                    AgeCategory.JUVENIL.displayName()
                )

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = uiState.category,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        label = { Text("Categoría") }
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.setCategory(option)
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Fila 4: Lugar y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // Lugar
                WrestlingTextField(
                    value = uiState.venue,
                    onValueChange = { viewModel.setVenue(it) },
                    label = "Celebrada en",
                    modifier = Modifier.weight(2f)
                )

                // Día
                WrestlingTextField(
                    value = uiState.day,
                    onValueChange = { viewModel.setDay(it) },
                    label = "Día",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(0.5f)
                )

                // Mes
                WrestlingTextField(
                    value = uiState.month,
                    onValueChange = { viewModel.setMonth(it) },
                    label = "Mes",
                    modifier = Modifier.weight(1f)
                )

                // Año
                WrestlingTextField(
                    value = uiState.year,
                    onValueChange = { viewModel.setYear(it) },
                    label = "Año",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(0.7f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // CAMBIO: Selectores de hora mejorados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // Hora inicial
                TimePickerField(
                    label = "Hora Inicial",
                    time = uiState.startTime,
                    onTimeSelected = { viewModel.setStartTime(it) },
                    modifier = Modifier.weight(1f)
                )

                // Hora final
                TimePickerField(
                    label = "Hora Final",
                    time = uiState.endTime,
                    onTimeSelected = { viewModel.setEndTime(it) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            // CAMBIO: Selector de árbitro y licencia en la misma fila
            val referees = viewModel.getAvailableReferees()
            var refereeExpanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // Selector de árbitro
                ExposedDropdownMenuBox(
                    expanded = refereeExpanded,
                    onExpandedChange = { refereeExpanded = it },
                    modifier = Modifier.weight(2f)
                ) {
                    OutlinedTextField(
                        value = uiState.referee,
                        onValueChange = { viewModel.setReferee(it) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = refereeExpanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        label = { Text("Árbitro Principal") }
                    )

                    ExposedDropdownMenu(
                        expanded = refereeExpanded,
                        onDismissRequest = { refereeExpanded = false }
                    ) {
                        referees.forEach { domainReferee ->
                            val referee = Referee(domainReferee)
                            DropdownMenuItem(
                                text = { Text(referee.name) },
                                onClick = {
                                    viewModel.setReferee(referee.name)
                                    viewModel.setRefereeLicense(referee.licenseNumber)
                                    refereeExpanded = false
                                }
                            )
                        }
                    }
                }

                // Nº Licencia (Solo lectura, se autocomplementa al seleccionar árbitro)
                OutlinedTextField(
                    value = uiState.refereeLicense,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Nº Licencia") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Sección de árbitros auxiliares - CÓDIGO ACTUALIZADO
            Text(
                text = "Árbitros Auxiliares",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            // Tabla de árbitros auxiliares - Solo se muestra si hay árbitros
            if (uiState.assistantReferees.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    uiState.assistantReferees.forEachIndexed { index, assistant ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Número del árbitro
                            Text(
                                text = "${index + 1}.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(40.dp)
                            )

                            // Nombre del árbitro
                            Text(
                                text = assistant.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )

                            // Licencia
                            Text(
                                text = assistant.licenseNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(120.dp)
                            )

                            // Botón eliminar
                            IconButton(
                                onClick = { viewModel.removeAssistantReferee(index) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        if (index < uiState.assistantReferees.size - 1) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            } else {
                // Mensaje cuando no hay árbitros
                Text(
                    text = "Aquí iría la tabla con los árbitros",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selector para añadir un árbitro auxiliar - Siempre visible
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var assistantRefExpanded by remember { mutableStateOf(false) }
                var selectedAssistant by remember { mutableStateOf<Referee?>(null) }

                // VERSIÓN CORREGIDA: Usamos ExposedDropdownMenuBox que es el componente oficial
                ExposedDropdownMenuBox(
                    expanded = assistantRefExpanded,
                    onExpandedChange = { assistantRefExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedAssistant?.name ?: "",
                        onValueChange = { /* Solo lectura */ },
                        readOnly = true, // Importante: debe ser de solo lectura
                        label = { Text("Seleccionar Árbitro") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = assistantRefExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor() // Importante: esto conecta el campo con el menú
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = assistantRefExpanded,
                        onDismissRequest = { assistantRefExpanded = false }
                    ) {
                        // Obtener árbitros disponibles (que no sean el principal ni ya estén añadidos)
                        val availableReferees = viewModel.getAvailableReferees().filter { domainReferee ->
                            domainReferee.name != uiState.referee &&
                                    !uiState.assistantReferees.any { it.name == domainReferee.name }
                        }.map { Referee(it) }

                        if (availableReferees.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("No hay árbitros disponibles") },
                                onClick = { /* No hacer nada */ },
                                enabled = false
                            )
                        } else {
                            availableReferees.forEach { referee ->
                                DropdownMenuItem(
                                    text = { Text(referee.name) },
                                    onClick = {
                                        selectedAssistant = referee
                                        assistantRefExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Botón de añadir
                Button(
                    onClick = {
                        selectedAssistant?.let {
                            viewModel.addAssistantReferee(it.name, it.licenseNumber)
                            selectedAssistant = null
                        }
                    },
                    enabled = selectedAssistant != null,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Añadir")
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Fila 7: Delegado terrero y DNI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // Delegado terrero
                WrestlingTextField(
                    value = uiState.fieldDelegate,
                    onValueChange = { viewModel.setFieldDelegate(it) },
                    label = "Delegado Terrero",
                    modifier = Modifier.weight(2f)
                )

                // DNI
                WrestlingTextField(
                    value = uiState.fieldDelegateDni,
                    onValueChange = { viewModel.setFieldDelegateDni(it) },
                    label = "D.N.I.",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// CAMBIO: Clase para manejar árbitros en la UI
data class Referee(val name: String, val licenseNumber: String) {
    constructor(domainReferee: DomainReferee) : this(
        name = domainReferee.name,
        licenseNumber = domainReferee.licenseNumber
    )
}

// CAMBIO: Widget para selección de hora usando entrada de texto simple
@Composable
fun TimePickerField(
    label: String,
    time: String,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var internalValue by remember(time) { mutableStateOf(time) }

    // Función de validación más simple que solo valida al perder el foco
    fun validateTimeOnFocusLost(input: String): String {
        if (input.isEmpty()) return ""

        // Limpiar caracteres no válidos
        val cleanInput = input.replace(Regex("[^0-9:]"), "")

        // Si contiene ":", validar formato HH:MM
        if (":" in cleanInput) {
            val parts = cleanInput.split(":")
            if (parts.size == 2) {
                val hours = parts[0].toIntOrNull()?.coerceIn(0, 23)
                val minutes = parts[1].toIntOrNull()?.coerceIn(0, 59)

                if (hours != null && minutes != null) {
                    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
                }
            }
        }
        // Si son solo números, intentar formatear como HHMM
        else if (cleanInput.all { it.isDigit() } && cleanInput.length <= 4) {
            when (cleanInput.length) {
                1, 2 -> {
                    val hours = cleanInput.toIntOrNull()?.coerceIn(0, 23)
                    if (hours != null) {
                        return "${hours.toString().padStart(2, '0')}:00"
                    }
                }
                3 -> {
                    val hours = cleanInput.substring(0, 1).toIntOrNull()?.coerceIn(0, 23)
                    val minutes = cleanInput.substring(1, 3).toIntOrNull()?.coerceIn(0, 59)
                    if (hours != null && minutes != null) {
                        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
                    }
                }
                4 -> {
                    val hours = cleanInput.substring(0, 2).toIntOrNull()?.coerceIn(0, 23)
                    val minutes = cleanInput.substring(2, 4).toIntOrNull()?.coerceIn(0, 59)
                    if (hours != null && minutes != null) {
                        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
                    }
                }
            }
        }

        // Si no se puede validar, devolver el input limpio
        return cleanInput
    }

    OutlinedTextField(
        value = internalValue,
        onValueChange = { newValue ->
            // Solo aplicar limpieza básica, no formateo completo
            val cleaned = newValue.replace(Regex("[^0-9:]"), "")
            if (cleaned.length <= 5) { // Máximo "HH:MM"
                internalValue = cleaned
            }
        },
        label = { Text(label) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Horario"
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    // Solo validar y formatear cuando pierde el foco
                    val validated = validateTimeOnFocusLost(internalValue)
                    internalValue = validated
                    onTimeSelected(validated)
                }
            },
        placeholder = { Text("HH:MM") }
    )
}

// CAMBIO IMPORTANTE: Nueva implementación de la sección de luchadores con tablas y selección mediante dropdown
@Composable
private fun MatchActWrestlersImprovedSection(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val localTeam = uiState.match?.localTeam
    val visitorTeam = uiState.match?.visitorTeam

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = WrestlingTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            SectionDivider(
                title = "Alineaciones de Equipos",
                type = SectionDividerType.PRIMARY
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Nombres de equipos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16)
            ) {
                // Equipo Local
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Equipo Local",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nombre del club local
                    WrestlingTextField(
                        value = uiState.localClubName,
                        onValueChange = { viewModel.setLocalClubName(it) },
                        label = "Nombre del Club",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Equipo Visitante
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Equipo Visitante",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nombre del club visitante
                    WrestlingTextField(
                        value = uiState.visitorClubName,
                        onValueChange = { viewModel.setVisitorClubName(it) },
                        label = "Nombre del Club",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Tablas de luchadores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16)
            ) {
                // Tabla de luchadores locales
                WrestlerSelectionTable(
                    isLocal = true,
                    teamName = localTeam?.name ?: "",
                    wrestlers = uiState.localWrestlers,
                    availableWrestlers = viewModel.getAvailableWrestlers(true),
                    onWrestlerSelected = { index, wrestler ->
                        viewModel.updateWrestler(index, wrestler.id, wrestler.licenseNumber, true)
                    },
                    onRemoveWrestler = { index ->
                        viewModel.removeWrestler(index, true)
                    },
                    modifier = Modifier.weight(1f)
                )

                // Tabla de luchadores visitantes
                WrestlerSelectionTable(
                    isLocal = false,
                    teamName = visitorTeam?.name ?: "",
                    wrestlers = uiState.visitorWrestlers,
                    availableWrestlers = viewModel.getAvailableWrestlers(false),
                    onWrestlerSelected = { index, wrestler ->
                        viewModel.updateWrestler(index, wrestler.id, wrestler.licenseNumber, false)
                    },
                    onRemoveWrestler = { index ->
                        viewModel.removeWrestler(index, false)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Capitán y Entrenador
            @OptIn(ExperimentalMaterial3Api::class)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16)
            ) {
                // Capitán y Entrenador Local
                Column(modifier = Modifier.weight(1f)) {
                    // Selector de capitán local
                    val localWrestlerNames = viewModel.getAvailableWrestlers(true).map { it.fullName }
                    var localCaptainExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = localCaptainExpanded,
                        onExpandedChange = { localCaptainExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = uiState.localCaptain,
                            onValueChange = { viewModel.setLocalCaptain(it) },
                            label = { Text("Capitán Local") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = localCaptainExpanded)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                        )

                        ExposedDropdownMenu(
                            expanded = localCaptainExpanded,
                            onDismissRequest = { localCaptainExpanded = false }
                        ) {
                            localWrestlerNames.forEach { name ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        viewModel.setLocalCaptain(name)
                                        localCaptainExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Selector de entrenador local
                    var localCoachExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = localCoachExpanded,
                        onExpandedChange = { localCoachExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = uiState.localCoach,
                            onValueChange = { viewModel.setLocalCoach(it) },
                            label = { Text("Entrenador Local") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = localCoachExpanded)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                        )

                        ExposedDropdownMenu(
                            expanded = localCoachExpanded,
                            onDismissRequest = { localCoachExpanded = false }
                        ) {
                            // Añadir opción vacía para permitir entrada personalizada
                            DropdownMenuItem(
                                text = { Text("Otro...") },
                                onClick = {
                                    localCoachExpanded = false
                                }
                            )

                            // Añadir capitanes como posibles entrenadores
                            localWrestlerNames.forEach { name ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        viewModel.setLocalCoach(name)
                                        localCoachExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Capitán y Entrenador Visitante
                Column(modifier = Modifier.weight(1f)) {
                    // Selector de capitán visitante
                    val visitorWrestlerNames = viewModel.getAvailableWrestlers(false).map { it.fullName }
                    var visitorCaptainExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = visitorCaptainExpanded,
                        onExpandedChange = { visitorCaptainExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = uiState.visitorCaptain,
                            onValueChange = { viewModel.setVisitorCaptain(it) },
                            label = { Text("Capitán Visitante") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = visitorCaptainExpanded)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                        )

                        ExposedDropdownMenu(
                            expanded = visitorCaptainExpanded,
                            onDismissRequest = { visitorCaptainExpanded = false }
                        ) {
                            visitorWrestlerNames.forEach { name ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        viewModel.setVisitorCaptain(name)
                                        visitorCaptainExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Selector de entrenador visitante
                    var visitorCoachExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = visitorCoachExpanded,
                        onExpandedChange = { visitorCoachExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = uiState.visitorCoach,
                            onValueChange = { viewModel.setVisitorCoach(it) },
                            label = { Text("Entrenador Visitante") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = visitorCoachExpanded)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                        )

                        ExposedDropdownMenu(
                            expanded = visitorCoachExpanded,
                            onDismissRequest = { visitorCoachExpanded = false }
                        ) {
                            // Añadir opción vacía para permitir entrada personalizada
                            DropdownMenuItem(
                                text = { Text("Otro...") },
                                onClick = {
                                    visitorCoachExpanded = false
                                }
                            )

                            // Añadir luchadores como posibles entrenadores
                            visitorWrestlerNames.forEach { name ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        viewModel.setVisitorCoach(name)
                                        visitorCoachExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Tabla de selección de luchadores por equipo
@Composable
private fun WrestlerSelectionTable(
    isLocal: Boolean,
    teamName: String,
    wrestlers: List<MatchActWrestler>,
    availableWrestlers: List<Wrestler>,
    onWrestlerSelected: (Int, Wrestler) -> Unit,
    onRemoveWrestler: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val teamColor = if (isLocal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

    Column(modifier = modifier) {
        // Cabecera de la tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = teamColor.copy(alpha = 0.2f),
                    shape = WrestlingTheme.shapes.small
                )
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Nº",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Luchador",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(2f)
            )
            Text(
                text = "Nº Licencia",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filas de luchadores (12 filas en total)
        val wrestlerRows = if (wrestlers.size < 12) {
            wrestlers + List(12 - wrestlers.size) { MatchActWrestler() }
        } else {
            wrestlers.take(12)
        }

        wrestlerRows.forEachIndexed { index, wrestlerData ->
            WrestlerSelectionRow(
                index = index,
                wrestler = wrestlerData,
                availableWrestlers = availableWrestlers,
                onWrestlerSelected = { wrestler -> onWrestlerSelected(index, wrestler) },
                onRemove = { if (wrestlers.size > index) onRemoveWrestler(index) },
                teamColor = teamColor
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

// Fila individual para selección de luchador
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WrestlerSelectionRow(
    index: Int,
    wrestler: MatchActWrestler,
    availableWrestlers: List<Wrestler>,
    onWrestlerSelected: (Wrestler) -> Unit,
    onRemove: () -> Unit,
    teamColor: Color
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Número
        Text(
            text = "${index + 1}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )

        // Selector de luchador
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = it },
            modifier = Modifier.weight(2f)
        ) {
            // Buscar el luchador actual en la lista de disponibles
            val selectedWrestler = availableWrestlers.find { it.licenseNumber == wrestler.licenseNumber }

            OutlinedTextField(
                value = selectedWrestler?.fullName ?: "",
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                label = { Text("Luchador") },
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                availableWrestlers.forEach { availableWrestler ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = availableWrestler.fullName,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            onWrestlerSelected(availableWrestler)
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Número de licencia (solo lectura, se autocomplementa al seleccionar luchador)
        OutlinedTextField(
            value = wrestler.licenseNumber,
            onValueChange = { },
            readOnly = true,
            label = { Text("Licencia") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
    }
}

@Composable
private fun MatchActDevelopmentSection(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Calcular resultado final como el último marcador registrado
    val calculatedLocalScore = uiState.bouts
        .mapNotNull { bout -> bout.localScore.toIntOrNull() }
        .maxOrNull() ?: 0

// Para el equipo visitante: encuentra el máximo de todos los marcadores visitantes
    val calculatedVisitorScore = uiState.bouts
        .mapNotNull { bout -> bout.visitorScore.toIntOrNull() }
        .maxOrNull() ?: 0

    // Actualizar automáticamente los resultados finales cuando cambien los marcadores
    LaunchedEffect(calculatedLocalScore, calculatedVisitorScore) {
        viewModel.setLocalTeamScore(calculatedLocalScore.toString())
        viewModel.setVisitorTeamScore(calculatedVisitorScore.toString())
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = WrestlingTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            SectionDivider(
                title = "Desarrollo de la Luchada",
                type = SectionDividerType.PRIMARY
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Tabla de desarrollo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = WrestlingTheme.shapes.small
                    )
                    .padding(12.dp)
            ) {
                // Cabecera de la tabla - MEJORADA con espaciado exacto
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Columnas para equipo local
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Club Local",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .weight(1f) // Flexible para el luchador
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = WrestlingTheme.shapes.small
                                )
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "1",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "2",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "3",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Amonest.",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(80.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Columnas para equipo visitante
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Club Visitante",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .weight(1f) // Flexible para el luchador
                                .background(
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                    shape = WrestlingTheme.shapes.small
                                )
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "1",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "2",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "3",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Amonest.",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(80.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Columna de resultado mejorada
                    Row(
                        modifier = Modifier.width(90.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "L",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = WrestlingTheme.shapes.small
                                )
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "V",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                    shape = WrestlingTheme.shapes.small
                                )
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Espacio para el botón de eliminar
                    Spacer(modifier = Modifier.width(38.dp)) // Ajustado para IconButton
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Filas de la tabla (por cada lucha)
                uiState.bouts.forEachIndexed { index, bout ->
                    MatchBoutRow(
                        boutIndex = index,
                        bout = bout,
                        localWrestlers = uiState.localWrestlers,
                        visitorWrestlers = uiState.visitorWrestlers,
                        availableLocalWrestlers = viewModel.getAvailableWrestlers(true),
                        availableVisitorWrestlers = viewModel.getAvailableWrestlers(false),
                        onUpdate = { updatedBout ->
                            viewModel.updateBout(index, updatedBout)
                        },
                        onDelete = {
                            viewModel.removeBout(index)
                        }
                    )

                    if (index < uiState.bouts.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 6.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón para añadir lucha
                WrestlingButton(
                    text = "Añadir Lucha",
                    onClick = { viewModel.addBout() },
                    type = WrestlingButtonType.SECONDARY,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))

            // Resultado final - ACTUALIZADO para mostrar cálculo automático
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Resultado Final:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Resultado calculado automáticamente (solo lectura)
                OutlinedTextField(
                    value = calculatedLocalScore.toString(),
                    onValueChange = { /* Solo lectura */ },
                    readOnly = true,
                    label = { Text("Club Local") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        disabledLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = false
                )

                OutlinedTextField(
                    value = calculatedVisitorScore.toString(),
                    onValueChange = { /* Solo lectura */ },
                    readOnly = true,
                    label = { Text("Club Visitante") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        disabledLabelColor = MaterialTheme.colorScheme.secondary
                    ),
                    enabled = false
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texto explicativo
            Text(
                text = "* El resultado final corresponde al último marcador registrado en la tabla",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MatchBoutRow(
    boutIndex: Int,
    bout: MatchActBout,
    localWrestlers: List<MatchActWrestler>,
    visitorWrestlers: List<MatchActWrestler>,
    availableLocalWrestlers: List<Wrestler>,
    availableVisitorWrestlers: List<Wrestler>,
    onUpdate: (MatchActBout) -> Unit,
    onDelete: () -> Unit
) {
    // Usar IntrinsicSize para que todos los elementos tengan la misma altura naturalmente
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // CLAVE: permite que todos los elementos se adapten a la altura del más alto
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Datos del equipo local
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selector de luchador local
            var localWrestlerExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = localWrestlerExpanded,
                onExpandedChange = { localWrestlerExpanded = it },
                modifier = Modifier.weight(1f) // Flexible, coincide con la cabecera
            ) {
                // Buscar el luchador seleccionado basado en el número del bout
                val selectedWrestlerIndex = bout.localWrestlerNumber.toIntOrNull()?.minus(1)
                val selectedLocalWrestler = if (selectedWrestlerIndex != null && selectedWrestlerIndex < localWrestlers.size) {
                    val matchWrestler = localWrestlers[selectedWrestlerIndex]
                    availableLocalWrestlers.find { it.licenseNumber == matchWrestler.licenseNumber }
                } else null

                OutlinedTextField(
                    value = selectedLocalWrestler?.let { wrestler ->
                        "${wrestler.classification?.displayName() ?: "Sin Cat."} - ${wrestler.fullName}"
                    } ?: "",
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = localWrestlerExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .fillMaxHeight(), // Ocupa toda la altura disponible
                    label = { Text("Luchador Local", style = MaterialTheme.typography.bodySmall) },
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = localWrestlerExpanded,
                    onDismissRequest = { localWrestlerExpanded = false }
                ) {
                    // Mostrar todos los luchadores de la alineación local
                    localWrestlers.forEachIndexed { index, matchWrestler ->
                        val wrestler = availableLocalWrestlers.find { it.licenseNumber == matchWrestler.licenseNumber }
                        if (wrestler != null) {
                            DropdownMenuItem(
                                text = {
                                    Text("${wrestler.classification?.displayName() ?: "Sin Cat."} - ${wrestler.fullName} - ${wrestler.licenseNumber}")
                                },
                                onClick = {
                                    onUpdate(bout.copy(localWrestlerNumber = (index + 1).toString()))
                                    localWrestlerExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Agarrada 1 local
            MatchBoutCheckbox(
                checked = bout.localCheck1,
                onCheckedChange = {
                    onUpdate(bout.copy(localCheck1 = it))
                },
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight() // Ocupa toda la altura disponible
            )

            // Agarrada 2 local
            MatchBoutCheckbox(
                checked = bout.localCheck2,
                onCheckedChange = {
                    onUpdate(bout.copy(localCheck2 = it))
                },
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight() // Ocupa toda la altura disponible
            )

            // Agarrada 3 local
            MatchBoutCheckbox(
                checked = bout.localCheck3,
                onCheckedChange = {
                    onUpdate(bout.copy(localCheck3 = it))
                },
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight() // Ocupa toda la altura disponible
            )

            // Amonestaciones local
            WrestlingTextField(
                value = bout.localPenalty,
                onValueChange = {
                    onUpdate(bout.copy(localPenalty = it))
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(), // Ocupa toda la altura disponible
                label = ""
            )
        }

        // Datos del equipo visitante
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selector de luchador visitante
            var visitorWrestlerExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = visitorWrestlerExpanded,
                onExpandedChange = { visitorWrestlerExpanded = it },
                modifier = Modifier.weight(1f) // Flexible, coincide con la cabecera
            ) {
                // Buscar el luchador seleccionado basado en el número del bout
                val selectedWrestlerIndex = bout.visitorWrestlerNumber.toIntOrNull()?.minus(1)
                val selectedVisitorWrestler = if (selectedWrestlerIndex != null && selectedWrestlerIndex < visitorWrestlers.size) {
                    val matchWrestler = visitorWrestlers[selectedWrestlerIndex]
                    availableVisitorWrestlers.find { it.licenseNumber == matchWrestler.licenseNumber }
                } else null

                OutlinedTextField(
                    value = selectedVisitorWrestler?.let { wrestler ->
                        "${wrestler.classification?.displayName() ?: "Sin Cat."} - ${wrestler.fullName}"
                    } ?: "",
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = visitorWrestlerExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .fillMaxHeight(), // Ocupa toda la altura disponible
                    label = { Text("Luchador Visitante", style = MaterialTheme.typography.bodySmall) },
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = visitorWrestlerExpanded,
                    onDismissRequest = { visitorWrestlerExpanded = false }
                ) {
                    // Mostrar todos los luchadores de la alineación visitante
                    visitorWrestlers.forEachIndexed { index, matchWrestler ->
                        val wrestler = availableVisitorWrestlers.find { it.licenseNumber == matchWrestler.licenseNumber }
                        if (wrestler != null) {
                            DropdownMenuItem(
                                text = {
                                    Text("${wrestler.classification?.displayName() ?: "Sin Cat."} - ${wrestler.fullName} - ${wrestler.licenseNumber}")
                                },
                                onClick = {
                                    onUpdate(bout.copy(visitorWrestlerNumber = (index + 1).toString()))
                                    visitorWrestlerExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Agarrada 1 visitante
            MatchBoutCheckbox(
                checked = bout.visitorCheck1,
                onCheckedChange = {
                    onUpdate(bout.copy(visitorCheck1 = it))
                },
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight() // Ocupa toda la altura disponible
            )

            // Agarrada 2 visitante
            MatchBoutCheckbox(
                checked = bout.visitorCheck2,
                onCheckedChange = {
                    onUpdate(bout.copy(visitorCheck2 = it))
                },
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight() // Ocupa toda la altura disponible
            )

            // Agarrada 3 visitante
            MatchBoutCheckbox(
                checked = bout.visitorCheck3,
                onCheckedChange = {
                    onUpdate(bout.copy(visitorCheck3 = it))
                },
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight() // Ocupa toda la altura disponible
            )

            // Amonestaciones visitante
            WrestlingTextField(
                value = bout.visitorPenalty,
                onValueChange = {
                    onUpdate(bout.copy(visitorPenalty = it))
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(), // Ocupa toda la altura disponible
                label = ""
            )
        }

        // CORRECCIÓN PRINCIPAL: Campos de marcador acumulado separados y editables
        Row(
            modifier = Modifier
                .width(90.dp)
                .fillMaxHeight(), // Ocupa toda la altura disponible
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Marcador acumulado equipo local
            WrestlingTextField(
                value = bout.localScore,
                onValueChange = { newValue ->
                    // Validar que sea un número válido
                    val cleanValue = newValue.filter { it.isDigit() }
                    if (cleanValue.length <= 2) { // Máximo 2 dígitos
                        onUpdate(bout.copy(localScore = cleanValue))
                    }
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), // Ocupa toda la altura disponible
                label = ""
            )

            // Marcador acumulado equipo visitante
            WrestlingTextField(
                value = bout.visitorScore,
                onValueChange = { newValue ->
                    // Validar que sea un número válido
                    val cleanValue = newValue.filter { it.isDigit() }
                    if (cleanValue.length <= 2) { // Máximo 2 dígitos
                        onUpdate(bout.copy(visitorScore = cleanValue))
                    }
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), // Ocupa toda la altura disponible
                label = ""
            )
        }

        // Botón eliminar
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .size(38.dp) // Tamaño fijo para coincidir con el espacio en la cabecera
                .fillMaxHeight() // Ocupa toda la altura disponible
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun MatchBoutCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentHeight() // Permite que tenga su altura natural
            .clip(WrestlingTheme.shapes.small)
            .border(
                width = 1.dp,
                color = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = WrestlingTheme.shapes.small
            )
            .background(
                color = if (checked)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                else
                    MaterialTheme.colorScheme.surface,
                shape = WrestlingTheme.shapes.small
            )
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp), // Padding interno para dar altura suficiente
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Marcado",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}