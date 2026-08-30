package com.example.data.repository

import com.example.data.model.DuaItem

object DuasDataRepository {

    val categories = listOf(
        "أدعية القرآن الكريم",
        "أدعية الأنبياء",
        "الرزق والبركة",
        "تفريج الهم والكرب",
        "الشفاء والعافية",
        "طلب المغفرة والتوبة",
        "بر الوالدين",
        "النجاح والتوفيق",
        "الزواج والذرية الصالحة",
        "السفر والحفظ"
    )

    val allDuas: List<DuaItem> = listOf(
        // Quran Duas
        DuaItem(
            id = "qd1",
            title = "دعاء ربنا آتنا في الدنيا حسنة",
            arabicText = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            translation = "Our Lord, give us in this world that which is good and in the Hereafter that which is good and protect us from the punishment of the Fire.",
            category = "أدعية القرآن الكريم",
            source = "سورة البقرة: 201"
        ),
        DuaItem(
            id = "qd2",
            title = "دعاء الثبات على الهداية",
            arabicText = "رَبَّنَا لَا تُزِغْ قُلُوبَنَا بَعْدَ إِذْ هَدَيْتَنَا وَهَبْ لَنَا مِن لَّدُنكَ رَحْمَةً ۚ إِنَّكَ أَنتَ الْوَهَّابُ",
            translation = "Our Lord, let not our hearts deviate after You have guided us and grant us from Yourself mercy. Indeed, You are the Bestower.",
            category = "أدعية القرآن الكريم",
            source = "سورة آل عمران: 8"
        ),
        DuaItem(
            id = "qd3",
            title = "دعاء تيسير الأمر وشرح الصدر",
            arabicText = "رَبِّ اشْرَحْ لِي صَدْرِي * وَيَسِّرْ لِي أَمْرِي * وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي * يَفْقَهُوا قَوْلِي",
            translation = "My Lord, expand for me my breast [with assurance] and ease for me my task and untie the knot from my tongue that they may understand my speech.",
            category = "أدعية الأنبياء",
            source = "دعاء موسى عليه السلام - سورة طه"
        ),
        DuaItem(
            id = "qd4",
            title = "دعاء يونس عليه السلام في بطن الحوت",
            arabicText = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            translation = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
            category = "أدعية الأنبياء",
            source = "سورة الأنبياء: 87"
        ),
        DuaItem(
            id = "qd5",
            title = "دعاء أيوب عليه السلام عند المرض",
            arabicText = "أَنِّي مَسَّنِيَ الضُّرُّ وَأَنتَ أَرْحَمُ الرَّاحِمِينَ",
            translation = "Indeed, adversity has touched me, and you are the Most Merciful of the merciful.",
            category = "الشفاء والعافية",
            source = "دعاء أيوب عليه السلام - سورة الأنبياء"
        ),
        DuaItem(
            id = "qd6",
            title = "دعاء بر الوالدين والرحمة لهما",
            arabicText = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
            translation = "My Lord, have mercy upon them as they brought me up [when I was] small.",
            category = "بر الوالدين",
            source = "سورة الإسراء: 24"
        ),
        DuaItem(
            id = "qd7",
            title = "دعاء طلب الذرية الصالحة والزواج",
            arabicText = "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا",
            translation = "Our Lord, grant us from among our wives and offspring comfort to our eyes and make us an example for the righteous.",
            category = "الزواج والذرية الصالحة",
            source = "سورة الفرقان: 74"
        ),
        DuaItem(
            id = "qd8",
            title = "دعاء طلب سعة الرزق وتفريج الكرب",
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَأَعُوذُ بِكَ مِنَ الْعَجْزِ وَالْكَسَلِ، وَأَعُوذُ بِكَ مِنَ الْجُبْنِ وَالْبُخْلِ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ.",
            translation = "O Allah, I seek refuge in You from grief and sadness, from weakness and laziness...",
            category = "تفريج الهم والكرب",
            source = "صحيح البخاري"
        ),
        DuaItem(
            id = "qd9",
            title = "دعاء الاستشفاء وزيارة المريض",
            arabicText = "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَاسَ، اشْفِهِ وَأَنْتَ الشَّافِي، لاَ شِفَاءَ إِلاَّ شِفَاؤُكَ، شِفَاءً لاَ يُغَادِرُ سَقَماً.",
            translation = "O Allah, Lord of the people, remove the affliction and heal, You are the Healer...",
            category = "الشفاء والعافية",
            source = "صحيح البخاري ومسلم"
        ),
        DuaItem(
            id = "qd10",
            title = "دعاء طلب التوفيق والنجاح",
            arabicText = "اللَّهُمَّ لاَ سَهْلَ إِلاَّ مَا جَعَلْتَهُ سَهْلاً، وَأَنْتَ تَجْعَلُ الْحَزْنَ إِذَا شِئْتَ سَهْلاً.",
            translation = "O Allah, nothing is easy except what You have made easy, and You make hardship easy if You will.",
            category = "النجاح والتوفيق",
            source = "صحيح ابن حبان"
        )
    )
}
