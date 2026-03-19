package com.lovekey.clone.data

data class Persona(
    val id: String,
    val icon: String = "",
    val text: String = "",
    val title: String = "",
    val hot: Boolean = false,
    val rank: Int = 0,
    val desc: String = "",
    val usage: String = "",
    val added: Boolean = false
)

object MockData {
    val PERSONA_DATA = listOf(
        Persona(id = "1", icon = "🎁", text = "风流浪子", hot = true),
        Persona(id = "2", icon = "🍭", text = "话题延伸", hot = false),
        Persona(id = "3", icon = "🌱", text = "温柔体贴", hot = true),
        Persona(id = "4", icon = "🙄", text = "怼一下", hot = true),
        Persona(id = "5", icon = "🎠", text = "富婆の爱", hot = true),
        Persona(id = "6", icon = "😎", text = "湘伢子", hot = false),
        Persona(id = "7", icon = "🦁", text = "霸道总裁", hot = true),
        Persona(id = "8", icon = "🏹", text = "深夜热聊", hot = false),
        Persona(id = "9", icon = "🧥", text = "成熟稳重", hot = true),
        Persona(id = "10", icon = "😋", text = "幽默有梗", hot = true),
        Persona(id = "11", icon = "🤞", text = "安慰鼓励", hot = false),
        Persona(id = "12", icon = "😆", text = "川渝男友", hot = false)
    )

    val GROUPS = listOf(
        listOf(PERSONA_DATA[0], PERSONA_DATA[1], PERSONA_DATA[2]),
        listOf(PERSONA_DATA[3], PERSONA_DATA[4], PERSONA_DATA[5]),
        listOf(PERSONA_DATA[6], PERSONA_DATA[7], PERSONA_DATA[8]),
        listOf(PERSONA_DATA[9], PERSONA_DATA[10], PERSONA_DATA[11])
    )

    val PERSONAS_REPLY = listOf(
        Persona(id = "r1", rank = 1, title = "高情商", desc = "把难说的话说得漂亮又舒服...", usage = "8395.7w", added = true),
        Persona(id = "r2", rank = 2, title = "幽默", desc = "把尴尬变笑点，聊天永远不...", usage = "4894.2w", added = true),
        Persona(id = "r3", rank = 3, title = "情场高手", desc = "节奏把握精准，情绪进退自...", usage = "2100.1w", added = true),
        Persona(id = "r4", rank = 4, title = "心动狙击", desc = "不露痕迹地撩，句句都在加...", usage = "7385.4w", added = true),
        Persona(id = "r5", rank = 5, title = "暧昧拉扯", desc = "话留余地，情绪却恰到好处...", usage = "2447.3w", added = true),
        Persona(id = "r6", rank = 6, title = "暖男", desc = "细节关怀在线，说话让人安...", usage = "1796.3w", added = true),
        Persona(id = "r7", rank = 7, title = "幽默有梗", desc = "逗她开心，是最重要的小事", usage = "989.7w", added = true),
        Persona(id = "r8", rank = 8, title = "情绪价值", desc = "懂共情会安慰，稳稳接住你...", usage = "502w", added = false),
        Persona(id = "r9", rank = 9, title = "双商在线", desc = "通透清醒进退有度，相处舒...", usage = "477w", added = true),
        Persona(id = "r10", rank = 10, title = "温柔体贴", desc = "话语细腻周到，让人感到被...", usage = "776w", added = true)
    )

    val PERSONAS_TALK = listOf(
        Persona(id = "t1", title = "情绪价值", desc = "懂情绪又懂安慰，是懂人心的高手💕", added = false),
        Persona(id = "t2", title = "心动狙击", desc = "懂分寸会撩，气氛刚刚好😉", added = false),
        Persona(id = "t3", title = "真情告白", desc = "不拐弯，直接把心掏给你❤️", added = false),
        Persona(id = "t4", title = "自然表达", desc = "说话变得自然流畅👏", added = true),
        Persona(id = "t5", title = "高情商", desc = "说话有温度，让人舒服到想续聊💬", added = true),
        Persona(id = "t6", title = "幽默", desc = "自带笑点buff，让聊天不冷场🤣", added = true),
        Persona(id = "t7", title = "双商在线", desc = "逻辑清晰又懂人情，稳得一批🧠", added = true),
        Persona(id = "t8", title = "夸夸TA", desc = "彩虹屁满分，让人开心到飞起🌈", added = true),
        Persona(id = "t9", title = "咸鱼躺平", desc = "不卷不装，佛系聊天最高级🛋️", added = false)
    )

    fun getZodiac(month: Int, day: Int): String {
        val dates = listOf(20, 19, 21, 20, 21, 21, 23, 23, 23, 23, 22, 22)
        val signs = listOf("摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座")
        return if (day < dates[month - 1]) signs[month - 1] else signs[month % 12]
    }
}
