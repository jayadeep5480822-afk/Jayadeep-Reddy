package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.CurriculumData
import com.example.model.Language
import com.example.model.StudyTopic
import com.example.model.UserMockTestResult
import com.example.network.GeminiStudyService
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LectureScreen
import com.example.ui.screens.MockTestScreen
import com.example.ui.screens.RemedialRoomScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.SpeechHelper

sealed class AppScreen {
    data object Home : AppScreen()
    data class Lecture(val topic: StudyTopic) : AppScreen()
    data class MockTest(val topic: StudyTopic) : AppScreen()
    data class Remedial(val topic: StudyTopic, val result: UserMockTestResult) : AppScreen()
}

class MainActivity : ComponentActivity() {

    private lateinit var speechHelper: SpeechHelper
    private val geminiService = GeminiStudyService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        speechHelper = SpeechHelper(this)

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    VidyaAiApp(
                        speechHelper = speechHelper,
                        geminiService = geminiService
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechHelper.shutdown()
    }
}

@Composable
fun VidyaAiApp(
    speechHelper: SpeechHelper,
    geminiService: GeminiStudyService
) {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
    var selectedLanguage by remember { mutableStateOf(Language.ENGLISH) }

    // Topic list containing predefined curriculum + user uploaded PDFs
    val topics = remember { mutableStateListOf<StudyTopic>().apply { addAll(CurriculumData.allTopics) } }

    Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
        when (screen) {
            is AppScreen.Home -> {
                HomeScreen(
                    topics = topics,
                    selectedLanguage = selectedLanguage,
                    onLanguageChange = { selectedLanguage = it },
                    onTopicSelected = { topic ->
                        currentScreen = AppScreen.Lecture(topic)
                    },
                    onCustomTopicCreated = { newTopic ->
                        topics.add(0, newTopic)
                    },
                    geminiService = geminiService
                )
            }

            is AppScreen.Lecture -> {
                LectureScreen(
                    topic = screen.topic,
                    language = selectedLanguage,
                    onLanguageChange = { selectedLanguage = it },
                    speechHelper = speechHelper,
                    onStartMockTest = {
                        currentScreen = AppScreen.MockTest(screen.topic)
                    },
                    onBack = {
                        currentScreen = AppScreen.Home
                    }
                )
            }

            is AppScreen.MockTest -> {
                MockTestScreen(
                    topic = screen.topic,
                    language = selectedLanguage,
                    onFinishTest = { testResult ->
                        currentScreen = AppScreen.Remedial(screen.topic, testResult)
                    },
                    onBack = {
                        currentScreen = AppScreen.Lecture(screen.topic)
                    }
                )
            }

            is AppScreen.Remedial -> {
                RemedialRoomScreen(
                    topic = screen.topic,
                    result = screen.result,
                    language = selectedLanguage,
                    onLanguageChange = { selectedLanguage = it },
                    speechHelper = speechHelper,
                    geminiService = geminiService,
                    onRetakeTest = {
                        currentScreen = AppScreen.MockTest(screen.topic)
                    },
                    onBackToHome = {
                        currentScreen = AppScreen.Home
                    }
                )
            }
        }
    }
}
