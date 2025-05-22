package org.iesharia.features.matches.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
    override fun ScreenTitle(): String = "Acta de Enfrentamiento"

    @Composable
    override fun OnNavigateBack(): () -> Unit = { viewModel.navigateBack() }

    @Composable
    override fun TopBarActions() {
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
            ErrorStateContent(
                errorMessage = uiState.errorMessage ?: "No se encontró el enfrentamiento",
                onRetry = { viewModel.navigateBack() }
            )
        } else if (!uiState.isLoading) {
            MatchActContent(viewModel = viewModel)
        }
    }

    @Composable
    private fun ErrorStateContent(
        errorMessage: String,
        onRetry: () -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            WrestlingButton(
                text = "Volver",
                onClick = onRetry,
                type = WrestlingButtonType.SECONDARY
            )
        }
    }
}

@Composable
private fun MatchActContent(viewModel: MatchActViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = WrestlingTheme.dimensions.spacing_16),
        contentPadding = PaddingValues(bottom = 24.dp, top = 16.dp)
    ) {
        item {
            MatchActHeaderSection(viewModel = viewModel)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        item {
            MatchActTeamsSection(viewModel = viewModel)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        item {
            MatchActDevelopmentSection(viewModel = viewModel)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // NUEVA SECCIÓN DE COMENTARIOS
        item {
            MatchActCommentsSection(viewModel = viewModel)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        item {
            WrestlingButton(
                text = "Finalizar Acta",
                onClick = { viewModel.finishAct() },
                type = WrestlingButtonType.PRIMARY,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Nueva sección de comentarios siguiendo el patrón de diseño del proyecto
 */
@Composable
private fun MatchActCommentsSection(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()

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
                title = "Comentarios",
                type = SectionDividerType.PRIMARY
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Fila con las tres columnas de comentarios
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16)
            ) {
                // Comentarios Equipo Local
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Equipo Local",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = WrestlingTheme.dimensions.spacing_8)
                    )

                    WrestlingTextField(
                        value = uiState.localTeamComments,
                        onValueChange = { viewModel.setLocalTeamComments(it) },
                        label = "Comentarios del equipo local",
                        placeholder = "Ingrese observaciones sobre el equipo local...",
                        singleLine = false,
                        maxLines = 6,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Comentarios Equipo Visitante
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Equipo Visitante",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = WrestlingTheme.dimensions.spacing_8)
                    )

                    WrestlingTextField(
                        value = uiState.visitorTeamComments,
                        onValueChange = { viewModel.setVisitorTeamComments(it) },
                        label = "Comentarios del equipo visitante",
                        placeholder = "Ingrese observaciones sobre el equipo visitante...",
                        singleLine = false,
                        maxLines = 6,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Comentarios Árbitro
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Árbitro",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(bottom = WrestlingTheme.dimensions.spacing_8)
                    )

                    WrestlingTextField(
                        value = uiState.refereeComments,
                        onValueChange = { viewModel.setRefereeComments(it) },
                        label = "Comentarios del arbitraje",
                        placeholder = "Ingrese observaciones sobre el desarrollo del arbitraje...",
                        singleLine = false,
                        maxLines = 6,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

            // Nota informativa usando el estilo del proyecto
            Text(
                text = "* Los comentarios quedarán registrados en el acta oficial del enfrentamiento",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Componente reutilizable para dropdowns
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it && enabled && !readOnly },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = if (readOnly) { _ -> } else onValueChange,
            readOnly = readOnly,
            enabled = enabled,
            label = { Text(label) },
            trailingIcon = if (!readOnly) {
                { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            } else null,
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = enabled && !readOnly)
                .fillMaxWidth()
        )

        if (options.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// Componente reutilizable para campos de tiempo
@Composable
private fun TimeField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var internalValue by remember(value) { mutableStateOf(value) }

    fun validateTime(input: String): String {
        if (input.isEmpty()) return ""

        val cleanInput = input.replace(Regex("[^0-9:]"), "")

        if (":" in cleanInput) {
            val parts = cleanInput.split(":")
            if (parts.size == 2) {
                val hours = parts[0].toIntOrNull()?.coerceIn(0, 23)
                val minutes = parts[1].toIntOrNull()?.coerceIn(0, 59)

                if (hours != null && minutes != null) {
                    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
                }
            }
        } else if (cleanInput.all { it.isDigit() } && cleanInput.length <= 4) {
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

        return cleanInput
    }

    OutlinedTextField(
        value = internalValue,
        onValueChange = { newValue ->
            val cleaned = newValue.replace(Regex("[^0-9:]"), "")
            if (cleaned.length <= 5) {
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
                    val validated = validateTime(internalValue)
                    internalValue = validated
                    onValueChange(validated)
                }
            },
        placeholder = { Text("HH:MM") }
    )
}

@Composable
private fun MatchActHeaderSection(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val referees = viewModel.getAvailableReferees()

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

            // Información básica
            BasicInfoSection(viewModel, uiState)

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Fecha y lugar
            DateAndVenueSection(viewModel, uiState)

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Horarios
            TimeSection(viewModel, uiState)

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Árbitros
            RefereesSection(viewModel, uiState, referees)

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Delegado terrero
            DelegateSection(viewModel, uiState)
        }
    }
}

@Composable
private fun BasicInfoSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
    ) {
        // Competición (solo lectura)
        DropdownField(
            value = uiState.competitionName,
            onValueChange = {},
            label = "Competición",
            options = emptyList(),
            readOnly = true,
            modifier = Modifier.weight(2f)
        )

        // Temporada
        WrestlingTextField(
            value = uiState.season,
            onValueChange = { viewModel.setSeason(it) },
            label = "Temporada",
            modifier = Modifier.weight(1f)
        )

        // Tipo (Regional/Insular)
        DropdownField(
            value = if (uiState.isRegional) "Regional" else "Insular",
            onValueChange = { value ->
                when (value) {
                    "Regional" -> viewModel.setIsRegional(true)
                    "Insular" -> viewModel.setIsInsular(true)
                }
            },
            label = "Tipo",
            options = listOf("Insular", "Regional"),
            modifier = Modifier.weight(1f)
        )

        // Categoría
        DropdownField(
            value = uiState.category,
            onValueChange = { viewModel.setCategory(it) },
            label = "Categoría",
            options = listOf(
                AgeCategory.REGIONAL.displayName(),
                AgeCategory.JUVENIL.displayName()
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DateAndVenueSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
    ) {
        WrestlingTextField(
            value = uiState.venue,
            onValueChange = { viewModel.setVenue(it) },
            label = "Celebrada en",
            modifier = Modifier.weight(2f)
        )

        WrestlingTextField(
            value = uiState.day,
            onValueChange = { viewModel.setDay(it) },
            label = "Día",
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(0.5f)
        )

        WrestlingTextField(
            value = uiState.month,
            onValueChange = { viewModel.setMonth(it) },
            label = "Mes",
            modifier = Modifier.weight(1f)
        )

        WrestlingTextField(
            value = uiState.year,
            onValueChange = { viewModel.setYear(it) },
            label = "Año",
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Composable
private fun TimeSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
    ) {
        TimeField(
            value = uiState.startTime,
            onValueChange = { viewModel.setStartTime(it) },
            label = "Hora Inicial",
            modifier = Modifier.weight(1f)
        )

        TimeField(
            value = uiState.endTime,
            onValueChange = { viewModel.setEndTime(it) },
            label = "Hora Final",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RefereesSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState,
    referees: List<DomainReferee>
) {
    // Árbitro principal y licencia
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
    ) {
        DropdownField(
            value = uiState.referee,
            onValueChange = { selectedName ->
                viewModel.setReferee(selectedName)
                val referee = referees.find { it.name == selectedName }
                referee?.let { viewModel.setRefereeLicense(it.licenseNumber) }
            },
            label = "Árbitro Principal",
            options = referees.map { it.name },
            modifier = Modifier.weight(2f)
        )

        DropdownField(
            value = uiState.refereeLicense,
            onValueChange = {},
            label = "Nº Licencia",
            options = emptyList(),
            readOnly = true,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

    // Árbitros auxiliares
    AssistantRefereesSection(viewModel, uiState, referees)
}

@Composable
private fun AssistantRefereesSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState,
    referees: List<DomainReferee>
) {
    Text(
        text = "Árbitros Auxiliares",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

    // Lista de árbitros auxiliares
    if (uiState.assistantReferees.isNotEmpty()) {
        AssistantRefereesList(
            assistantReferees = uiState.assistantReferees,
            onRemove = { index -> viewModel.removeAssistantReferee(index) }
        )
    } else {
        Text(
            text = "No hay árbitros auxiliares añadidos",
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

    // Selector para añadir árbitro auxiliar
    AssistantRefereeSelector(viewModel, uiState, referees)
}

@Composable
private fun AssistantRefereesList(
    assistantReferees: List<org.iesharia.features.matches.ui.viewmodel.AssistantReferee>,
    onRemove: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
        ),
        shape = WrestlingTheme.shapes.small
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Cabecera de la tabla
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = WrestlingTheme.shapes.small
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nº",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(30.dp)
                )

                Text(
                    text = "Nombre del Árbitro",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1.5f)
                )

                Text(
                    text = "Nº Licencia",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(140.dp),
                    textAlign = TextAlign.Center
                )

                // Espacio para el botón de eliminar
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de árbitros
            assistantReferees.forEachIndexed { index, assistant ->
                AssistantRefereeRow(
                    index = index,
                    assistant = assistant,
                    onRemove = { onRemove(index) }
                )

                if (index < assistantReferees.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AssistantRefereeRow(
    index: Int,
    assistant: org.iesharia.features.matches.ui.viewmodel.AssistantReferee,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Número del árbitro
        Box(
            modifier = Modifier
                .width(30.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${index + 1}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Nombre del árbitro
        Text(
            text = assistant.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.5f)
        )

        // Número de licencia
        Box(
            modifier = Modifier
                .width(140.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
                    shape = WrestlingTheme.shapes.small
                )
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = assistant.licenseNumber,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Botón de eliminar
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar árbitro auxiliar",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun AssistantRefereeSelector(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState,
    referees: List<DomainReferee>
) {
    var selectedReferee by remember { mutableStateOf<String?>(null) }

    val availableReferees = referees.filter { referee ->
        referee.name != uiState.referee &&
                !uiState.assistantReferees.any { it.name == referee.name }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DropdownField(
            value = selectedReferee ?: "",
            onValueChange = { selectedReferee = it },
            label = "Seleccionar Árbitro",
            options = availableReferees.map { it.name },
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                selectedReferee?.let { name ->
                    val referee = availableReferees.find { it.name == name }
                    referee?.let {
                        viewModel.addAssistantReferee(it.name, it.licenseNumber)
                        selectedReferee = null
                    }
                }
            },
            enabled = selectedReferee != null,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text("Añadir")
        }
    }
}

@Composable
private fun DelegateSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
    ) {
        WrestlingTextField(
            value = uiState.fieldDelegate,
            onValueChange = { viewModel.setFieldDelegate(it) },
            label = "Delegado Terrero",
            modifier = Modifier.weight(2f)
        )

        WrestlingTextField(
            value = uiState.fieldDelegateDni,
            onValueChange = { viewModel.setFieldDelegateDni(it) },
            label = "D.N.I.",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MatchActTeamsSection(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()

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
            TeamNamesSection(viewModel, uiState)

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Tablas de luchadores
            WrestlerTablesSection(viewModel, uiState)

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Capitanes y entrenadores
            CaptainsAndCoachesSection(viewModel, uiState)
        }
    }
}

@Composable
private fun TeamNamesSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Equipo Local",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            WrestlingTextField(
                value = uiState.localClubName,
                onValueChange = { viewModel.setLocalClubName(it) },
                label = "Nombre del Club",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Equipo Visitante",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            WrestlingTextField(
                value = uiState.visitorClubName,
                onValueChange = { viewModel.setVisitorClubName(it) },
                label = "Nombre del Club",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WrestlerTablesSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16)
    ) {
        WrestlerSelectionTable(
            isLocal = true,
            wrestlers = uiState.localWrestlers,
            availableWrestlers = viewModel.getAvailableWrestlers(true),
            onWrestlerSelected = { index, wrestler ->
                viewModel.updateWrestler(index, wrestler.id, wrestler.licenseNumber, true)
            },
            modifier = Modifier.weight(1f)
        )

        WrestlerSelectionTable(
            isLocal = false,
            wrestlers = uiState.visitorWrestlers,
            availableWrestlers = viewModel.getAvailableWrestlers(false),
            onWrestlerSelected = { index, wrestler ->
                viewModel.updateWrestler(index, wrestler.id, wrestler.licenseNumber, false)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CaptainsAndCoachesSection(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16)
    ) {
        // Equipo Local
        Column(modifier = Modifier.weight(1f)) {
            val localWrestlerNames = viewModel.getAvailableWrestlers(true).map { it.fullName }

            DropdownField(
                value = uiState.localCaptain,
                onValueChange = { viewModel.setLocalCaptain(it) },
                label = "Capitán Local",
                options = localWrestlerNames,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                value = uiState.localCoach,
                onValueChange = { viewModel.setLocalCoach(it) },
                label = "Entrenador Local",
                options = listOf("Otro...") + localWrestlerNames,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Equipo Visitante
        Column(modifier = Modifier.weight(1f)) {
            val visitorWrestlerNames = viewModel.getAvailableWrestlers(false).map { it.fullName }

            DropdownField(
                value = uiState.visitorCaptain,
                onValueChange = { viewModel.setVisitorCaptain(it) },
                label = "Capitán Visitante",
                options = visitorWrestlerNames,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                value = uiState.visitorCoach,
                onValueChange = { viewModel.setVisitorCoach(it) },
                label = "Entrenador Visitante",
                options = listOf("Otro...") + visitorWrestlerNames,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WrestlerSelectionTable(
    isLocal: Boolean,
    wrestlers: List<MatchActWrestler>,
    availableWrestlers: List<Wrestler>,
    onWrestlerSelected: (Int, Wrestler) -> Unit,
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
                onWrestlerSelected = { wrestler -> onWrestlerSelected(index, wrestler) }
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun WrestlerSelectionRow(
    index: Int,
    wrestler: MatchActWrestler,
    availableWrestlers: List<Wrestler>,
    onWrestlerSelected: (Wrestler) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${index + 1}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )

        val selectedWrestler = availableWrestlers.find { it.licenseNumber == wrestler.licenseNumber }

        DropdownField(
            value = selectedWrestler?.fullName ?: "",
            onValueChange = { selectedName ->
                val selectedWrestlerObject = availableWrestlers.find { it.fullName == selectedName }
                selectedWrestlerObject?.let { onWrestlerSelected(it) }
            },
            label = "Luchador",
            options = availableWrestlers.map { it.fullName },
            modifier = Modifier.weight(2f)
        )

        DropdownField(
            value = wrestler.licenseNumber,
            onValueChange = {},
            label = "Licencia",
            options = emptyList(),
            readOnly = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MatchActDevelopmentSection(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Calcular resultado final automáticamente
    val calculatedLocalScore = uiState.bouts
        .mapNotNull { bout -> bout.localScore.toIntOrNull() }
        .maxOrNull() ?: 0

    val calculatedVisitorScore = uiState.bouts
        .mapNotNull { bout -> bout.visitorScore.toIntOrNull() }
        .maxOrNull() ?: 0

    // Actualizar automáticamente los resultados finales
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
            BoutDevelopmentTable(viewModel, uiState)

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))

            // Resultado final automático
            FinalResultSection(calculatedLocalScore, calculatedVisitorScore)
        }
    }
}

@Composable
private fun BoutDevelopmentTable(
    viewModel: MatchActViewModel,
    uiState: org.iesharia.features.matches.ui.viewmodel.MatchActUiState
) {
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
        // Cabecera de la tabla
        BoutTableHeader()

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // Filas de datos
        uiState.bouts.forEachIndexed { index, bout ->
            MatchBoutRow(
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

        WrestlingButton(
            text = "Añadir Lucha",
            onClick = { viewModel.addBout() },
            type = WrestlingButtonType.SECONDARY,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun BoutTableHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Columnas para equipo local
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Club Local",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = WrestlingTheme.shapes.small
                    )
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            repeat(3) { i ->
                Text(
                    text = "${i + 1}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "Amonestaciones",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        // Columnas para equipo visitante
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Club Visitante",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        shape = WrestlingTheme.shapes.small
                    )
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            repeat(3) { i ->
                Text(
                    text = "${i + 1}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "Amonestaciones",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        // Columna de resultado
        Row(
            modifier = Modifier.width(90.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "L",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = WrestlingTheme.shapes.small
                    )
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "V",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        shape = WrestlingTheme.shapes.small
                    )
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(38.dp)) // Espacio para botón eliminar
    }
}

@Composable
private fun MatchBoutRow(
    bout: MatchActBout,
    localWrestlers: List<MatchActWrestler>,
    visitorWrestlers: List<MatchActWrestler>,
    availableLocalWrestlers: List<Wrestler>,
    availableVisitorWrestlers: List<Wrestler>,
    onUpdate: (MatchActBout) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Datos del equipo local - pasar weight como modifier
        BoutTeamRow(
            isLocal = true,
            bout = bout,
            wrestlers = localWrestlers,
            availableWrestlers = availableLocalWrestlers,
            onUpdate = onUpdate,
            modifier = Modifier.weight(1f) // Pasar weight aquí
        )

        // Datos del equipo visitante - pasar weight como modifier
        BoutTeamRow(
            isLocal = false,
            bout = bout,
            wrestlers = visitorWrestlers,
            availableWrestlers = availableVisitorWrestlers,
            onUpdate = onUpdate,
            modifier = Modifier.weight(1f) // Pasar weight aquí
        )

        // Marcadores
        BoutScoreSection(bout = bout, onUpdate = onUpdate)

        // Botón eliminar
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(38.dp)
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
private fun BoutTeamRow(
    isLocal: Boolean,
    bout: MatchActBout,
    wrestlers: List<MatchActWrestler>,
    availableWrestlers: List<Wrestler>,
    onUpdate: (MatchActBout) -> Unit,
    modifier: Modifier = Modifier // Añadir parámetro modifier
) {
    Row(
        modifier = modifier, // Usar el modifier pasado como parámetro
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Selector de luchador
        val wrestlerNumber = if (isLocal) bout.localWrestlerNumber else bout.visitorWrestlerNumber
        val selectedWrestlerIndex = wrestlerNumber.toIntOrNull()?.minus(1)
        val selectedWrestler = if (selectedWrestlerIndex != null && selectedWrestlerIndex < wrestlers.size) {
            val matchWrestler = wrestlers[selectedWrestlerIndex]
            availableWrestlers.find { it.licenseNumber == matchWrestler.licenseNumber }
        } else null

        DropdownField(
            value = selectedWrestler?.let { wrestler ->
                "${wrestler.classification.displayName()} - ${wrestler.fullName}"
            } ?: "",
            onValueChange = { selectedValue ->
                // Extraer el número del luchador basado en la selección
                val wrestlerOptions = wrestlers.mapIndexedNotNull { index, matchWrestler ->
                    val wrestler = availableWrestlers.find { it.licenseNumber == matchWrestler.licenseNumber }
                    wrestler?.let {
                        "${it.classification.displayName()} - ${it.fullName}" to (index + 1)
                    }
                }

                val selectedIndex = wrestlerOptions.find { it.first == selectedValue }?.second
                selectedIndex?.let { index ->
                    if (isLocal) {
                        onUpdate(bout.copy(localWrestlerNumber = index.toString()))
                    } else {
                        onUpdate(bout.copy(visitorWrestlerNumber = index.toString()))
                    }
                }
            },
            label = if (isLocal) "Luchador Local" else "Luchador Visitante",
            options = wrestlers.mapIndexedNotNull { _, matchWrestler ->
                val wrestler = availableWrestlers.find { it.licenseNumber == matchWrestler.licenseNumber }
                wrestler?.let {
                    "${it.classification.displayName()} - ${it.fullName}"
                }
            },
            modifier = Modifier.weight(1f)
        )

        // Checkboxes para agarradas
        val checks = if (isLocal) {
            listOf(bout.localCheck1, bout.localCheck2, bout.localCheck3)
        } else {
            listOf(bout.visitorCheck1, bout.visitorCheck2, bout.visitorCheck3)
        }

        checks.forEachIndexed { index, checked ->
            MatchBoutCheckbox(
                checked = checked,
                onCheckedChange = { newChecked ->
                    when {
                        isLocal -> when (index) {
                            0 -> onUpdate(bout.copy(localCheck1 = newChecked))
                            1 -> onUpdate(bout.copy(localCheck2 = newChecked))
                            2 -> onUpdate(bout.copy(localCheck3 = newChecked))
                        }
                        else -> when (index) {
                            0 -> onUpdate(bout.copy(visitorCheck1 = newChecked))
                            1 -> onUpdate(bout.copy(visitorCheck2 = newChecked))
                            2 -> onUpdate(bout.copy(visitorCheck3 = newChecked))
                        }
                    }
                },
                modifier = Modifier.width(40.dp)
            )
        }

        // Campo de amonestaciones
        val penalty = if (isLocal) bout.localPenalty else bout.visitorPenalty
        WrestlingTextField(
            value = penalty,
            onValueChange = { newValue ->
                if (isLocal) {
                    onUpdate(bout.copy(localPenalty = newValue))
                } else {
                    onUpdate(bout.copy(visitorPenalty = newValue))
                }
            },
            keyboardType = KeyboardType.Number,
            modifier = Modifier.width(80.dp),
            label = ""
        )
    }
}

@Composable
private fun BoutScoreSection(
    bout: MatchActBout,
    onUpdate: (MatchActBout) -> Unit
) {
    Row(
        modifier = Modifier.width(90.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        WrestlingTextField(
            value = bout.localScore,
            onValueChange = { newValue ->
                val cleanValue = newValue.filter { it.isDigit() }
                if (cleanValue.length <= 2) {
                    onUpdate(bout.copy(localScore = cleanValue))
                }
            },
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f),
            label = ""
        )

        WrestlingTextField(
            value = bout.visitorScore,
            onValueChange = { newValue ->
                val cleanValue = newValue.filter { it.isDigit() }
                if (cleanValue.length <= 2) {
                    onUpdate(bout.copy(visitorScore = cleanValue))
                }
            },
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f),
            label = ""
        )
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
            .padding(vertical = 12.dp),
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

@Composable
private fun FinalResultSection(
    localScore: Int,
    visitorScore: Int
) {
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

        DropdownField(
            value = localScore.toString(),
            onValueChange = {},
            label = "Club Local",
            options = emptyList(),
            readOnly = true,
            modifier = Modifier.weight(1f)
        )

        DropdownField(
            value = visitorScore.toString(),
            onValueChange = {},
            label = "Club Visitante",
            options = emptyList(),
            readOnly = true,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "* El resultado final corresponde al último marcador registrado en la tabla",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}