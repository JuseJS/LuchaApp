package org.iesharia.features.matches.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.iesharia.features.matches.ui.viewmodel.MatchActBout
import org.iesharia.features.matches.ui.viewmodel.MatchActViewModel
import org.koin.core.parameter.parametersOf

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

        // Sección de luchadores
        item {
            MatchActWrestlersSection(viewModel = viewModel, isLocal = true)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        item {
            MatchActWrestlersSection(viewModel = viewModel, isLocal = false)
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

@Composable
private fun MatchActHeader(viewModel: MatchActViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val match = uiState.match ?: return

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

            // Fila 1: Tipo de competición
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_16)
            ) {
                // Checkbox Insular
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = uiState.isInsular,
                        onCheckedChange = { viewModel.setIsInsular(it) }
                    )
                    Text(
                        text = "Insular",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Checkbox Regional
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = uiState.isRegional,
                        onCheckedChange = { viewModel.setIsRegional(it) }
                    )
                    Text(
                        text = "Regional",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Fila 2: Datos de la competición
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // Temporada
                WrestlingTextField(
                    value = uiState.season,
                    onValueChange = { viewModel.setSeason(it) },
                    label = "Temporada",
                    modifier = Modifier.weight(1f)
                )

                // Categoría
                WrestlingTextField(
                    value = uiState.category,
                    onValueChange = { viewModel.setCategory(it) },
                    label = "Categoría",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            // Fila 3: Nombre de competición
            WrestlingTextField(
                value = uiState.competitionName,
                onValueChange = { viewModel.setCompetitionName(it) },
                label = "Competición",
                modifier = Modifier.fillMaxWidth()
            )

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

            // Fila 5: Hora y árbitros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // Hora inicial
                WrestlingTextField(
                    value = uiState.startTime,
                    onValueChange = { viewModel.setStartTime(it) },
                    label = "Hora Inicial",
                    modifier = Modifier.weight(1f)
                )

                // Hora final
                WrestlingTextField(
                    value = uiState.endTime,
                    onValueChange = { viewModel.setEndTime(it) },
                    label = "Hora Final",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            // Fila 6: Árbitro y delegado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // Árbitro
                WrestlingTextField(
                    value = uiState.referee,
                    onValueChange = { viewModel.setReferee(it) },
                    label = "Árbitro",
                    modifier = Modifier.weight(2f)
                )

                // Nº Licencia
                WrestlingTextField(
                    value = uiState.refereeLicense,
                    onValueChange = { viewModel.setRefereeLicense(it) },
                    label = "Nº Licencia",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

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

@Composable
private fun MatchActWrestlersSection(viewModel: MatchActViewModel, isLocal: Boolean) {
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
                title = if (isLocal) "Luchadores Equipo Local" else "Luchadores Equipo Visitante",
                type = SectionDividerType.PRIMARY,
                textColor = if (isLocal)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Tabla de luchadores
            val wrestlers = if (isLocal) uiState.localWrestlers else uiState.visitorWrestlers

            // Cabecera de tabla
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isLocal)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
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
                    text = "Club",
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

            // Filas de luchadores
            wrestlers.forEachIndexed { index, wrestler ->
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

                    // Nombre del club (solo editable en la primera fila)
                    if (index == 0) {
                        WrestlingTextField(
                            value = if (isLocal) uiState.localClubName else uiState.visitorClubName,
                            onValueChange = {
                                if (isLocal) viewModel.setLocalClubName(it)
                                else viewModel.setVisitorClubName(it)
                            },
                            label = "Club",
                            modifier = Modifier.weight(2f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(2f))
                    }

                    // Número de licencia
                    WrestlingTextField(
                        value = wrestler.licenseNumber,
                        onValueChange = {
                            viewModel.updateWrestlerLicense(index, it, isLocal)
                        },
                        label = "Nº Licencia",
                        modifier = Modifier.weight(1f)
                    )

                    // Botón eliminar
                    IconButton(
                        onClick = { viewModel.removeWrestler(index, isLocal) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para añadir luchador
            WrestlingButton(
                text = "Añadir Luchador",
                onClick = { viewModel.addWrestler(isLocal) },
                type = WrestlingButtonType.SECONDARY,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Capitán y Entrenador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                // Capitán
                WrestlingTextField(
                    value = if (isLocal) uiState.localCaptain else uiState.visitorCaptain,
                    onValueChange = {
                        if (isLocal) viewModel.setLocalCaptain(it)
                        else viewModel.setVisitorCaptain(it)
                    },
                    label = "Capitán",
                    modifier = Modifier.weight(1f)
                )

                // Entrenador
                WrestlingTextField(
                    value = if (isLocal) uiState.localCoach else uiState.visitorCoach,
                    onValueChange = {
                        if (isLocal) viewModel.setLocalCoach(it)
                        else viewModel.setVisitorCoach(it)
                    },
                    label = "Entrenador",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MatchActDevelopmentSection(viewModel: MatchActViewModel) {
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
                    .padding(8.dp)
            ) {
                // Cabecera de la tabla
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Primera columna - Número
                    Text(
                        text = "Nº",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.width(30.dp),
                        textAlign = TextAlign.Center
                    )

                    // Columnas para equipo local
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Club Local",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Nº",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "2",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "A",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Columnas para equipo visitante
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Club Visitante",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Nº",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "2",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "A",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Columna de resultado
                    Column(
                        modifier = Modifier.width(60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Resultado",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "L",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "V",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Filas de la tabla (por cada lucha)
                uiState.bouts.forEachIndexed { index, bout ->
                    MatchBoutRow(
                        boutIndex = index,
                        bout = bout,
                        onUpdate = { updatedBout ->
                            viewModel.updateBout(index, updatedBout)
                        },
                        onDelete = {
                            viewModel.removeBout(index)
                        }
                    )

                    if (index < uiState.bouts.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para añadir lucha
                WrestlingButton(
                    text = "Añadir Lucha",
                    onClick = { viewModel.addBout() },
                    type = WrestlingButtonType.SECONDARY,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))

            // Resultado final
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

                WrestlingTextField(
                    value = uiState.localTeamScore,
                    onValueChange = { viewModel.setLocalTeamScore(it) },
                    label = "Club Local",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                WrestlingTextField(
                    value = uiState.visitorTeamScore,
                    onValueChange = { viewModel.setVisitorTeamScore(it) },
                    label = "Club Visitante",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MatchBoutRow(
    boutIndex: Int,
    bout: MatchActBout,
    onUpdate: (MatchActBout) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Número de lucha
        Text(
            text = "${boutIndex + 1}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )

        // Datos del equipo local
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Número de luchador
            WrestlingTextField(
                value = bout.localWrestlerNumber,
                onValueChange = {
                    onUpdate(bout.copy(localWrestlerNumber = it))
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f),
                label = ""
            )

            // Casilla 1
            MatchBoutCheckbox(
                checked = bout.localCheck1,
                onCheckedChange = {
                    onUpdate(bout.copy(localCheck1 = it))
                },
                modifier = Modifier.width(24.dp)
            )

            // Casilla 2
            MatchBoutCheckbox(
                checked = bout.localCheck2,
                onCheckedChange = {
                    onUpdate(bout.copy(localCheck2 = it))
                },
                modifier = Modifier.width(24.dp)
            )

            // Amonestación
            WrestlingTextField(
                value = bout.localPenalty,
                onValueChange = {
                    onUpdate(bout.copy(localPenalty = it))
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.width(24.dp),
                label = ""
            )
        }

        // Datos del equipo visitante
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Número de luchador
            WrestlingTextField(
                value = bout.visitorWrestlerNumber,
                onValueChange = {
                    onUpdate(bout.copy(visitorWrestlerNumber = it))
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f),
                label = ""
            )

            // Casilla 1
            MatchBoutCheckbox(
                checked = bout.visitorCheck1,
                onCheckedChange = {
                    onUpdate(bout.copy(visitorCheck1 = it))
                },
                modifier = Modifier.width(24.dp)
            )

            // Casilla 2
            MatchBoutCheckbox(
                checked = bout.visitorCheck2,
                onCheckedChange = {
                    onUpdate(bout.copy(visitorCheck2 = it))
                },
                modifier = Modifier.width(24.dp)
            )

            // Amonestación
            WrestlingTextField(
                value = bout.visitorPenalty,
                onValueChange = {
                    onUpdate(bout.copy(visitorPenalty = it))
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.width(24.dp),
                label = ""
            )
        }

        // Resultado
        Row(
            modifier = Modifier.width(60.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Victoria local
            MatchBoutCheckbox(
                checked = bout.localWin,
                onCheckedChange = {
                    onUpdate(bout.copy(localWin = it, visitorWin = if (it) false else bout.visitorWin))
                },
                modifier = Modifier.weight(1f)
            )

            // Victoria visitante
            MatchBoutCheckbox(
                checked = bout.visitorWin,
                onCheckedChange = {
                    onUpdate(bout.copy(visitorWin = it, localWin = if (it) false else bout.localWin))
                },
                modifier = Modifier.weight(1f)
            )
        }

        // Botón eliminar
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
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
            .size(24.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = WrestlingTheme.shapes.small
            )
            .background(
                color = if (checked)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                else
                    Color.Transparent,
                shape = WrestlingTheme.shapes.small
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Marcado",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}