package com.lovekey.clone.ime

object ChineseUtils {
    // A tiny mock dictionary mapping Pinyin to Chinese words
    private val pinyinDict = mapOf(
        "nihao" to listOf("你好", "拟好", "泥好"),
        "zai" to listOf("在", "再", "载", "灾"),
        "ganma" to listOf("干嘛", "赶马"),
        "wo" to listOf("我", "握", "卧"),
        "xihuan" to listOf("喜欢", "稀罕"),
        "ni" to listOf("你", "泥", "拟"),
        "wanan" to listOf("晚安", "万安")
    )

    // A tiny dictionary for Simplified to Traditional conversion
    private val simpToTrad = mapOf(
        '你' to '你', '好' to '好',
        '在' to '在', '干' to '幹', '嘛' to '嘛',
        '我' to '我', '喜' to '喜', '欢' to '歡',
        '发' to '發', '现' to '現',
        '晚' to '晚', '安' to '安'
    )

    fun getCandidates(pinyin: String): List<String> {
        if (pinyin.isEmpty()) return emptyList()
        return pinyinDict[pinyin.lowercase()] ?: listOf(pinyin) // fallback to pinyin itself if not found
    }

    fun convertToTraditional(text: String): String {
        return text.map { char -> simpToTrad[char] ?: char }.joinActToString("")
    }

    // Custom joinToString to fix syntax
    private fun List<Char>.joinActToString(separator: String): String {
        val sb = StringBuilder()
        for (i in this.indices) {
            sb.append(this[i])
            if (i < this.size - 1) sb.append(separator)
        }
        return sb.toString()
    }
}
