package com.example.network

import com.example.BuildConfig
import com.example.data.CurriculumData
import com.example.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiStudyService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateLectureFromTopic(
        topicTitle: String,
        contextText: String,
        language: Language,
        character: AiCharacter
    ): Result<StudyTopic> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Return synthesized dynamic topic if key is unconfigured
            return@withContext Result.success(createFallbackGeneratedTopic(topicTitle, contextText, language, character))
        }

        val prompt = """
            You are ${character.name}, ${character.title.en} specializing in ${character.specialty.en}.
            Create a comprehensive higher studies university lecture and mock exam on the following topic/PDF excerpt:
            TOPIC: $topicTitle
            CONTEXT / EXCERPT: $contextText
            PRIMARY LANGUAGE: ${language.displayName} (Note: Provide clear academic content with English, Telugu, and Hindi translations where possible).

            Respond with a strict JSON object with this exact structure:
            {
              "titleEn": "...",
              "titleTe": "...",
              "titleHi": "...",
              "field": "Higher Academic Studies",
              "summaryEn": "...",
              "summaryTe": "...",
              "summaryHi": "...",
              "slides": [
                {
                  "titleEn": "...",
                  "titleTe": "...",
                  "titleHi": "...",
                  "spokenDialogueEn": "...",
                  "spokenDialogueTe": "...",
                  "spokenDialogueHi": "...",
                  "whiteboardNotesEn": ["...", "..."],
                  "whiteboardNotesTe": ["...", "..."],
                  "whiteboardNotesHi": ["...", "..."],
                  "equationOrCode": "..."
                }
              ],
              "checkpoint": {
                "questionEn": "...",
                "questionTe": "...",
                "questionHi": "...",
                "optionsEn": ["A", "B", "C", "D"],
                "optionsTe": ["A", "B", "C", "D"],
                "optionsHi": ["A", "B", "C", "D"],
                "correctIndex": 0,
                "correctFeedbackEn": "...",
                "correctFeedbackTe": "...",
                "correctFeedbackHi": "...",
                "wrongFeedbackEn": "...",
                "wrongFeedbackTe": "...",
                "wrongFeedbackHi": "..."
              },
              "mockQuestion": {
                "questionEn": "...",
                "questionTe": "...",
                "questionHi": "...",
                "optionsEn": ["Option 1", "Option 2", "Option 3", "Option 4"],
                "optionsTe": ["Option 1", "Option 2", "Option 3", "Option 4"],
                "optionsHi": ["Option 1", "Option 2", "Option 3", "Option 4"],
                "correctIndex": 0,
                "whyCorrectEn": "...",
                "whyCorrectTe": "...",
                "whyCorrectHi": "...",
                "trapsEn": ["...", "...", "...", "..."],
                "trapsTe": ["...", "...", "...", "..."],
                "trapsHi": ["...", "...", "...", "..."],
                "coreIntuitionEn": "...",
                "coreIntuitionTe": "...",
                "coreIntuitionHi": "...",
                "mnemonicEn": "...",
                "mnemonicTe": "...",
                "mnemonicHi": "..."
              }
            }
        """.trimIndent()

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.4)
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(requestJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.success(createFallbackGeneratedTopic(topicTitle, contextText, language, character))
            }

            val responseBody = response.body?.string() ?: ""
            val jsonResponse = JSONObject(responseBody)
            val candidate = jsonResponse.getJSONArray("candidates").getJSONObject(0)
            val textContent = candidate.getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text")
            val parsed = JSONObject(textContent)

            val parsedTopic = mapJsonToStudyTopic(parsed, character, topicTitle)
            Result.success(parsedTopic)
        } catch (e: Exception) {
            Result.success(createFallbackGeneratedTopic(topicTitle, contextText, language, character))
        }
    }

    suspend fun askFollowUpDoubt(
        questionContext: String,
        userQuery: String,
        language: Language,
        character: AiCharacter
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext when (language) {
                Language.ENGLISH -> "${character.name}: Great question! In higher academic studies, focus on the fundamental derivation. Verify the boundary conditions and remember why options with extreme absolutes are usually distractors."
                Language.TELUGU -> "${character.name}: మంచి సందేహం! ఉన్నత అధ్యయనాలలో ప్రాథమిక నిరూపణపై దృష్టి పెట్టండి. సరిహద్దు విలువలను ఎల్లప్పుడూ సరిచూసుకోండి."
                Language.HINDI -> "${character.name}: बहुत अच्छा सवाल! उच्च अध्ययन में मौलिक व्युत्पत्ति पर ध्यान केंद्रित करें। हमेशा सीमाओं और अपवादों की पुष्टि करें।"
            }
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val prompt = """
                You are ${character.name}, a distinguished university professor and tutor.
                The student is reviewing an exam question: "$questionContext"
                The student asks you: "$userQuery"
                Explain clearly, warmly, and with deep academic insight.
                Language to respond in: ${language.displayName}.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(requestJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string() ?: ""
                val jsonObj = JSONObject(body)
                val text = jsonObj.getJSONArray("candidates").getJSONObject(0)
                    .getJSONObject("content").getJSONArray("parts").getJSONObject(0)
                    .getString("text")
                return@withContext text
            }
        } catch (_: Exception) {}

        "${character.name}: Always ground your understanding in the foundational principles before jumping to competitive shortcuts!"
    }

    private fun mapJsonToStudyTopic(
        json: JSONObject,
        character: AiCharacter,
        originalTitle: String
    ): StudyTopic {
        val titleEn = json.optString("titleEn", originalTitle)
        val titleTe = json.optString("titleTe", originalTitle)
        val titleHi = json.optString("titleHi", originalTitle)

        val summaryEn = json.optString("summaryEn", "Comprehensive AI guided lesson for higher studies.")
        val summaryTe = json.optString("summaryTe", "ఉన్నత విద్యా అంశాలపై లోతైన AI లెక్చర్.")
        val summaryHi = json.optString("summaryHi", "उच्च अध्ययन के लिए व्यापक एआई व्याख्यान।")

        val slidesList = mutableListOf<LectureSlide>()
        val slidesArray = json.optJSONArray("slides")
        if (slidesArray != null) {
            for (i in 0 until slidesArray.length()) {
                val sObj = slidesArray.getJSONObject(i)
                val notesEn = mutableListOf<LocalizedText>()
                val nEn = sObj.optJSONArray("whiteboardNotesEn")
                val nTe = sObj.optJSONArray("whiteboardNotesTe")
                val nHi = sObj.optJSONArray("whiteboardNotesHi")
                if (nEn != null) {
                    for (j in 0 until nEn.length()) {
                        notesEn.add(
                            LocalizedText(
                                en = nEn.optString(j),
                                te = nTe?.optString(j) ?: nEn.optString(j),
                                hi = nHi?.optString(j) ?: nEn.optString(j)
                            )
                        )
                    }
                }
                slidesList.add(
                    LectureSlide(
                        id = "gen_s_$i",
                        speakerId = character.id,
                        title = LocalizedText(
                            en = sObj.optString("titleEn", "Section ${i + 1}"),
                            te = sObj.optString("titleTe", "విభాగం ${i + 1}"),
                            hi = sObj.optString("titleHi", "भाग ${i + 1}")
                        ),
                        spokenDialogue = LocalizedText(
                            en = sObj.optString("spokenDialogueEn", ""),
                            te = sObj.optString("spokenDialogueTe", ""),
                            hi = sObj.optString("spokenDialogueHi", "")
                        ),
                        whiteboardNotes = notesEn,
                        equationOrCode = if (sObj.has("equationOrCode")) sObj.optString("equationOrCode") else null
                    )
                )
            }
        }

        if (slidesList.isEmpty()) {
            slidesList.add(
                LectureSlide(
                    id = "gen_s_0",
                    speakerId = character.id,
                    title = LocalizedText(titleEn, titleTe, titleHi),
                    spokenDialogue = LocalizedText(summaryEn, summaryTe, summaryHi),
                    whiteboardNotes = listOf(LocalizedText(titleEn, titleTe, titleHi))
                )
            )
        }

        // Checkpoint question
        val cpObj = json.optJSONObject("checkpoint")
        val checkpoint = if (cpObj != null) {
            val optEn = cpObj.optJSONArray("optionsEn")
            val optTe = cpObj.optJSONArray("optionsTe")
            val optHi = cpObj.optJSONArray("optionsHi")
            val optionsList = mutableListOf<LocalizedText>()
            if (optEn != null) {
                for (k in 0 until optEn.length()) {
                    optionsList.add(
                        LocalizedText(
                            en = optEn.optString(k),
                            te = optTe?.optString(k) ?: optEn.optString(k),
                            hi = optHi?.optString(k) ?: optEn.optString(k)
                        )
                    )
                }
            }
            CheckpointQuestion(
                id = "cp_gen",
                speakerId = character.id,
                promptHeading = LocalizedText(
                    en = "${character.name}'s In-Lecture Checkpoint",
                    te = "${character.name} చెక్‌పాయింట్ క్విజ్",
                    hi = "${character.name} की चेकपॉइंट प्रश्नोत्तरी"
                ),
                questionText = LocalizedText(
                    en = cpObj.optString("questionEn"),
                    te = cpObj.optString("questionTe"),
                    hi = cpObj.optString("questionHi")
                ),
                options = optionsList,
                correctIndex = cpObj.optInt("correctIndex", 0),
                correctFeedback = LocalizedText(
                    en = cpObj.optString("correctFeedbackEn", "Splendid! Exactly right."),
                    te = cpObj.optString("correctFeedbackTe", "చాలా బాగుంది! ఖచ్చితమైన సమాధానం."),
                    hi = cpObj.optString("correctFeedbackHi", "शानदार! बिल्कुल सही उत्तर।")
                ),
                wrongFeedback = LocalizedText(
                    en = cpObj.optString("wrongFeedbackEn", "Notice the core definition once more."),
                    te = cpObj.optString("wrongFeedbackTe", "ప్రాథమిక నిర్వచనాన్ని మరొకసారి గమనించండి."),
                    hi = cpObj.optString("wrongFeedbackHi", "कृपया मुख्य परिभाषा को पुनः देखें।")
                )
            )
        } else null

        // Attach checkpoint to first slide if present
        if (checkpoint != null && slidesList.isNotEmpty()) {
            slidesList[0] = slidesList[0].copy(checkpointQuestion = checkpoint)
        }

        // Mock test question
        val mqObj = json.optJSONObject("mockQuestion")
        val mockQuestionList = mutableListOf<MockQuestion>()
        if (mqObj != null) {
            val optEn = mqObj.optJSONArray("optionsEn")
            val optTe = mqObj.optJSONArray("optionsTe")
            val optHi = mqObj.optJSONArray("optionsHi")
            val optionsList = mutableListOf<LocalizedText>()
            if (optEn != null) {
                for (k in 0 until optEn.length()) {
                    optionsList.add(
                        LocalizedText(
                            en = optEn.optString(k),
                            te = optTe?.optString(k) ?: optEn.optString(k),
                            hi = optHi?.optString(k) ?: optEn.optString(k)
                        )
                    )
                }
            }

            val trapsEn = mqObj.optJSONArray("trapsEn")
            val trapsTe = mqObj.optJSONArray("trapsTe")
            val trapsHi = mqObj.optJSONArray("trapsHi")
            val trapList = mutableListOf<LocalizedText>()
            if (trapsEn != null) {
                for (k in 0 until trapsEn.length()) {
                    trapList.add(
                        LocalizedText(
                            en = trapsEn.optString(k),
                            te = trapsTe?.optString(k) ?: trapsEn.optString(k),
                            hi = trapsHi?.optString(k) ?: trapsEn.optString(k)
                        )
                    )
                }
            }

            mockQuestionList.add(
                MockQuestion(
                    id = "q_gen_1",
                    questionNumber = 1,
                    examTag = "Master's Examination",
                    questionText = LocalizedText(
                        en = mqObj.optString("questionEn"),
                        te = mqObj.optString("questionTe"),
                        hi = mqObj.optString("questionHi")
                    ),
                    options = optionsList,
                    correctIndex = mqObj.optInt("correctIndex", 0),
                    remedialExplanation = RemedialExplanation(
                        speakerId = character.id,
                        whyCorrect = LocalizedText(
                            en = mqObj.optString("whyCorrectEn"),
                            te = mqObj.optString("whyCorrectTe"),
                            hi = mqObj.optString("whyCorrectHi")
                        ),
                        trapAnalysis = trapList,
                        coreIntuition = LocalizedText(
                            en = mqObj.optString("coreIntuitionEn"),
                            te = mqObj.optString("coreIntuitionTe"),
                            hi = mqObj.optString("coreIntuitionHi")
                        ),
                        mnemonicOrExamTip = LocalizedText(
                            en = mqObj.optString("mnemonicEn"),
                            te = mqObj.optString("mnemonicTe"),
                            hi = mqObj.optString("mnemonicHi")
                        )
                    )
                )
            )
        }

        return StudyTopic(
            id = "custom_${System.currentTimeMillis()}",
            title = LocalizedText(titleEn, titleTe, titleHi),
            field = "AI Higher Studies Curriculum",
            summary = LocalizedText(summaryEn, summaryTe, summaryHi),
            leadCharacterId = character.id,
            slides = slidesList,
            mockTest = MockTest(
                id = "mock_gen",
                title = LocalizedText(
                    en = "Evaluation Mock: $titleEn",
                    te = "మూల్యాంకన మాక్: $titleTe",
                    hi = "मूल्यांकन मॉक: $titleHi"
                ),
                durationMinutes = 10,
                questions = mockQuestionList.ifEmpty { CurriculumData.DEEP_LEARNING_TOPIC.mockTest.questions }
            ),
            isCustomOrPdf = true
        )
    }

    private fun createFallbackGeneratedTopic(
        topicTitle: String,
        contextText: String,
        language: Language,
        character: AiCharacter
    ): StudyTopic {
        val cleanTitle = if (topicTitle.isNotBlank()) topicTitle else "Advanced Academic Synthesis"
        return StudyTopic(
            id = "custom_${System.currentTimeMillis()}",
            title = LocalizedText(
                en = cleanTitle,
                te = "$cleanTitle (తెలుగు అనువాదం)",
                hi = "$cleanTitle (हिंदी अनुवाद)"
            ),
            field = "Higher Studies Academic Excerpt",
            summary = LocalizedText(
                en = "AI synthesized masterclass on $cleanTitle based on document text with live simulation and mock evaluation.",
                te = "$cleanTitle పై AI ప్రొఫెసర్ విశ్లేషణ, లైవ్ సిమ్యులేషన్ మరియు మాక్ టెస్ట్ వివరణలు.",
                hi = "$cleanTitle पर एआई प्रोफेसर विश्लेषण, लाइव सिमुलेशन और मॉक टेस्ट व्याख्याएं।"
            ),
            leadCharacterId = character.id,
            slides = listOf(
                LectureSlide(
                    id = "gen_fallback_s1",
                    speakerId = character.id,
                    title = LocalizedText(
                        en = "Foundational Principles of $cleanTitle",
                        te = "$cleanTitle యొక్క ప్రాథమిక సూత్రాలు",
                        hi = "$cleanTitle के मूलभूत सिद्धांत"
                    ),
                    spokenDialogue = LocalizedText(
                        en = "Welcome to this specialized lecture on $cleanTitle. Based on your uploaded text, let us analyze the core theoretical framework and its academic significance.",
                        te = "$cleanTitle పై ప్రత్యేక లెక్చర్‌కు స్వాగతం. మీరు అందించిన సమాచారం ఆధారంగా ప్రాథమిక భావనలను వివరంగా అర్థం చేసుకుందాం.",
                        hi = "$cleanTitle पर इस विशेष व्याख्यान में आपका स्वागत है। आपके दस्तावेज़ के आधार पर चलिए मुख्य सिद्धांतों का विश्लेषण करते हैं।"
                    ),
                    whiteboardNotes = listOf(
                        LocalizedText(
                            en = "• Core Concept: Systemic analysis of $cleanTitle",
                            te = "• ముఖ్య భావన: $cleanTitle యొక్క క్రమబద్ధమైన విశ్లేషణ",
                            hi = "• मुख्य अवधारणा: $cleanTitle का व्यवस्थित विश्लेषण"
                        ),
                        LocalizedText(
                            en = "• Excerpt: ${contextText.take(120)}...",
                            te = "• సారాంశం: ${contextText.take(120)}...",
                            hi = "• सारांश: ${contextText.take(120)}..."
                        )
                    ),
                    checkpointQuestion = CheckpointQuestion(
                        id = "cp_fallback",
                        speakerId = character.id,
                        promptHeading = LocalizedText(
                            en = "${character.name}'s Quick Checkpoint",
                            te = "${character.name} చెక్‌పాయింట్ ప్రశ్న",
                            hi = "${character.name} का त्वरित प्रश्न"
                        ),
                        questionText = LocalizedText(
                            en = "What is the primary governing principle in analyzing $cleanTitle for higher studies?",
                            te = "$cleanTitle ను విశ్లేషించడంలో అత్యంత కీలకమైన సూత్రం ఏమిటి?",
                            hi = "$cleanTitle का विश्लेषण करने में सबसे महत्वपूर्ण नियम क्या है?"
                        ),
                        options = listOf(
                            LocalizedText(
                                en = "Systemic boundary condition consistency and mathematical rigor.",
                                te = "వ్యవస్థ సరిహద్దు పరిస్థితుల సమతుల్యత మరియు గణిత ఖచ్చితత్వం.",
                                hi = "प्रणाली की सीमाओं की निरंतरता और गणितीय सटीकता।"
                            ),
                            LocalizedText(
                                en = "Superficial memorization without understanding underlying causes.",
                                te = "సూత్రాలను అర్థం చేసుకోకుండా కేవలం బట్టీ పట్టడం.",
                                hi = "कारण समझे बिना केवल रटना।"
                            ),
                            LocalizedText(
                                en = "Assuming all variables remain invariant under all perturbations.",
                                te = "అన్ని పరిస్థితుల్లో వేరియబుల్స్ మారవని అనుకోవడం.",
                                hi = "यह मानना कि परिस्थितियां कभी नहीं बदलेंगी।"
                            ),
                            LocalizedText(
                                en = "Ignoring experimental errors in standard calculations.",
                                te = "లెక్కల్లో ప్రయోగ లోపాలను పట్టించుకోకపోవడం.",
                                hi = "मानक गणनाओं में प्रयोगात्मक त्रुटियों की उपेक्षा करना।"
                            )
                        ),
                        correctIndex = 0,
                        correctFeedback = LocalizedText(
                            en = "Precisely! Boundary condition consistency is paramount in higher academic research.",
                            te = "ఖచ్చితంగా! ఉన్నత పరిశోధనల్లో సరిహద్దు విలువల స్థిరత్వమే అత్యంత కీలకం.",
                            hi = "बिल्कुल सही! उच्च शोध में सीमाओं की निरंतरता सर्वोपरि है।"
                        ),
                        wrongFeedback = LocalizedText(
                            en = "Reflect on how higher studies require rigorous proof rather than simplistic assumptions.",
                            te = "ఉన్నత చదువుల్లో ఊహల కంటే ఖచ్చితమైన రుజువులు ముఖ్యమని గమనించండి.",
                            hi = "ध्यान दें कि उच्च अध्ययन में सरलीकृत मान्यताओं के बजाय कठोर प्रमाण की आवश्यकता होती है।"
                        )
                    )
                ),
                LectureSlide(
                    id = "gen_fallback_s2",
                    speakerId = "arjun",
                    title = LocalizedText(
                        en = "Live Parameter Sensitivity & Simulation",
                        te = "లైవ్ పారామీటర్ విశ్లేషణ & సిమ్యులేషన్",
                        hi = "लाइव पैरामीटर संवेदनशीलता और सिमुलेशन"
                    ),
                    spokenDialogue = LocalizedText(
                        en = "Master Arjun here! Every high-level topic boils down to interacting variables. Let us observe the live feedback loop on the simulator.",
                        te = "మాస్టర్ అర్జున్ మీతో! ప్రతి ఉన్నత సిద్ధాంతం వేరియబుల్స్ పరస్పర చర్యలపై ఆధారపడి ఉంటుంది. సిమ్యులేటర్ లో ప్రత్యక్షంగా చూడండి.",
                        hi = "मास्टर अर्जुन यहाँ है! प्रत्येक उच्च-स्तरीय विषय परस्पर क्रिया करने वाले चरों पर निर्भर करता है। सिमुलेटर पर लाइव प्रतिक्रिया देखें।"
                    ),
                    whiteboardNotes = listOf(
                        LocalizedText(
                            en = "• Sensitivity Coefficient: d(Output)/d(Input)",
                            te = "• సెన్సిటివిటీ గుణకం: d(అవుట్‌పుట్)/d(ఇన్‌పుట్)",
                            hi = "• संवेदनशीलता गुणांक: d(आउटपुट)/d(इनपुट)"
                        ),
                        LocalizedText(
                            en = "• Dynamic Response Under Parameter Modulation",
                            te = "• పారామీటర్ల మార్పుల వల్ల కలిగే ప్రత్యక్ష ఫలితం",
                            hi = "• पैरामीटर संशोधन के तहत गतिशील प्रतिक्रिया"
                        )
                    ),
                    liveExample = LiveExample(
                        id = "ex_fallback_sim",
                        title = LocalizedText(
                            en = "Dynamic Equilibrium Simulator",
                            te = "డైనమిక్ ఈక్విలిబ్రియం సిమ్యులేటర్",
                            hi = "गतिशील संतुलन सिम्युलेटर"
                        ),
                        description = LocalizedText(
                            en = "Tweak driving force and damping ratio to see stability convergence.",
                            te = "స్థిరత్వాన్ని చూడటానికి డ్రైవింగ్ ఫోర్స్ మరియు డంపింగ్ రేషియోలను మార్చండి.",
                            hi = "स्थिरता अभिसरण देखने के लिए प्रेरक बल और अवमंदन अनुपात को समायोजित करें।"
                        ),
                        simulationKind = SimulationKind.NEURAL_NETWORK,
                        parameter1Label = LocalizedText(en = "Coupling Factor", te = "కప్లింగ్ ఫ్యాక్టర్", hi = "युग्मन कारक"),
                        parameter1Default = 0.5f,
                        parameter1Min = 0.1f,
                        parameter1Max = 1.0f,
                        parameter2Label = LocalizedText(en = "Damping Ratio", te = "డంపింగ్ రేషియో", hi = "अवमंदन अनुपात"),
                        parameter2Default = 0.7f,
                        parameter2Min = 0.05f,
                        parameter2Max = 1.0f,
                        formulaDisplay = "\\frac{d^2 x}{dt^2} + 2\\zeta\\omega_n \\frac{dx}{dt} + \\omega_n^2 x = F(t)"
                    )
                )
            ),
            mockTest = MockTest(
                id = "mock_fallback",
                title = LocalizedText(
                    en = "Evaluation Mock: $cleanTitle",
                    te = "పరీక్షా మాక్: $cleanTitle",
                    hi = "मूल्यांकन मॉक: $cleanTitle"
                ),
                durationMinutes = 8,
                questions = listOf(
                    MockQuestion(
                        id = "q_fb_1",
                        questionNumber = 1,
                        examTag = "Higher Studies Benchmark",
                        questionText = LocalizedText(
                            en = "Which factor represents the single biggest determinant of stability in analyzing $cleanTitle?",
                            te = "$cleanTitle ను విశ్లేషించడంలో స్థిరత్వాన్ని నిర్దేశించే ముఖ్యమైన అంశం ఏది?",
                            hi = "$cleanTitle का विश्लेषण करते समय स्थिरता का सबसे बड़ा निर्धारक कौन सा कारक है?"
                        ),
                        options = listOf(
                            LocalizedText(en = "Negative feedback gain and phase margin", te = "నెగటివ్ ఫీడ్‌బ్యాక్ గెయిన్ మరియు ఫేజ్ మార్జిన్", hi = "नकारात्मक प्रतिक्रिया लाभ और चरण मार्जिन"),
                            LocalizedText(en = "Unbounded positive feedback amplification", te = "అపరిమిత పాజిటివ్ ఫీడ్‌బ్యాక్ విస్తరణ", hi = "अनियंत्रित सकारात्मक प्रतिक्रिया प्रवर्धन"),
                            LocalizedText(en = "Disregarding sampling frequency bounds", te = "శాంప్లింగ్ ఫ్రీక్వెన్సీ పరిమితులను విస్మరించడం", hi = "नमूना आवृत्ति सीमाओं की उपेक्षा करना"),
                            LocalizedText(en = "Random non-deterministic perturbations", te = "యాదృచ్ఛిక అనిశ్చిత హెచ్చుతగ్గులు", hi = "यादृच्छिक अनिश्चित उतार-चढ़ाव")
                        ),
                        correctIndex = 0,
                        remedialExplanation = RemedialExplanation(
                            speakerId = character.id,
                            whyCorrect = LocalizedText(
                                en = "Negative feedback with adequate phase margin prevents closed-loop poles from crossing into the unstable right-half plane.",
                                te = "సరైన ఫేజ్ మార్జిన్ కలిగిన నెగటివ్ ఫీడ్‌బ్యాక్ సిస్టమ్‌ను స్థిరంగా ఉంచుతుంది మరియు అస్థిరతను నివారిస్తుంది.",
                                hi = "पर्याप्त चरण मार्जिन के साथ नकारात्मक प्रतिक्रिया प्रणाली को स्थिर बनाए रखती है।"
                            ),
                            trapAnalysis = listOf(
                                LocalizedText(en = "Correct: Guarantees bounded-input bounded-output (BIBO) stability.", te = "సరైనది: సిస్టమ్ స్థిరత్వానికి హామీ ఇస్తుంది.", hi = "सही: प्रणाली स्थिरता की गारंटी देता है।"),
                                LocalizedText(en = "Trap: Positive feedback causes exponential runaway divergence.", te = "పొరపాటు: పాజిటివ్ ఫీడ్‌బ్యాక్ సిస్టమ్‌ను అస్థిరం చేస్తుంది.", hi = "धोखा: सकारात्मक प्रतिक्रिया अस्थिरता पैदा करती है।"),
                                LocalizedText(en = "Trap: Ignoring sampling bounds causes aliasing errors.", te = "పొరపాటు: శాంప్లింగ్ పరిమితులను విస్మరిస్తే లోపాలు వస్తాయి.", hi = "धोखा: नमूना सीमाओं की उपेक्षा करने से त्रुटियां होती हैं।"),
                                LocalizedText(en = "Trap: Random perturbations test robustness, but do not establish stable design.", te = "పొరపాటు: ఇవి కేవలం పరీక్షించడానికి మాత్రమే ఉపయోగపడతాయి.", hi = "धोखा: यादृच्छिक उतार-चढ़ाव केवल मजबूती का परीक्षण करते हैं।")
                            ),
                            coreIntuition = LocalizedText(
                                en = "Just like cruise control gently tapping brakes to maintain steady speed.",
                                te = "వాహనంలో క్రూయిజ్ కంట్రోల్ వేగాన్ని ఎలా నియంత్రిస్తుందో అలాగే ఇది పనిచేస్తుంది.",
                                hi = "जैसे क्रूज कंट्रोल वाहन की गति को स्थिर बनाए रखने के लिए ब्रेक को नियंत्रित करता है।"
                            ),
                            mnemonicOrExamTip = LocalizedText(
                                en = "Remember: 'Negative Feedback = Stability; Positive Feedback = Explosion!'",
                                te = "గుర్తుంచుకోండి: 'నెగటివ్ ఫీడ్‌బ్యాక్ = స్థిరత్వం; పాజిటివ్ ఫీడ్‌బ్యాక్ = విస్ఫోటనం!'",
                                hi = "याद रखें: 'नकारात्मक प्रतिक्रिया = स्थिरता; सकारात्मक प्रतिक्रिया = विस्फोट!'"
                            )
                        )
                    )
                )
            ),
            isCustomOrPdf = true
        )
    }
}
