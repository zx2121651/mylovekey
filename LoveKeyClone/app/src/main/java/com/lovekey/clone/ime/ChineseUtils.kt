package com.lovekey.clone.ime

/**
 * Utility class for Chinese input logic.
 * This now acts as a bridge to the C++ Rime Engine.
 */
object ChineseUtils {

    /**
     * Get candidate words for a given pinyin string using the integrated Rime Engine.
     */
    fun getCandidates(pinyin: String): List<String> {
        if (pinyin.isEmpty()) return emptyList()

        // Delegate candidate generation to the C++ Rime Engine via our adapter
        val candidates = PinyinEngineAdapter.getCandidates(pinyin)

        // Fallback or debug fallback if no real candidates are generated yet
        if (candidates.isEmpty()) {
            return listOf(pinyin + "1", pinyin + "2", "我", "你", "的", "了")
        }

        return candidates
    }

    /**
     * Mock method to get T9 syllable combinations from a number string
     */
    fun getT9SyllableCombinations(numberSequence: String): List<String> {
        if (numberSequence.isEmpty()) return emptyList()
        // Very basic mock for demonstration (e.g., 426)
        return when (numberSequence) {
            "4" -> listOf("g", "h", "i")
            "42" -> listOf("ha", "ga", "ia")
            "426" -> listOf("hao", "gan", "han", "gao", "iao")
            "96" -> listOf("wo", "yo", "zo")
            "64" -> listOf("ni", "mi")
            else -> {
                // Fallback mock
                listOf(numberSequence + "a", numberSequence + "o", numberSequence + "e")
            }
        }
    }

    // Simple mock for conversion until opencc is fully integrated
    fun convertToTraditional(word: String): String {
        return word.map { char ->
            when (char) {
                '我' -> '我'
                '你' -> '妳'
                '的' -> '的'
                '爱' -> '愛'
                '国' -> '國'
                '说' -> '說'
                else -> char
            }
        }.joinToString("")
    }
}
