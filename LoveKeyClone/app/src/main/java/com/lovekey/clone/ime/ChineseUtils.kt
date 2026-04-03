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

        // Clean up formatting spaces/apostrophes introduced by the T9 Engine segmentation
        // e.g. "ni hao" -> "nihao" or "ni'hao" (Rime handles plain concatenated syllables best in most setups,
        // but apostrophes are used for explicit disambiguation like xi'an)
        val rimeInput = pinyin.replace(" ", "")

        // Delegate candidate generation to the C++ Rime Engine via our adapter
        val candidates = PinyinEngineAdapter.getCandidates(rimeInput)

        // Fallback or debug fallback if no real candidates are generated yet
        if (candidates.isEmpty()) {
            return listOf(rimeInput + "1", rimeInput + "2", "我", "你", "的", "了")
        }

        return candidates
    }

    /**
     * Convert a T9 number sequence to valid pinyin syllable combinations
     * using the highly localized T9 Trie Engine.
     */
    fun getT9SyllableCombinations(numberSequence: String): List<String> {
        if (numberSequence.isEmpty()) return emptyList()

        // Use the advanced segmentation logic for long sequences,
        // fallback/include single syllable prefixes for short ones
        val segmented = T9Engine.segmentT9Sequence(numberSequence)
        val singlePrefixes = T9Engine.getSingleCombinations(numberSequence)

        // Combine them, prioritizing the full segmentation over single long prefixes if they differ
        val combined = mutableListOf<String>()
        combined.addAll(segmented)
        for (prefix in singlePrefixes) {
            if (!combined.contains(prefix)) {
                combined.add(prefix)
            }
        }
        return combined.take(15) // Limit to top 15 results
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
