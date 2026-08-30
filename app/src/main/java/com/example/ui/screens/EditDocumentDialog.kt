package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TechnicalDocument
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishSubsurfaceAlt
import com.example.ui.theme.PolishWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditDocumentSheet(
    viewModel: MainViewModel,
    document: TechnicalDocument?,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val isEditing = document != null

    var title by remember(document) { mutableStateOf(document?.title ?: "") }
    var manufacturer by remember(document) { mutableStateOf(document?.manufacturer ?: "") }
    var model by remember(document) { mutableStateOf(document?.model ?: "") }
    var category by remember(document) { mutableStateOf(document?.category ?: "Variador VFD") }
    var pageReference by remember(document) { mutableStateOf(document?.pageReference ?: "") }
    var contentSnippet by remember(document) { mutableStateOf(document?.contentSnippet ?: "") }
    var expectedValues by remember(document) { mutableStateOf(document?.expectedValues ?: "") }
    var diagnosticProcedure by remember(document) { mutableStateOf(document?.diagnosticProcedure ?: "") }

    val categories = listOf("Variador VFD", "Protección Motor", "Bomba Hidráulica", "Autómata PLC", "Normativa", "Sensores / Instrumentación")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PolishWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Editar Archivo / Manual Técnico" else "Subir / Añadir Manual Técnico",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título del Manual o Procedimiento") },
                placeholder = { Text("ej. Diagnóstico de Variador Sinamics G120") },
                modifier = Modifier.fillMaxWidth().testTag("input_doc_title"),
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = manufacturer,
                    onValueChange = { manufacturer = it },
                    label = { Text("Fabricante") },
                    placeholder = { Text("ej. Siemens") },
                    modifier = Modifier.weight(1f).testTag("input_doc_manufacturer"),
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
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Modelo / Serie") },
                    placeholder = { Text("ej. G120 CU240E") },
                    modifier = Modifier.weight(1f).testTag("input_doc_model"),
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

            Text("Categoría:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat, fontSize = 11.sp) },
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

            OutlinedTextField(
                value = pageReference,
                onValueChange = { pageReference = it },
                label = { Text("Referencia de Archivo / Página") },
                placeholder = { Text("ej. Manual de Servicio pág. 45 - Secc. 3.2") },
                modifier = Modifier.fillMaxWidth().testTag("input_doc_page_ref"),
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
                value = contentSnippet,
                onValueChange = { contentSnippet = it },
                label = { Text("Descripción / Contenido Técnico Clave") },
                placeholder = { Text("Notas de funcionamiento, códigos de fallo, advertencias...") },
                modifier = Modifier.fillMaxWidth().testTag("input_doc_content"),
                maxLines = 3,
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
                value = expectedValues,
                onValueChange = { expectedValues = it },
                label = { Text("Valores Estándar de Ajuste / Mediciones Esperadas") },
                placeholder = { Text("ej. 230 VAC ±10%, 10 kΩ a 25°C, 180 bar") },
                modifier = Modifier.fillMaxWidth().testTag("input_doc_expected_values"),
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
                value = diagnosticProcedure,
                onValueChange = { diagnosticProcedure = it },
                label = { Text("Procedimiento de Diagnóstico y Reparación") },
                placeholder = { Text("Paso a paso: 1. Desconectar LOTO, 2. Medir bornes A1-A2, 3. Reajustar disparo...") },
                modifier = Modifier.fillMaxWidth().testTag("input_doc_procedure"),
                maxLines = 4,
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

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.saveTechnicalDocument(
                        id = document?.id ?: 0L,
                        title = title,
                        manufacturer = manufacturer,
                        model = model,
                        category = category,
                        pageReference = pageReference,
                        snippet = contentSnippet,
                        expectedValues = expectedValues,
                        procedure = diagnosticProcedure
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_save_document"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandBlue,
                    contentColor = PolishWhite
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Guardar Cambios del Manual" else "Subir y Guardar Manual",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
