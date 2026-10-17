package com.example.model

enum class Language(
    val displayName: String,
    val nativeName: String,
    val tag: String
) {
    ENGLISH("English", "English", "en"),
    TELUGU("Telugu", "తెలుగు", "te"),
    HINDI("Hindi", "हिंदी", "hi");

    companion object {
        fun fromTag(tag: String): Language =
            entries.find { it.tag.equals(tag, ignoreCase = true) } ?: ENGLISH
    }
}

data class LocalizedText(
    val en: String,
    val te: String,
    val hi: String
) {
    fun get(lang: Language): String = when (lang) {
        Language.ENGLISH -> en
        Language.TELUGU -> if (te.isNotBlank()) te else en
        Language.HINDI -> if (hi.isNotBlank()) hi else en
    }
}
