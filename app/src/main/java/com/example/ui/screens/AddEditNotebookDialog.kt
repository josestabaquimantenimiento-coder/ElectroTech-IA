package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.TechnicalNotebook

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditNotebookDialog(
    notebook: TechnicalNotebook?,
    onDismiss: () -> Unit,
    onSave: (id: Long, name: String, machineCode: String, area: String, description: String, colorHex: String, iconName: String) -> Unit
) {
    var name by remember { mutableStateOf(notebook?.name ?: "") }
    var machineCode by remember { mutableStateOf(notebook?.machineCode ?: "") }
    var area by remember { mutableStateOf(notebook?.area ?: "") }
    var description by remember { mutableStateOf(notebook?.description ?: "") }
    var selectedColor by remember { mutableStateOf(notebook?.colorHex ?: "#0284C7") }
    var selectedIcon by remember { mutableStateOf(notebook?.iconName ?: "machine") }

    val colorOptions = listOf(
        "#0284C7" to "Azul Industrial",
        "#10B981" to "Verde Esmeralda",
        "#F59E0B" to "Ámbar Alerta",
        "#8B5CF6" to "Púrpura Control",
        "#06B6D4" to "Cian Neumático",
        "#EC4899" to "Rosa Seguridad",
        "#EF4444" to "Rojo Crítico"
    )

    val iconOptions = listOf(
        "machine" to (Icons.Default.PrecisionManufacturing to "Máquina"),
        "pump" to (Icons.Default.Engineering to "Bomba/Motor"),
        "bolt" to (Icons.Default.Bolt to "Variador/Potencia"),
        "memory" to (Icons.Default.Memory to "PLC/Control"),
        "settings" to (Icons.Default.Settings to "Cuadro/Maniobra"),
        "shield" to (Icons.Default.Shield to "Seguridad/LOTO")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (notebook == null) "Nuevo Cuaderno de Máquina" else "Editar Cuaderno",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Organiza manuales, memorias de taller, notas e historial de intervenciones aislados por máquina o sistema (estilo NotebookLM).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la Máquina / Sistema *") },
                    placeholder = { Text("ej. Bomba Hidráulica Grundfos CR-32") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("notebook_name_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = machineCode,
                        onValueChange = { machineCode = it },
                        label = { Text("Código *") },
                        placeholder = { Text("BOM-01") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("notebook_code_input")
                    )

                    OutlinedTextField(
                        value = area,
                        onValueChange = { area = it },
                        label = { Text("Ubicación / Área *") },
                        placeholder = { Text("Sala Calderas") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("notebook_area_input")
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción del Sistema / Equipamiento") },
                    placeholder = { Text("Grupo de presión, contactores TeSys, maniobra y protecciones...") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("notebook_desc_input")
                )

                // Color selection
                Text(
                    text = "Color de Identificación:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    colorOptions.forEach { (hex, label) ->
                        val color = try {
                            Color(android.graphics.Color.parseColor(hex))
                        } catch (e: Exception) {
                            MaterialTheme.colorScheme.primary
                        }
                        val isSelected = selectedColor.equals(hex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = hex }
                                .then(
                                    if (isSelected) {
                                        Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                    } else Modifier
                                )
                        )
                    }
                }

                // Icon selection
                Text(
                    text = "Icono del Equipo:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    iconOptions.forEach { (key, pair) ->
                        val isSelected = selectedIcon == key
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { selectedIcon = key }
                                .then(
                                    if (isSelected) {
                                        Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = pair.first,
                                contentDescription = pair.second,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            notebook?.id ?: 0L,
                            name,
                            machineCode,
                            area,
                            description,
                            selectedColor,
                            selectedIcon
                        )
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.testTag("save_notebook_button")
            ) {
                Text(if (notebook == null) "Crear Cuaderno" else "Guardar Cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
