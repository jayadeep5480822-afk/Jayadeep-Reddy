package com.example.model

enum class SimulationKind {
    NEURAL_NETWORK,
    QUANTUM_GATE,
    PHARMA_CLEARANCE,
    OS_SEMAPHORE_DEADLOCK
}

data class CheckpointQuestion(
    val id: String,
    val speakerId: String,
    val promptHeading: LocalizedText,
    val questionText: LocalizedText,
    val options: List<LocalizedText>,
    val correctIndex: Int,
    val correctFeedback: LocalizedText,
    val wrongFeedback: LocalizedText
)

data class LiveExample(
    val id: String,
    val title: LocalizedText,
    val description: LocalizedText,
    val simulationKind: SimulationKind,
    val parameter1Label: LocalizedText,
    val parameter1Default: Float,
    val parameter1Min: Float,
    val parameter1Max: Float,
    val parameter2Label: LocalizedText,
    val parameter2Default: Float,
    val parameter2Min: Float,
    val parameter2Max: Float,
    val formulaDisplay: String
)

data class LectureSlide(
    val id: String,
    val speakerId: String,
    val title: LocalizedText,
    val spokenDialogue: LocalizedText,
    val whiteboardNotes: List<LocalizedText>,
    val equationOrCode: String? = null,
    val checkpointQuestion: CheckpointQuestion? = null,
    val liveExample: LiveExample? = null,
    val estimatedDurationSeconds: Int = 45
)

data class RemedialExplanation(
    val speakerId: String,
    val whyCorrect: LocalizedText,
    val trapAnalysis: List<LocalizedText>, // Explanations for options [0..3]
    val coreIntuition: LocalizedText,
    val mnemonicOrExamTip: LocalizedText
)

data class MockQuestion(
    val id: String,
    val questionNumber: Int,
    val examTag: String,
    val questionText: LocalizedText,
    val options: List<LocalizedText>,
    val correctIndex: Int,
    val remedialExplanation: RemedialExplanation
)

data class MockTest(
    val id: String,
    val title: LocalizedText,
    val durationMinutes: Int,
    val passingScorePercent: Int = 70,
    val questions: List<MockQuestion>
)

data class StudyTopic(
    val id: String,
    val title: LocalizedText,
    val field: String,
    val summary: LocalizedText,
    val leadCharacterId: String,
    val slides: List<LectureSlide>,
    val mockTest: MockTest,
    val sourcePdfName: String? = null,
    val isCustomOrPdf: Boolean = false
)

data class UserMockTestResult(
    val topicId: String,
    val totalQuestions: Int,
    val score: Int,
    val selectedAnswers: Map<Int, Int>, // question index -> selected option index
    val timeTakenSeconds: Int,
    val completedTimestamp: Long = System.currentTimeMillis()
)
