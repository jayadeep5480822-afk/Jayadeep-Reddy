package com.example.model

import com.example.R

data class AiCharacter(
    val id: String,
    val name: String,
    val title: LocalizedText,
    val specialty: LocalizedText,
    val bio: LocalizedText,
    val avatarRes: Int,
    val accentColorHex: Long,
    val voicePitch: Float = 1.0f,
    val voiceRate: Float = 1.0f
)

object CharacterRepository {
    val PROF_ANANYA = AiCharacter(
        id = "ananya",
        name = "Prof. Ananya",
        title = LocalizedText(
            en = "Lead Concept Architect",
            te = "కాన్సెప్ట్ ఆర్కిటెక్ట్ ప్రొఫెసర్",
            hi = "मुख्य अवधारणा वास्तुकार"
        ),
        specialty = LocalizedText(
            en = "Intuitive Foundations & Core Principles",
            te = "ప్రాథమిక సిద్ధాంతాలు & లోతైన అవగాహన",
            hi = "सहज नींव और मुख्य सिद्धांत"
        ),
        bio = LocalizedText(
            en = "Specializes in demystifying abstract higher studies theories into crystal clear mental models.",
            te = "క్లిష్టమైన ఉన్నత విద్యా అంశాలను సులభమైన భావనలుగా వివరించడంలో నిపుణురాలు.",
            hi = "जटिल उच्च अध्ययन सिद्धांतों को स्पष्ट मानसिक मॉडलों में बदलने में विशेषज्ञ।"
        ),
        avatarRes = R.drawable.avatar_ananya,
        accentColorHex = 0xFF6366F1, // Indigo
        voicePitch = 1.1f,
        voiceRate = 0.95f
    )

    val DR_VIKRAM = AiCharacter(
        id = "vikram",
        name = "Dr. Vikram",
        title = LocalizedText(
            en = "Advanced Analytical Lead",
            te = "అడ్వాన్స్డ్ ఎనలిటికల్ రీసెర్చర్",
            hi = "उन्नत विश्लेषणात्मक प्रमुख"
        ),
        specialty = LocalizedText(
            en = "Mathematical Rigor & Competitive Exam Pitfalls",
            te = "గణిత నైపుణ్యం & కాంపిటీటివ్ పరీక్షా మెళకువలు",
            hi = "गणितीय सटीकता और प्रतियोगी परीक्षा की बारीकियां"
        ),
        bio = LocalizedText(
            en = "20+ years guiding GATE, GRE, and Master's scholars. Masters deep derivations and tricky multiple-choice traps.",
            te = "GATE, GRE మరియు ఉన్నత పరీక్షలలో విద్యార్థులకు మార్గదర్శనం చేయడంలో 20 సంవత్సరాల అనుభవం.",
            hi = "गेट, जीआरई और स्नातकोत्तर छात्रों के लिए 20 वर्षों का अनुभव। कठिन प्रश्नों के विश्लेषण में माहिर।"
        ),
        avatarRes = R.drawable.avatar_vikram,
        accentColorHex = 0xFF0891B2, // Cyan dark
        voicePitch = 0.9f,
        voiceRate = 0.92f
    )

    val MASTER_ARJUN = AiCharacter(
        id = "arjun",
        name = "Master Arjun",
        title = LocalizedText(
            en = "Practical Simulation Mentor",
            te = "ప్రాక్టికల్ సిమ్యులేషన్ మెంటర్",
            hi = "व्यावहारिक सिमुलेशन मेंटर"
        ),
        specialty = LocalizedText(
            en = "Live Experiments, Code & Physical Analogies",
            te = "లైవ్ ప్రయోగాలు & ప్రాక్టికల్ ఉదాహరణలు",
            hi = "लाइव प्रयोग, कोड और भौतिक उदाहरण"
        ),
        bio = LocalizedText(
            en = "Transforms dry formulas into live visual simulations so you see science and engineering in action.",
            te = "సూత్రాలను ప్రత్యక్ష విజువల్ సిమ్యులేషన్లుగా మార్చి సులభంగా అర్థమయ్యేలా చేస్తారు.",
            hi = "सूखे सूत्रों को लाइव विजुअल सिमुलेशन में बदलकर विज्ञान और इंजीनियरिंग को जीवंत बनाते हैं।"
        ),
        avatarRes = R.drawable.avatar_arjun,
        accentColorHex = 0xFFF59E0B, // Amber
        voicePitch = 1.0f,
        voiceRate = 1.05f
    )

    val allCharacters: List<AiCharacter> = listOf(PROF_ANANYA, DR_VIKRAM, MASTER_ARJUN)

    fun getById(id: String): AiCharacter =
        allCharacters.find { it.id == id } ?: PROF_ANANYA
}
