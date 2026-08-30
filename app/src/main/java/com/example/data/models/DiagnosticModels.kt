package com.example.data.models

data class VisionDiagnosisResult(
    val summary: String,
    val identifiedComponents: List<ElectricalComponent>,
    val lineAnalysis: String,
    val diagnosticQuestions: List<DiagnosticQuestion>,
    val rawAiResponse: String
)

data class ElectricalComponent(
    val tag: String,          // e.g. "K1", "F2", "A1-A2", "Q1", "PTC"
    val name: String,         // e.g. "Contactor de Fuerza", "Relé Térmico Sobrecarga"
    val role: String,         // e.g. "Conmutación motor trifásico 400V"
    val statusCheck: String   // e.g. "Medir tensión 230V en A1-A2"
)

data class DiagnosticQuestion(
    val id: String,
    val question: String,     // e.g. "¿Has medido tensión en los bornes A1-A2?"
    val expectedValue: String,// e.g. "230 VAC (Rango 207 - 253V)"
    val howToTest: String,    // e.g. "Poner selector del multímetro en V~ y colocar puntas de prueba en bornes de bobina."
    var isChecked: Boolean = false,
    var measuredValue: String = ""
)

data class RagDiagnosisResult(
    val answer: String,
    val citedSource: String,
    val expectedValue: String,
    val stepProcedure: List<String>,
    val relatedManualTitle: String? = null
)

data class VoiceResponseResult(
    val spokenResponse: String,
    val actionType: VoiceActionType,
    val actionPayload: String? = null
)

enum class VoiceActionType {
    OPEN_SCHEMATIC,
    READ_VALUES,
    RUN_DIAGNOSIS,
    CREATE_REPAIR_LOG,
    GENERAL_ANSWER
}

data class WebSearchResult(
    val query: String,
    val title: String,
    val summary: String,
    val technicalSpecs: List<TechnicalSpecItem> = emptyList(),
    val errorCodesResolved: List<ErrorCodeItem> = emptyList(),
    val wiringNotes: String = "",
    val recommendedActions: List<String> = emptyList(),
    val safetyWarning: String = "",
    val sources: List<WebSourceItem> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class TechnicalSpecItem(
    val parameter: String,
    val value: String,
    val note: String = ""
)

data class ErrorCodeItem(
    val code: String,
    val description: String,
    val remedy: String
)

data class WebSourceItem(
    val title: String,
    val url: String,
    val domain: String,
    val snippet: String
)

