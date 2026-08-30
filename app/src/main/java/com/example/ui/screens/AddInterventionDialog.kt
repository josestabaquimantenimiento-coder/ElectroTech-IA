package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InterventionEntity
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandBlueContainer
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintOnContainer
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishSubsurface
import com.example.ui.theme.PolishSubsurfaceAlt
import com.example.ui.theme.PolishWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddInterventionSheet(
    viewModel: MainViewModel,
    existingIntervention: InterventionEntity? = null,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditing = existingIntervention != null

    var equipmentName by remember(existingIntervention) { mutableStateOf(existingIntervention?.equipmentName ?: "") }
    var cabinetCode by remember(existingIntervention) { mutableStateOf(existingIntervention?.cabinetCode ?: "") }
    var failureDescription by remember(existingIntervention) { mutableStateOf(existingIntervention?.failureDescription ?: "") }
    var rootCause by remember(existingIntervention) { mutableStateOf(existingIntervention?.rootCause ?: "") }
    var actionsTaken by remember(existingIntervention) { mutableStateOf(existingIntervention?.actionsTaken ?: "") }
    var partsReplaced by remember(existingIntervention) { mutableStateOf(existingIntervention?.partsReplaced ?: "") }
    var status by remember(existingIntervention) { mutableStateOf(existingIntervention?.status ?: "Resuelto") }
    var notes by remember(existingIntervention) { mutableStateOf(existingIntervention?.technicalNotes ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PolishWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandBlueContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Handyman else Icons.Default.Build,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isEditing) "Editar Registro de Reparación" else "Registrar Reparación / Avería",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Documentación técnica de la solución y mediciones",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Equipment and Cabinet
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = equipmentName,
                    onValueChange = { equipmentName = it },
                    label = { Text("Equipo / Máquina") },
                    placeholder = { Text("ej. Bomba Hidráulica 1") },
                    modifier = Modifier.weight(1.3f).testTag("input_new_equipment"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandBlue,
                        unfocusedBorderColor = PolishBorder,
                        focusedContainerColor = PolishWhite,
                        unfocusedContainerColor = PolishSubsurfaceAlt,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                OutlinedTextField(
                    value = cabinetCode,
                    onValueChange = { cabinetCode = it },
                    label = { Text("Cuadro / Armario") },
                    placeholder = { Text("ej. ARM-04") },
                    modifier = Modifier.weight(1f).testTag("input_new_cabinet"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandBlue,
                        unfocusedBorderColor = PolishBorder,
                        focusedContainerColor = PolishWhite,
                        unfocusedContainerColor = PolishSubsurfaceAlt,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }

            // Failure description
            OutlinedTextField(
                value = failureDescription,
                onValueChange = { failureDescription = it },
                label = { Text("Avería / Síntoma Notificado") },
                placeholder = { Text("ej. Disparo térmico continuo al arrancar el motor bajo carga") },
                modifier = Modifier.fillMaxWidth().testTag("input_new_failure"),
                maxLines = 2,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandBlue,
                    unfocusedBorderColor = PolishBorder,
                    focusedContainerColor = PolishWhite,
                    unfocusedContainerColor = PolishSubsurfaceAlt,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            // Root Cause
            OutlinedTextField(
                value = rootCause,
                onValueChange = { rootCause = it },
                label = { Text("Causa Raíz Identificada") },
                placeholder = { Text("ej. Relé térmico F2 descalibrado por sobretemperatura local") },
                modifier = Modifier.fillMaxWidth().testTag("input_new_cause"),
                maxLines = 2,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandBlue,
                    unfocusedBorderColor = PolishBorder,
                    focusedContainerColor = PolishWhite,
                    unfocusedContainerColor = PolishSubsurfaceAlt,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            // HIGHLIGHTED SECTION: ¿Cómo se reparó la avería?
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MintContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MintOnContainer.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Construction,
                            contentDescription = null,
                            tint = MintOnContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "¿Cómo se reparó la avería? (Procedimiento y Mediciones)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MintOnContainer
                        )
                    }

                    Text(
                        text = "Detalla paso a paso el procedimiento técnico, comprobaciones y mediciones tomadas (Voltaje, Intensidad, Resistencia):",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    OutlinedTextField(
                        value = actionsTaken,
                        onValueChange = { actionsTaken = it },
                        label = { Text("Procedimiento de Reparación y Mediciones") },
                        placeholder = { Text("Paso 1: Se verificó tensión 400V en L1-L2-L3.\nPaso 2: Se ajustó el relé F2 de 12A a 14.5A.\nPaso 3: Se comprobó consumo en carga de 13.8A...") },
                        modifier = Modifier.fillMaxWidth().testTag("input_new_actions"),
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandBlue,
                            unfocusedBorderColor = PolishBorder,
                            focusedContainerColor = PolishWhite,
                            unfocusedContainerColor = PolishWhite,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            }

            // Replaced Parts
            OutlinedTextField(
                value = partsReplaced,
                onValueChange = { partsReplaced = it },
                label = { Text("Repuestos y Componentes Sustituidos (opcional)") },
                placeholder = { Text("ej. Relé térmico Schneider LRD-16 (12-18A), Fusible gG 20A") },
                modifier = Modifier.fillMaxWidth().testTag("input_new_parts"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandBlue,
                    unfocusedBorderColor = PolishBorder,
                    focusedContainerColor = PolishWhite,
                    unfocusedContainerColor = PolishSubsurfaceAlt,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            // Status Selector
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Estado de la Reparación:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = TextSecondary)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Resuelto", "En Diagnóstico", "Pendiente Repuesto").forEach { st ->
                        FilterChip(
                            selected = status == st,
                            onClick = { status = st },
                            label = { Text(st, fontSize = 12.sp, fontWeight = if (status == st) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlue,
                                selectedLabelColor = PolishWhite,
                                containerColor = PolishWhite,
                                labelColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // Additional Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Observaciones / Recomendaciones Futuras") },
                placeholder = { Text("ej. Programar termografía preventiva en próximo mantenimiento semestral") },
                modifier = Modifier.fillMaxWidth().testTag("input_new_notes"),
                maxLines = 2,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandBlue,
                    unfocusedBorderColor = PolishBorder,
                    focusedContainerColor = PolishWhite,
                    unfocusedContainerColor = PolishSubsurfaceAlt,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Action Button
            Button(
                onClick = {
                    if (isEditing && existingIntervention != null) {
                        viewModel.updateIntervention(
                            id = existingIntervention.id,
                            equipmentName = equipmentName,
                            cabinetCode = cabinetCode,
                            failureDescription = failureDescription,
                            rootCause = rootCause,
                            actionsTaken = actionsTaken,
                            partsReplaced = partsReplaced,
                            status = status,
                            notes = notes
                        )
                    } else {
                        viewModel.createIntervention(
                            equipmentName = equipmentName,
                            cabinetCode = cabinetCode,
                            failureDescription = failureDescription,
                            rootCause = rootCause,
                            actionsTaken = actionsTaken,
                            partsReplaced = partsReplaced,
                            status = status,
                            notes = notes
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_save_new_intervention"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandBlue,
                    contentColor = PolishWhite
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Guardar Cambios de la Reparación" else "Guardar Registro de Reparación",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
