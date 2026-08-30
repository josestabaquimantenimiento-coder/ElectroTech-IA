package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.local.AiMemoryEntity
import com.example.data.local.AppDatabase
import com.example.data.local.InterventionEntity
import com.example.data.local.NotebookNote
import com.example.data.local.TechnicalDocument
import com.example.data.local.TechnicalNotebook
import com.example.data.models.DiagnosticQuestion
import com.example.data.models.RagDiagnosisResult
import com.example.data.models.VisionDiagnosisResult
import com.example.data.models.VoiceActionType
import com.example.data.models.VoiceResponseResult
import com.example.data.models.WebSearchResult
import com.example.data.repository.MaintenanceRepository
import com.example.util.ImageUtils
import com.example.util.TtsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiInterventionFilter(
    val query: String = "",
    val status: String = "Todos"
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = MaintenanceRepository(database.interventionDao())
    val ttsHelper = TtsHelper(application)

    // Global Nav: 0: Visión, 1: Búsqueda Web, 2: Manuales RAG, 3: Memorias IA, 4: Historial
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // 1. Vision State
    private val _selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedBitmap: StateFlow<Bitmap?> = _selectedBitmap.asStateFlow()

    private val _selectedSampleName = MutableStateFlow<String?>("Esquema Contactor K1 y Relé F2")
    val selectedSampleName: StateFlow<String?> = _selectedSampleName.asStateFlow()

    private val _visionPrompt = MutableStateFlow("Tengo un contactor K1 y relé F2 que no arranca. ¿Qué pruebas de tensión y bornes debo medir?")
    val visionPrompt: StateFlow<String> = _visionPrompt.asStateFlow()

    private val _isVisionLoading = MutableStateFlow(false)
    val isVisionLoading: StateFlow<Boolean> = _isVisionLoading.asStateFlow()

    private val _visionResult = MutableStateFlow<VisionDiagnosisResult?>(null)
    val visionResult: StateFlow<VisionDiagnosisResult?> = _visionResult.asStateFlow()

    private val _activeQuestions = MutableStateFlow<List<DiagnosticQuestion>>(emptyList())
    val activeQuestions: StateFlow<List<DiagnosticQuestion>> = _activeQuestions.asStateFlow()

    // 2. Web Search State
    private val _webSearchQuery = MutableStateFlow("Variador Siemens Sinamics G120 fallo F07900 y solución")
    val webSearchQuery: StateFlow<String> = _webSearchQuery.asStateFlow()

    private val _isWebSearchLoading = MutableStateFlow(false)
    val isWebSearchLoading: StateFlow<Boolean> = _isWebSearchLoading.asStateFlow()

    private val _webSearchResult = MutableStateFlow<WebSearchResult?>(null)
    val webSearchResult: StateFlow<WebSearchResult?> = _webSearchResult.asStateFlow()

    // 2.1 NotebookLM Machine Notebooks State
    val allNotebooks: StateFlow<List<TechnicalNotebook>> = repository.allNotebooks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedNotebookIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedNotebookIds: StateFlow<Set<Long>> = _selectedNotebookIds.asStateFlow()

    val isAllNotebooksSelected: StateFlow<Boolean> = combine(
        repository.allNotebooks,
        _selectedNotebookIds
    ) { all, selected ->
        selected.isEmpty() || (all.isNotEmpty() && selected.size == all.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _activeNotebookForWorkspace = MutableStateFlow<TechnicalNotebook?>(null)
    val activeNotebookForWorkspace: StateFlow<TechnicalNotebook?> = _activeNotebookForWorkspace.asStateFlow()

    private val _activeNotebookSummary = MutableStateFlow<String?>(null)
    val activeNotebookSummary: StateFlow<String?> = _activeNotebookSummary.asStateFlow()

    private val _isGeneratingNotebookSummary = MutableStateFlow(false)
    val isGeneratingNotebookSummary: StateFlow<Boolean> = _isGeneratingNotebookSummary.asStateFlow()

    private val _showAddNotebookDialog = MutableStateFlow(false)
    val showAddNotebookDialog: StateFlow<Boolean> = _showAddNotebookDialog.asStateFlow()

    private val _editingNotebook = MutableStateFlow<TechnicalNotebook?>(null)
    val editingNotebook: StateFlow<TechnicalNotebook?> = _editingNotebook.asStateFlow()

    private val _showAddNoteDialog = MutableStateFlow(false)
    val showAddNoteDialog: StateFlow<Boolean> = _showAddNoteDialog.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val activeNotebookNotes: StateFlow<List<NotebookNote>> = _activeNotebookForWorkspace
        .flatMapLatest { current ->
            if (current != null) repository.getNotesForNotebook(current.id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 3. RAG State
    private val _ragQuery = MutableStateFlow("La máquina da un error de 'Sobretemperatura' pero el ventilador gira")
    val ragQuery: StateFlow<String> = _ragQuery.asStateFlow()

    private val _isRagLoading = MutableStateFlow(false)
    val isRagLoading: StateFlow<Boolean> = _isRagLoading.asStateFlow()

    private val _ragResult = MutableStateFlow<RagDiagnosisResult?>(null)
    val ragResult: StateFlow<RagDiagnosisResult?> = _ragResult.asStateFlow()

    val technicalManuals: StateFlow<List<TechnicalDocument>> = repository.allDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered manuals considering the NotebookLM selection
    val effectiveManualsForAi: StateFlow<List<TechnicalDocument>> = combine(
        repository.allDocuments,
        _selectedNotebookIds
    ) { docs, selectedIds ->
        if (selectedIds.isEmpty()) {
            docs
        } else {
            docs.filter { it.notebookId == null || it.notebookId in selectedIds }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDocumentForDetail = MutableStateFlow<TechnicalDocument?>(null)
    val selectedDocumentForDetail: StateFlow<TechnicalDocument?> = _selectedDocumentForDetail.asStateFlow()

    private val _showDocumentEditDialog = MutableStateFlow(false)
    val showDocumentEditDialog: StateFlow<Boolean> = _showDocumentEditDialog.asStateFlow()

    private val _editingDocument = MutableStateFlow<TechnicalDocument?>(null)
    val editingDocument: StateFlow<TechnicalDocument?> = _editingDocument.asStateFlow()

    // 4. AI Memories State
    val allMemories: StateFlow<List<AiMemoryEntity>> = repository.allMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeMemoriesCount: StateFlow<Int> = combine(repository.allMemories) { memories ->
        memories.firstOrNull()?.count { it.isEnabled } ?: 0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _memoriesFilterCategory = MutableStateFlow("Todas")
    val memoriesFilterCategory: StateFlow<String> = _memoriesFilterCategory.asStateFlow()

    private val _memoriesSearchQuery = MutableStateFlow("")
    val memoriesSearchQuery: StateFlow<String> = _memoriesSearchQuery.asStateFlow()

    val filteredMemories: StateFlow<List<AiMemoryEntity>> = combine(
        repository.allMemories,
        _memoriesFilterCategory,
        _memoriesSearchQuery
    ) { list, category, query ->
        list.filter { memory ->
            val matchesCategory = category == "Todas" || memory.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                memory.title.contains(query, ignoreCase = true) ||
                memory.content.contains(query, ignoreCase = true) ||
                memory.category.contains(query, ignoreCase = true) ||
                memory.source.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _showMemoryEditDialog = MutableStateFlow(false)
    val showMemoryEditDialog: StateFlow<Boolean> = _showMemoryEditDialog.asStateFlow()

    private val _editingMemory = MutableStateFlow<AiMemoryEntity?>(null)
    val editingMemory: StateFlow<AiMemoryEntity?> = _editingMemory.asStateFlow()

    private val _selectedMemoryDetail = MutableStateFlow<AiMemoryEntity?>(null)
    val selectedMemoryDetail: StateFlow<AiMemoryEntity?> = _selectedMemoryDetail.asStateFlow()

    // 5. Voice State
    private val _voiceInteractions = MutableStateFlow<List<Pair<String, String>>>(
        listOf(
            "Técnico: Ok Google, abre el esquema de la bomba hidráulica" to "ElectroTech: Mostrando esquema hidráulico de alta presión con presostato SP1 y bornes de maniobra.",
            "Técnico: ¿Cuál es el valor esperado de la sonda PTC?" to "ElectroTech: Según el manual técnico (pág. 45), la sonda PTC en frío debe medir 10.000 Ω (10 kΩ) a 25°C."
        )
    )
    val voiceInteractions: StateFlow<List<Pair<String, String>>> = _voiceInteractions.asStateFlow()

    private val _lastVoiceAction = MutableStateFlow<VoiceResponseResult?>(null)
    val lastVoiceAction: StateFlow<VoiceResponseResult?> = _lastVoiceAction.asStateFlow()

    // 6. Interventions State
    private val _filterState = MutableStateFlow(UiInterventionFilter())
    val filterState: StateFlow<UiInterventionFilter> = _filterState.asStateFlow()

    val filteredInterventions: StateFlow<List<InterventionEntity>> = combine(
        repository.allInterventions,
        _filterState
    ) { list, filter ->
        list.filter { item ->
            val matchesQuery = filter.query.isBlank() ||
                item.equipmentName.contains(filter.query, ignoreCase = true) ||
                item.cabinetCode.contains(filter.query, ignoreCase = true) ||
                item.failureDescription.contains(filter.query, ignoreCase = true) ||
                item.rootCause.contains(filter.query, ignoreCase = true)

            val matchesStatus = filter.status == "Todos" || item.status.equals(filter.status, ignoreCase = true)
            matchesQuery && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeInterventionDetail = MutableStateFlow<InterventionEntity?>(null)
    val activeInterventionDetail: StateFlow<InterventionEntity?> = _activeInterventionDetail.asStateFlow()

    private val _showAddInterventionSheet = MutableStateFlow(false)
    val showAddInterventionSheet: StateFlow<Boolean> = _showAddInterventionSheet.asStateFlow()

    private val _editingIntervention = MutableStateFlow<InterventionEntity?>(null)
    val editingIntervention: StateFlow<InterventionEntity?> = _editingIntervention.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureDatabaseSeeded()
            loadSampleSchematic(R.drawable.schematic_k1_f2, "Esquema Contactor K1 y Relé F2")
            // Pre-execute a sample web query
            executeWebSearch("Variador Siemens Sinamics G120 fallo F07900 y solución")
        }
    }

    fun setSelectedTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    // Vision functions
    fun loadSampleSchematic(drawableRes: Int, name: String) {
        viewModelScope.launch {
            val bitmap = ImageUtils.drawableToBitmap(getApplication(), drawableRes)
            _selectedBitmap.value = bitmap
            _selectedSampleName.value = name
        }
    }

    fun setImageBitmap(bitmap: Bitmap) {
        _selectedBitmap.value = bitmap
        _selectedSampleName.value = "Foto personalizada capturada"
    }

    fun setVisionPrompt(prompt: String) {
        _visionPrompt.value = prompt
    }

    fun analyzeVisionSchematic() {
        viewModelScope.launch {
            _isVisionLoading.value = true
            val result = repository.analyzeSchematicWithVision(
                bitmap = _selectedBitmap.value,
                userContext = _visionPrompt.value
            )
            result.onSuccess { data ->
                _visionResult.value = data
                _activeQuestions.value = data.diagnosticQuestions
            }
            _isVisionLoading.value = false
        }
    }

    fun toggleQuestionCheck(questionId: String) {
        _activeQuestions.value = _activeQuestions.value.map {
            if (it.id == questionId) it.copy(isChecked = !it.isChecked) else it
        }
    }

    fun setQuestionMeasuredValue(questionId: String, value: String) {
        _activeQuestions.value = _activeQuestions.value.map {
            if (it.id == questionId) it.copy(measuredValue = value) else it
        }
    }

    // Web Search functions
    fun setWebSearchQuery(query: String) {
        _webSearchQuery.value = query
    }

    fun executeWebSearch(query: String? = null) {
        val q = query ?: _webSearchQuery.value
        if (q.isBlank()) return
        _webSearchQuery.value = q
        viewModelScope.launch {
            _isWebSearchLoading.value = true
            val result = repository.searchWebInformation(q)
            result.onSuccess { data ->
                _webSearchResult.value = data
            }
            _isWebSearchLoading.value = false
        }
    }

    fun saveWebResultAsTechnicalDocument(result: WebSearchResult) {
        viewModelScope.launch {
            val specsText = result.technicalSpecs.joinToString("; ") { "${it.parameter}: ${it.value}" }
            val procedureText = result.recommendedActions.joinToString("\n") { "• $it" }
            val doc = TechnicalDocument(
                title = result.title.ifBlank { "Datasheet: ${result.query}" },
                manufacturer = if (result.title.contains("Siemens", ignoreCase = true)) "Siemens" else if (result.title.contains("Schneider", ignoreCase = true)) "Schneider Electric" else "Fabricante Web",
                model = result.query.take(40),
                category = "Datasheet Web",
                pageReference = result.sources.firstOrNull()?.domain ?: "Internet Web Search",
                contentSnippet = result.summary,
                expectedValues = specsText.ifBlank { result.wiringNotes },
                diagnosticProcedure = procedureText.ifBlank { result.safetyWarning }
            )
            repository.insertDocument(doc)
        }
    }

    fun saveWebResultAsAiMemory(result: WebSearchResult) {
        viewModelScope.launch {
            val memory = AiMemoryEntity(
                title = result.title.take(60),
                category = "Regla de Diagnóstico",
                content = "${result.summary}\nCableado: ${result.wiringNotes}",
                source = "Web Research",
                isEnabled = true
            )
            repository.insertMemory(memory)
        }
    }

    // AI Memories functions
    fun setMemoriesFilterCategory(category: String) {
        _memoriesFilterCategory.value = category
    }

    fun setMemoriesSearchQuery(query: String) {
        _memoriesSearchQuery.value = query
    }

    fun selectMemoryForDetail(memory: AiMemoryEntity?) {
        _selectedMemoryDetail.value = memory
    }

    fun openAddMemoryDialog() {
        _editingMemory.value = null
        _showMemoryEditDialog.value = true
    }

    fun openEditMemoryDialog(memory: AiMemoryEntity) {
        _editingMemory.value = memory
        _showMemoryEditDialog.value = true
    }

    fun closeMemoryDialog() {
        _showMemoryEditDialog.value = false
        _editingMemory.value = null
    }

    fun saveMemory(
        id: Long = 0,
        title: String,
        category: String,
        content: String,
        source: String = "Definido por Usuario",
        isEnabled: Boolean = true,
        notebookId: Long? = null
    ) {
        viewModelScope.launch {
            val memory = AiMemoryEntity(
                id = id,
                title = title.ifBlank { "Regla Técnica de Taller" },
                category = category.ifBlank { "Regla de Diagnóstico" },
                content = content,
                source = source.ifBlank { "Definido por Usuario" },
                isEnabled = isEnabled,
                timestamp = System.currentTimeMillis(),
                notebookId = notebookId ?: _editingMemory.value?.notebookId
            )
            if (id > 0) {
                repository.updateMemory(memory)
                if (_selectedMemoryDetail.value?.id == id) {
                    _selectedMemoryDetail.value = memory
                }
            } else {
                repository.insertMemory(memory)
            }
            closeMemoryDialog()
        }
    }

    fun deleteMemory(id: Long) {
        viewModelScope.launch {
            repository.deleteMemory(id)
            if (_selectedMemoryDetail.value?.id == id) {
                _selectedMemoryDetail.value = null
            }
        }
    }

    fun toggleMemoryStatus(id: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.toggleMemoryStatus(id, isEnabled)
            if (_selectedMemoryDetail.value?.id == id) {
                _selectedMemoryDetail.value = _selectedMemoryDetail.value?.copy(isEnabled = isEnabled)
            }
        }
    }

    fun learnMemoryFromIntervention(intervention: InterventionEntity) {
        viewModelScope.launch {
            val memory = AiMemoryEntity(
                title = "${intervention.equipmentName} (${intervention.cabinetCode}) - Solución Avería",
                category = "Fallo Frecuente",
                content = "Causa: ${intervention.rootCause}\nSolución: ${intervention.actionsTaken}\nRepuestos: ${intervention.partsReplaced}",
                source = "Aprendido en Reparación",
                isEnabled = true
            )
            repository.insertMemory(memory)
        }
    }

    // NotebookLM Management & Scoping
    fun toggleNotebookSelection(id: Long) {
        val current = _selectedNotebookIds.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _selectedNotebookIds.value = current
    }

    fun selectAllNotebooks() {
        _selectedNotebookIds.value = emptySet()
    }

    fun selectSingleNotebook(id: Long) {
        _selectedNotebookIds.value = setOf(id)
    }

    fun clearNotebookSelection() {
        _selectedNotebookIds.value = emptySet()
    }

    fun setActiveNotebookForWorkspace(notebook: TechnicalNotebook?) {
        _activeNotebookForWorkspace.value = notebook
        _activeNotebookSummary.value = null
    }

    fun openAddNotebookDialog() {
        _editingNotebook.value = null
        _showAddNotebookDialog.value = true
    }

    fun openEditNotebookDialog(notebook: TechnicalNotebook) {
        _editingNotebook.value = notebook
        _showAddNotebookDialog.value = true
    }

    fun closeNotebookDialog() {
        _showAddNotebookDialog.value = false
        _editingNotebook.value = null
    }

    fun openAddNoteDialog() {
        _showAddNoteDialog.value = true
    }

    fun closeNoteDialog() {
        _showAddNoteDialog.value = false
    }

    fun saveNotebook(
        id: Long = 0,
        name: String,
        machineCode: String,
        area: String,
        description: String,
        colorHex: String,
        iconName: String
    ) {
        viewModelScope.launch {
            val notebook = TechnicalNotebook(
                id = id,
                name = name.ifBlank { "Cuaderno de Máquina" },
                machineCode = machineCode.ifBlank { "MAQ-01" },
                area = area.ifBlank { "Planta Principal" },
                description = description.ifBlank { "Documentación y memorias de la máquina" },
                colorHex = colorHex.ifBlank { "#0284C7" },
                iconName = iconName.ifBlank { "machine" },
                createdAt = if (id > 0) (_editingNotebook.value?.createdAt ?: System.currentTimeMillis()) else System.currentTimeMillis()
            )
            if (id > 0) {
                repository.updateNotebook(notebook)
                if (_activeNotebookForWorkspace.value?.id == id) {
                    _activeNotebookForWorkspace.value = notebook
                }
            } else {
                val newId = repository.insertNotebook(notebook)
                _activeNotebookForWorkspace.value = notebook.copy(id = newId)
            }
            closeNotebookDialog()
        }
    }

    fun deleteNotebook(id: Long) {
        viewModelScope.launch {
            repository.deleteNotebook(id)
            val currentSelected = _selectedNotebookIds.value.toMutableSet()
            currentSelected.remove(id)
            _selectedNotebookIds.value = currentSelected
            if (_activeNotebookForWorkspace.value?.id == id) {
                _activeNotebookForWorkspace.value = null
            }
        }
    }

    fun saveNote(
        notebookId: Long,
        title: String,
        content: String,
        author: String = "Técnico de Turno"
    ) {
        viewModelScope.launch {
            val note = NotebookNote(
                notebookId = notebookId,
                title = title.ifBlank { "Nota de Mantenimiento" },
                content = content,
                author = author.ifBlank { "Técnico" },
                timestamp = System.currentTimeMillis()
            )
            repository.insertNote(note)
            closeNoteDialog()
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun generateActiveNotebookSummary() {
        val currentNotebook = _activeNotebookForWorkspace.value ?: return
        viewModelScope.launch {
            _isGeneratingNotebookSummary.value = true
            val docs = technicalManuals.value.filter { it.notebookId == currentNotebook.id }
            val memories = allMemories.value.filter { it.notebookId == currentNotebook.id }
            val notes = activeNotebookNotes.value
            val interventions = filteredInterventions.value.filter { it.notebookId == currentNotebook.id }

            val result = repository.generateNotebookOverview(currentNotebook, docs, memories, notes, interventions)
            result.onSuccess { summary ->
                _activeNotebookSummary.value = summary
            }
            _isGeneratingNotebookSummary.value = false
        }
    }

    // RAG functions with NotebookLM scoping
    fun setRagQuery(query: String) {
        _ragQuery.value = query
    }

    fun executeRagSearch(query: String? = null) {
        val q = query ?: _ragQuery.value
        if (q.isBlank()) return
        _ragQuery.value = q
        viewModelScope.launch {
            _isRagLoading.value = true
            val manuals = effectiveManualsForAi.value
            val selectedIds = _selectedNotebookIds.value
            val scopeName = if (selectedIds.isEmpty()) {
                "Todos los Cuadernos (${allNotebooks.value.size} Máquinas)"
            } else {
                val names = allNotebooks.value.filter { it.id in selectedIds }.map { it.name }
                if (names.isNotEmpty()) names.joinToString(", ") else "Cuadernos seleccionados"
            }

            val result = repository.queryTechnicalRag(q, manuals, scopeName)
            result.onSuccess { data ->
                _ragResult.value = data
            }
            _isRagLoading.value = false
        }
    }

    fun selectDocumentForDetail(doc: TechnicalDocument?) {
        _selectedDocumentForDetail.value = doc
    }

    fun openAddDocumentDialog() {
        _editingDocument.value = null
        _showDocumentEditDialog.value = true
    }

    fun openEditDocumentDialog(doc: TechnicalDocument) {
        _editingDocument.value = doc
        _showDocumentEditDialog.value = true
    }

    fun closeDocumentEditDialog() {
        _showDocumentEditDialog.value = false
        _editingDocument.value = null
    }

    fun saveTechnicalDocument(
        id: Long = 0,
        title: String,
        manufacturer: String,
        model: String,
        category: String,
        pageReference: String,
        snippet: String,
        expectedValues: String,
        procedure: String,
        notebookId: Long? = null
    ) {
        viewModelScope.launch {
            val doc = TechnicalDocument(
                id = id,
                title = title.ifBlank { "Manual Técnico Sin Título" },
                manufacturer = manufacturer.ifBlank { "Genérico Industrial" },
                model = model.ifBlank { "STD-01" },
                category = category.ifBlank { "Normativa" },
                pageReference = pageReference.ifBlank { "Pág. 1" },
                contentSnippet = snippet,
                expectedValues = expectedValues,
                diagnosticProcedure = procedure,
                notebookId = notebookId ?: _editingDocument.value?.notebookId
            )
            if (id > 0) {
                repository.updateDocument(doc)
                if (_selectedDocumentForDetail.value?.id == id) {
                    _selectedDocumentForDetail.value = doc
                }
            } else {
                repository.insertDocument(doc)
            }
            closeDocumentEditDialog()
        }
    }

    fun deleteTechnicalDocument(id: Long) {
        viewModelScope.launch {
            repository.deleteDocument(id)
            if (_selectedDocumentForDetail.value?.id == id) {
                _selectedDocumentForDetail.value = null
            }
        }
    }

    // Voice Assistant functions
    fun processSpokenVoiceInput(spokenText: String) {
        if (spokenText.isBlank()) return
        viewModelScope.launch {
            val manuals = technicalManuals.value
            val responseResult = repository.processVoiceCommand(spokenText, manuals)
            responseResult.onSuccess { action ->
                _lastVoiceAction.value = action
                _voiceInteractions.value = _voiceInteractions.value + (spokenText to action.spokenResponse)
                
                ttsHelper.speak(action.spokenResponse)

                when (action.actionType) {
                    VoiceActionType.OPEN_SCHEMATIC -> {
                        if (action.actionPayload == "schematic_pump") {
                            loadSampleSchematic(R.drawable.schematic_pump, "Esquema Bomba Hidráulica")
                        } else {
                            loadSampleSchematic(R.drawable.schematic_k1_f2, "Esquema Contactor K1 y F2")
                        }
                        _selectedTab.value = 0
                    }
                    VoiceActionType.RUN_DIAGNOSIS -> {
                        _selectedTab.value = 0
                        analyzeVisionSchematic()
                    }
                    VoiceActionType.READ_VALUES -> {}
                    else -> {}
                }
            }
        }
    }

    fun speakText(text: String) {
        ttsHelper.speak(text)
    }

    fun stopSpeaking() {
        ttsHelper.stop()
    }

    // Interventions functions
    fun setFilterQuery(query: String) {
        _filterState.value = _filterState.value.copy(query = query)
    }

    fun setFilterStatus(status: String) {
        _filterState.value = _filterState.value.copy(status = status)
    }

    fun openAddInterventionSheet(show: Boolean) {
        _editingIntervention.value = null
        _showAddInterventionSheet.value = show
    }

    fun openEditInterventionSheet(intervention: InterventionEntity) {
        _editingIntervention.value = intervention
        _showAddInterventionSheet.value = true
    }

    fun closeInterventionSheet() {
        _showAddInterventionSheet.value = false
        _editingIntervention.value = null
    }

    fun setActiveInterventionDetail(intervention: InterventionEntity?) {
        _activeInterventionDetail.value = intervention
    }

    fun updateIntervention(
        id: Long,
        equipmentName: String,
        cabinetCode: String,
        failureDescription: String,
        rootCause: String,
        actionsTaken: String,
        partsReplaced: String,
        status: String,
        notes: String
    ) {
        viewModelScope.launch {
            val existing = _activeInterventionDetail.value
            val updated = InterventionEntity(
                id = id,
                equipmentName = equipmentName.ifBlank { "Armario General Cuadro 1" },
                cabinetCode = cabinetCode.ifBlank { "ARM-01" },
                failureDescription = failureDescription,
                rootCause = rootCause,
                actionsTaken = actionsTaken,
                partsReplaced = partsReplaced,
                status = status,
                timestamp = existing?.timestamp ?: System.currentTimeMillis(),
                technicalNotes = notes
            )
            repository.updateIntervention(updated)
            if (_activeInterventionDetail.value?.id == id) {
                _activeInterventionDetail.value = updated
            }
            closeInterventionSheet()
        }
    }

    fun createIntervention(
        equipmentName: String,
        cabinetCode: String,
        failureDescription: String,
        rootCause: String,
        actionsTaken: String,
        partsReplaced: String,
        status: String,
        notes: String
    ) {
        viewModelScope.launch {
            val entity = InterventionEntity(
                equipmentName = equipmentName.ifBlank { "Armario General Cuadro 1" },
                cabinetCode = cabinetCode.ifBlank { "ARM-01" },
                failureDescription = failureDescription,
                rootCause = rootCause,
                actionsTaken = actionsTaken,
                partsReplaced = partsReplaced,
                status = status,
                timestamp = System.currentTimeMillis(),
                technicalNotes = notes
            )
            repository.insertIntervention(entity)
            _showAddInterventionSheet.value = false
        }
    }

    fun createInterventionFromVisionDiagnosis(equipmentName: String, cabinetCode: String) {
        val currentVision = _visionResult.value ?: return
        val measuredSummary = _activeQuestions.value.filter { it.isChecked || it.measuredValue.isNotBlank() }
            .joinToString("; ") { "${it.question}: ${it.measuredValue.ifBlank { "Verificado OK" }}" }

        createIntervention(
            equipmentName = equipmentName.ifBlank { "Armario Eléctrico K1/F2" },
            cabinetCode = cabinetCode.ifBlank { "ARM-K1-F2" },
            failureDescription = "No arranca. " + _visionPrompt.value,
            rootCause = currentVision.lineAnalysis,
            actionsTaken = "Inspección de esquema por visión artificial. $measuredSummary",
            partsReplaced = "Comprobación bornes A1-A2 y 95-96 de F2",
            status = "En Diagnóstico",
            notes = currentVision.summary
        )
    }

    fun markInterventionResolved(intervention: InterventionEntity) {
        viewModelScope.launch {
            val updated = intervention.copy(status = "Resuelto")
            repository.updateIntervention(updated)
            _activeInterventionDetail.value = updated
        }
    }

    fun deleteIntervention(id: Long) {
        viewModelScope.launch {
            repository.deleteIntervention(id)
            if (_activeInterventionDetail.value?.id == id) {
                _activeInterventionDetail.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsHelper.shutdown()
    }
}
