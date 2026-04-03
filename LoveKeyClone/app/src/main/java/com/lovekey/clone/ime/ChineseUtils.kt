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
     * Convert a T9 number sequence to valid pinyin syllable combinations
     * using the integrated T9 Trie Engine.
     */
    fun getT9SyllableCombinations(numberSequence: String): List<String> {
        if (numberSequence.isEmpty()) return emptyList()
        // Fetch from the fast Trie engine instead of mocking
        return T9Engine.getCombinations(numberSequence)
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
