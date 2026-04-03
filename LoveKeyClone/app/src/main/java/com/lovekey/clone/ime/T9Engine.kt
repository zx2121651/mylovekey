package com.lovekey.clone.ime

/**
 * A fast, Trie-based engine to convert T9 number sequences (e.g., "426")
 * into valid Pinyin syllables and prefixes (e.g., "hao", "han", "gao", "gan").
 * Now highly localized with frequency weights, abbreviations (initials), and sentence segmentation.
 */
object T9Engine {

    // A comprehensive list of valid Pinyin syllables (without tones)
    // with basic frequency weights (higher is more frequent)
    private val SYLLABLES = mapOf(
        "a" to 100, "ai" to 90, "an" to 80, "ang" to 70, "ao" to 80, "ba" to 90, "bai" to 80, "ban" to 80, "bang" to 80, "bao" to 90, "bei" to 90, "ben" to 80, "beng" to 70, "bi" to 90, "bian" to 80, "biao" to 80, "bie" to 80, "bin" to 70, "bing" to 80, "bo" to 80, "bu" to 90,
        "ca" to 50, "cai" to 80, "can" to 80, "cang" to 70, "cao" to 80, "ce" to 80, "cen" to 40, "ceng" to 60, "cha" to 80, "chai" to 70, "chan" to 80, "chang" to 90, "chao" to 80, "che" to 80, "chen" to 80, "cheng" to 90, "chi" to 80, "chong" to 80, "chou" to 80, "chu" to 90, "chua" to 10, "chuai" to 30, "chuan" to 80, "chuang" to 80, "chui" to 80, "chun" to 80, "chuo" to 60, "ci" to 80, "cong" to 80, "cou" to 60, "cu" to 70, "cuan" to 70, "cui" to 80, "cun" to 80, "cuo" to 80,
        "da" to 90, "dai" to 80, "dan" to 90, "dang" to 90, "dao" to 90, "de" to 100, "dei" to 70, "deng" to 80, "di" to 90, "dian" to 90, "diao" to 80, "die" to 80, "ding" to 90, "diu" to 60, "dong" to 90, "dou" to 80, "du" to 90, "duan" to 80, "dui" to 90, "dun" to 80, "duo" to 90,
        "e" to 80, "ei" to 50, "en" to 70, "eng" to 50, "er" to 80, "fa" to 90, "fan" to 90, "fang" to 90, "fei" to 80, "fen" to 80, "feng" to 90, "fo" to 60, "fou" to 60, "fu" to 90,
        "ga" to 60, "gai" to 80, "gan" to 90, "gang" to 80, "gao" to 90, "ge" to 90, "gei" to 80, "gen" to 70, "geng" to 80, "gong" to 90, "gou" to 80, "gu" to 80, "gua" to 80, "guai" to 70, "guan" to 90, "guang" to 80, "gui" to 80, "gun" to 70, "guo" to 90,
        "ha" to 80, "hai" to 90, "han" to 90, "hang" to 80, "hao" to 100, "he" to 90, "hei" to 80, "hen" to 80, "heng" to 80, "hong" to 90, "hou" to 80, "hu" to 90, "hua" to 90, "huai" to 80, "huan" to 90, "huang" to 80, "hui" to 90, "hun" to 80, "huo" to 90,
        "ji" to 90, "jia" to 90, "jian" to 90, "jiang" to 90, "jiao" to 90, "jie" to 90, "jin" to 90, "jing" to 90, "jiong" to 70, "jiu" to 90, "ju" to 90, "juan" to 80, "jue" to 80, "jun" to 80,
        "ka" to 70, "kai" to 90, "kan" to 90, "kang" to 80, "kao" to 80, "ke" to 90, "ken" to 70, "keng" to 70, "kong" to 80, "kou" to 80, "ku" to 80, "kua" to 80, "kuai" to 80, "kuan" to 80, "kuang" to 80, "kui" to 80, "kun" to 70, "kuo" to 80,
        "la" to 80, "lai" to 90, "lan" to 80, "lang" to 80, "lao" to 80, "le" to 100, "lei" to 80, "leng" to 80, "li" to 90, "lia" to 60, "lian" to 90, "liang" to 90, "liao" to 90, "lie" to 80, "lin" to 80, "ling" to 90, "liu" to 80, "long" to 80, "lou" to 80, "lu" to 80, "lv" to 80, "luan" to 80, "lve" to 70, "lun" to 80, "luo" to 80,
        "ma" to 90, "mai" to 90, "man" to 90, "mang" to 80, "mao" to 80, "me" to 90, "mei" to 90, "men" to 90, "meng" to 80, "mi" to 90, "mian" to 90, "miao" to 80, "mie" to 70, "min" to 80, "ming" to 90, "miu" to 50, "mo" to 80, "mou" to 70, "mu" to 80,
        "na" to 90, "nai" to 80, "nan" to 90, "nang" to 70, "nao" to 80, "ne" to 90, "nei" to 80, "nen" to 70, "neng" to 90, "ni" to 100, "nian" to 90, "niang" to 80, "niao" to 80, "nie" to 80, "nin" to 80, "ning" to 80, "niu" to 80, "nong" to 80, "nou" to 50, "nu" to 80, "nv" to 80, "nuan" to 80, "nve" to 60, "nuo" to 80,
        "o" to 70, "ou" to 80, "pa" to 80, "pai" to 80, "pan" to 80, "pang" to 80, "pao" to 80, "pei" to 80, "pen" to 70, "peng" to 80, "pi" to 80, "pian" to 80, "piao" to 80, "pie" to 70, "pin" to 80, "ping" to 90, "po" to 80, "pou" to 60, "pu" to 80,
        "qi" to 90, "qia" to 80, "qian" to 90, "qiang" to 90, "qiao" to 80, "qie" to 80, "qin" to 80, "qing" to 90, "qiong" to 70, "qiu" to 80, "qu" to 90, "quan" to 90, "que" to 80, "qun" to 80,
        "ran" to 80, "rang" to 80, "rao" to 80, "re" to 80, "ren" to 90, "reng" to 80, "ri" to 90, "rong" to 80, "rou" to 80, "ru" to 80, "ruan" to 80, "rui" to 80, "run" to 80, "ruo" to 80,
        "sa" to 80, "sai" to 80, "san" to 80, "sang" to 80, "sao" to 80, "se" to 80, "sen" to 70, "seng" to 60, "sha" to 80, "shai" to 70, "shan" to 90, "shang" to 90, "shao" to 80, "she" to 80, "shei" to 70, "shen" to 90, "sheng" to 90, "shi" to 100, "shou" to 90, "shu" to 90, "shua" to 80, "shuai" to 80, "shuan" to 70, "shuang" to 80, "shui" to 90, "shun" to 80, "shuo" to 90, "si" to 90, "song" to 80, "sou" to 70, "su" to 80, "suan" to 80, "sui" to 80, "sun" to 80, "suo" to 90,
        "ta" to 90, "tai" to 90, "tan" to 90, "tang" to 90, "tao" to 90, "te" to 90, "teng" to 80, "ti" to 90, "tian" to 90, "tiao" to 80, "tie" to 80, "ting" to 90, "tong" to 90, "tou" to 80, "tu" to 80, "tuan" to 80, "tui" to 80, "tun" to 80, "tuo" to 80,
        "wa" to 80, "wai" to 80, "wan" to 90, "wang" to 90, "wei" to 90, "wen" to 90, "weng" to 70, "wo" to 100, "wu" to 90,
        "xi" to 90, "xia" to 90, "xian" to 90, "xiang" to 90, "xiao" to 90, "xie" to 90, "xin" to 90, "xing" to 90, "xiong" to 80, "xiu" to 80, "xu" to 90, "xuan" to 80, "xue" to 90, "xun" to 80,
        "ya" to 90, "yan" to 90, "yang" to 90, "yao" to 90, "ye" to 90, "yi" to 90, "yin" to 90, "ying" to 90, "yo" to 70, "yong" to 90, "you" to 90, "yu" to 90, "yuan" to 90, "yue" to 90, "yun" to 90,
        "za" to 80, "zai" to 90, "zan" to 80, "zang" to 70, "zao" to 90, "ze" to 80, "zei" to 60, "zen" to 80, "zeng" to 80, "zha" to 80, "zhai" to 80, "zhan" to 90, "zhang" to 90, "zhao" to 90, "zhe" to 90, "zhei" to 70, "zhen" to 90, "zheng" to 90, "zhi" to 90, "zhong" to 90, "zhou" to 90, "zhu" to 90, "zhua" to 80, "zhuai" to 70, "zhuan" to 80, "zhuang" to 80, "zhui" to 80, "zhun" to 80, "zhuo" to 80, "zi" to 90, "zong" to 90, "zou" to 80, "zu" to 80, "zuan" to 70, "zui" to 90, "zun" to 80, "zuo" to 90
    )

    // Valid initials (Shengmu) for abbreviation (jianpin)
    private val INITIALS = listOf("b", "p", "m", "f", "d", "t", "n", "l", "g", "k", "h", "j", "q", "x", "zh", "ch", "sh", "r", "z", "c", "s", "y", "w")

    // Map each alphabet letter to its corresponding T9 digit
    private val LETTER_TO_DIGIT = mapOf(
        'a' to '2', 'b' to '2', 'c' to '2',
        'd' to '3', 'e' to '3', 'f' to '3',
        'g' to '4', 'h' to '4', 'i' to '4',
        'j' to '5', 'k' to '5', 'l' to '5',
        'm' to '6', 'n' to '6', 'o' to '6',
        'p' to '7', 'q' to '7', 'r' to '7', 's' to '7',
        't' to '8', 'u' to '8', 'v' to '8',
        'w' to '9', 'x' to '9', 'y' to '9', 'z' to '9'
    )

    private class TrieNode {
        // Children representing the next T9 digit ('2'..'9')
        val children = mutableMapOf<Char, TrieNode>()

        // Map of <Pinyin Prefix -> Weight>. Higher weight means more frequent/likely.
        val validPrefixes = mutableMapOf<String, Int>()
        // Flag to indicate if a full valid syllable or initial ends here
        var isWordEnd = false
    }

    private val root = TrieNode()

    init {
        // 1. Insert Full Syllables
        for ((syllable, weight) in SYLLABLES) {
            insertWord(syllable, weight, isInitial = false)
        }
        // 2. Insert Initials (Jianpin) with high weight so they can easily form sentences (like 'n' 'h' -> ni hao)
        for (initial in INITIALS) {
            insertWord(initial, 95, isInitial = true)
        }
    }

    private fun insertWord(word: String, weight: Int, isInitial: Boolean) {
        var currentNode = root
        var currentPrefix = ""

        for ((index, char) in word.withIndex()) {
            val digit = LETTER_TO_DIGIT[char] ?: continue

            // Move down the Trie along the digit path
            currentNode = currentNode.children.getOrPut(digit) { TrieNode() }

            currentPrefix += char

            // If the current prefix already exists, keep the max weight
            val existingWeight = currentNode.validPrefixes[currentPrefix] ?: 0
            currentNode.validPrefixes[currentPrefix] = maxOf(existingWeight, weight)

            // Mark end of word
            if (index == word.length - 1) {
                currentNode.isWordEnd = true
            }
        }
    }

    /**
     * Given a sequence of T9 digits (e.g., "426"),
     * returns a list of valid Pinyin single-syllable combinations sorted by frequency weight.
     * e.g., "426" -> ["hao", "han", "gan", "gao", "iao"]
     */
    fun getSingleCombinations(numberSequence: String): List<String> {
        if (numberSequence.isEmpty()) return emptyList()

        var currentNode = root
        for (digit in numberSequence) {
            // '1' can act as an explicit separator, but for single syllables we might stop or ignore
            if (digit == '1') continue

            val nextNode = currentNode.children[digit]
            if (nextNode != null) {
                currentNode = nextNode
            } else {
                return emptyList()
            }
        }

        // Return prefixes sorted by weight descending, then alphabetically
        return currentNode.validPrefixes.entries
            .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
            .map { it.key }
    }

    /**
     * DFS + Memoization to segment a long T9 sequence into possible Pinyin sentence phrases.
     * Handles abbreviations (e.g. 64 -> n h -> ni hao) and full spellings (64426 -> ni hao).
     * '1' is treated as an explicit syllable separator (apostrophe).
     */
    fun segmentT9Sequence(sequence: String): List<String> {
        val memo = mutableMapOf<Int, List<List<String>>>()

        fun dfs(startIndex: Int): List<List<String>> {
            if (startIndex == sequence.length) return listOf(emptyList())
            if (memo.containsKey(startIndex)) return memo[startIndex]!!

            val results = mutableListOf<List<String>>()
            var currentNode = root

            // Explicit separator handling: if the first digit is '1', skip it and continue parsing the rest as a new syllable.
            if (sequence[startIndex] == '1') {
                val subResults = dfs(startIndex + 1)
                // We add an explicit "'" marker so the UI knows it was split by the user
                for (sub in subResults) {
                    val newSeq = mutableListOf("'")
                    newSeq.addAll(sub)
                    results.add(newSeq)
                }
                memo[startIndex] = results
                return results
            }

            for (i in startIndex until sequence.length) {
                val digit = sequence[i]
                if (digit == '1') {
                    // A separator '1' forces a syllable break here.
                    // We only accept the current path if it's a valid word end.
                    if (currentNode.isWordEnd) {
                        // Find the highest weighted exact word at this node
                        val exactWords = currentNode.validPrefixes.keys.filter { SYLLABLES.containsKey(it) || INITIALS.contains(it) }
                        if (exactWords.isNotEmpty()) {
                            val bestWord = exactWords.maxByOrNull { SYLLABLES[it] ?: 95 } ?: exactWords.first()
                            val subResults = dfs(i) // pass the '1' to the next dfs so it handles it as an explicit divider
                            for (sub in subResults) {
                                val newSeq = mutableListOf(bestWord)
                                newSeq.addAll(sub)
                                results.add(newSeq)
                            }
                        }
                    }
                    break // Stop traversing deeper down the Trie for this word
                }

                val nextNode = currentNode.children[digit] ?: break
                currentNode = nextNode

                if (currentNode.isWordEnd) {
                    // Collect all exact valid words (not just prefixes) at this node
                    val exactWords = currentNode.validPrefixes.keys.filter { SYLLABLES.containsKey(it) || INITIALS.contains(it) }

                    // We just take the most likely word (or top few) for performance.
                    val bestWords = exactWords.sortedByDescending { SYLLABLES[it] ?: 95 }.take(2)

                    val subResults = dfs(i + 1)

                    for (word in bestWords) {
                        for (sub in subResults) {
                            val newSeq = mutableListOf(word)
                            newSeq.addAll(sub)
                            results.add(newSeq)
                        }
                    }
                }
            }

            // Fallback: If we couldn't form any valid complete words but we have a valid prefix,
            // we can treat the whole remaining sequence as a prefix (useful when the user is still typing).
            if (results.isEmpty() && startIndex == 0 && currentNode.validPrefixes.isNotEmpty()) {
                val bestPrefix = currentNode.validPrefixes.maxByOrNull { it.value }?.key
                if (bestPrefix != null) {
                    results.add(listOf(bestPrefix))
                }
            }

            memo[startIndex] = results
            return results
        }

        val allSegments = dfs(0)

        // Format the segments into strings (e.g. ["ni", "hao"] -> "ni hao" or "ni'hao")
        // We'll use a space for standard segmentation, and remove the explicit "'" placeholders by just appending closely
        val formatted = allSegments.map { segmentList ->
            val sb = java.lang.StringBuilder()
            for (i in segmentList.indices) {
                val token = segmentList[i]
                if (token == "'") {
                    sb.append("'") // append explicit separator directly
                } else {
                    if (i > 0 && sb.isNotEmpty() && sb.last() != '\'') {
                        sb.append(" ") // space as a soft separator for readability
                    }
                    sb.append(token)
                }
            }
            sb.toString()
        }.distinct()

        // Give a little bonus sort: fewer spaces/splits are often preferred, or we just trust the DFS order
        return formatted.sortedBy { it.length }.take(10)
    }
}
