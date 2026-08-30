package com.example.data.repository

import com.example.data.model.QuranVerse
import com.example.data.model.Surah

object QuranDataRepository {

    val surahsList: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", "The Opening", 7, "مكية", listOf(
            QuranVerse(1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "أعظم سورة في القرآن الكريم، تسمى أم الكتاب والسبع المثاني."),
            QuranVerse(2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "[All] praise is [due] to Allah, Lord of the worlds.", "الحمد والشكر لله وحده المستحق للعبادة وخالق الخلق أجمعين."),
            QuranVerse(3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful,", "صفات رحمة الله الشاملة لجميع خلقه في الدنيا والخاصة بالمؤمنين في الآخرة."),
            QuranVerse(4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense.", "الملك المتصرف في يوم القيامة والحساب والجزاء."),
            QuranVerse(5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "It is You we worship and You we ask for help.", "إخلاص العبادة والاستعانة بالله وحده لا شريك له."),
            QuranVerse(6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path -", "طلب الهداية والثبات على دين الحق والإسلام."),
            QuranVerse(7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.", "طريق النبيين والصديقين والشهداء والصالحين.")
        )),
        Surah(2, "البقرة", "Al-Baqarah", "The Cow", 286, "مدنية", listOf(
            QuranVerse(1, "الم", "Alif, Lam, Meem.", "حروف مقطعة لبيان إعجاز القرآن الكريم."),
            QuranVerse(2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "This is the Book about which there is no doubt, a guidance for those conscious of Allah -", "كتاب منزل من عند الله لا شك فيه يهدي القلوب الحية."),
            QuranVerse(255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep...", "آية الكرسي: أعظم آية في كتاب الله تعالى، تحفظ قارئها من كل شر."),
            QuranVerse(285, "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ", "The Messenger has believed in what was revealed to him from his Lord, and [so have] the believers...", "خواتيم سورة البقرة: من قرأهما في ليلة كفتاه."),
            QuranVerse(286, "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا", "Allah does not charge a soul except [with that within] its capacity...", "دعاء جامع لطلب العفو والرحمة والمغفرة والنصر.")
        )),
        Surah(3, "آل عمران", "Ali 'Imran", "Family of Imran", 200, "مدنية"),
        Surah(4, "النساء", "An-Nisa", "The Women", 176, "مدنية"),
        Surah(5, "المائدة", "Al-Ma'idah", "The Table Spread", 120, "مدنية"),
        Surah(6, "الأنعام", "Al-An'am", "The Cattle", 165, "مكية"),
        Surah(7, "الأعراف", "Al-A'raf", "The Heights", 206, "مكية"),
        Surah(8, "الأنفال", "Al-Anfal", "The Spoils of War", 75, "مدنية"),
        Surah(9, "التوبة", "At-Tawbah", "The Repentance", 129, "مدنية"),
        Surah(10, "يونس", "Yunus", "Jonah", 109, "مكية"),
        Surah(11, "هود", "Hud", "Hud", 123, "مكية"),
        Surah(12, "يوسف", "Yusuf", "Joseph", 111, "مكية"),
        Surah(13, "الرعد", "Ar-Ra'd", "The Thunder", 43, "مدنية"),
        Surah(14, "إبراهيم", "Ibrahim", "Abraham", 52, "مكية"),
        Surah(15, "الحجر", "Al-Hijr", "The Rocky Tract", 99, "مكية"),
        Surah(16, "النحل", "An-Nahl", "The Bee", 128, "مكية"),
        Surah(17, "الإسراء", "Al-Isra", "The Night Journey", 111, "مكية"),
        Surah(18, "الكهف", "Al-Kahf", "The Cave", 110, "مكية", listOf(
            QuranVerse(1, "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا", "[All] praise is [due] to Allah, who has sent down upon His Servant the Book and has not made therein any deviance.", "استحباب قراءة سورة الكهف كل يوم جمعة تُضيء ما بين الجمعتين."),
            QuranVerse(10, "إِذْ أَوَى الْفِتْيَةُ إِلَى الْكَهْفِ فَقَالُوا رَبَّنَا آتِنَا مِن لَّدُنكَ رَحْمَةً وَهَيِّئْ لَنَا مِنْ أَمْرِنَا رَشَدًا", "When the youths retreated to the cave and said, 'Our Lord, grant us from Yourself mercy and prepare for us from our affair right guidance.'", "دعاء الفتية بالرحمة والرشد عند الشدائد."),
            QuranVerse(46, "الْمَالُ وَالْبَنُونَ زِينَةُ الْحَيَاةِ الدُّنْيَا ۖ وَالْبَاقِيَاتُ الصَّالِحَاتُ خَيْرٌ عِندَ رَبِّكَ ثَوَابًا وَخَيْرٌ أَمَلًا", "Wealth and children are [but] adornment of the worldly life. But the enduring good deeds are better to your Lord for reward and better for [one's] hope.", "بيان فضل الباقيات الصالحات (سبحان الله، الحمد لله، لا إله إلا الله، والله أكبر)."),
            QuranVerse(110, "قُلْ إِنَّمَا أَنَا بَشَرٌ مِّثْلُكُمْ يُوحَىٰ إِلَيَّ أَنَّمَا إِلَٰهُكُمْ إِلَٰهٌ وَاحِدٌ ۖ فَمَن كَانَ يَرْجُو لِقَاءَ رَبِّهِ فَلْيَعْمَلْ عَمَلًا صَالِحًا وَلَا يُشْرِكْ بِعِبَادَةِ رَبِّهِ أَحَدًا", "Say, 'I am only a man like you, to whom has been revealed that your god is one God. So whoever would hope for the meeting with his Lord - let him do righteous work...'", "خاتمة سورة الكهف: شرطا قبول العمل الإخلاص والمتابعة.")
        )),
        Surah(19, "مريم", "Maryam", "Mary", 98, "مكية"),
        Surah(20, "طه", "Ta-Ha", "Ta-Ha", 135, "مكية"),
        Surah(21, "الأنبياء", "Al-Anbiya", "The Prophets", 112, "مكية"),
        Surah(22, "الحج", "Al-Hajj", "The Pilgrimage", 78, "مدنية"),
        Surah(23, "المؤمنون", "Al-Mu'minun", "The Believers", 118, "مكية"),
        Surah(24, "النور", "An-Nur", "The Light", 64, "مدنية", listOf(
            QuranVerse(35, "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ ۚ مَثَلُ نُورِهِ كَمِشْكَاةٍ فِيهَا مِصْبَاحٌ ۖ الْمِصْبَاحُ فِي زُجَاجَةٍ ۖ الزُّجَاجَةُ كَأَنَّهَا كَوْكَبٌ دُرِّيٌّ يُوقَدُ مِن شَجَرَةٍ مُّبَارَكَةٍ زَيْتُونَةٍ لَّا شَرْقِيَّةٍ وَلَا غَرْبِيَّةٍ يَكَادُ زَيْتُهَا يُضِيءُ وَلَوْ لَمْ تَمْسَسْهُ نَارٌ ۚ نُّورٌ عَلَىٰ نُورٍ ۗ يَهْدِي اللَّهُ لِنُورِهِ مَن يَشَاءُ", "Allah is the Light of the heavens and the earth. The example of His light is like a niche within which is a lamp...", "آية النور العظيمة: بيان نور الله وهدايته لقلوب المؤمنين.")
        )),
        Surah(25, "الفرقان", "Al-Furqan", "The Criterion", 77, "مكية"),
        Surah(36, "يس", "Ya-Sin", "Ya-Sin", 83, "مكية", listOf(
            QuranVerse(1, "يس", "Ya-Sin.", "حروف مقطعة وقسم بكتاب الله العزيز."),
            QuranVerse(2, "وَالْقُرْآنِ الْحَكِيمِ", "By the wise Qur'an.", "قسم بالقرآن الحكيم المحكم المنزه عن الخطأ."),
            QuranVerse(58, "سَلَامٌ قَوْلًا مِّن رَّبٍّ رَّحِيمٍ", "'Peace,' a word from a Merciful Lord.", "تحية أهل الجنة من ربهم الرحيم الكريم.")
        )),
        Surah(55, "الرحمن", "Ar-Rahman", "The Beneficent", 78, "مدنية", listOf(
            QuranVerse(1, "الرَّحْمَٰنُ", "The Entirely Merciful", "افتتاح باسم الرحمن لبيان عظيم نعمه."),
            QuranVerse(2, "عَلَّمَ الْقُرْآنَ", "Taught the Qur'an,", "أعظم نعمة أنزلها على عباده."),
            QuranVerse(13, "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ", "So which of the favors of your Lord would you deny?", "تذكير مستمر بنعم الله وآلائه على الثقلين الإنس والجن.")
        )),
        Surah(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", 96, "مكية"),
        Surah(67, "الملك", "Al-Mulk", "The Sovereignty", 30, "مكية", listOf(
            QuranVerse(1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Blessed is He in whose hand is dominion, and He is over all things competent -", "سورة تبارك المنجية من عذاب القبر."),
            QuranVerse(2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -", "الغاية من الخلق هو الابتلاء والعمل الصالح.")
        )),
        Surah(112, "الإخلاص", "Al-Ikhlas", "The Sincerity", 4, "مكية", listOf(
            QuranVerse(1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, 'He is Allah, [who is] One,", "توحيد الله وتفرده بالألوهية، تعدل ثلث القرآن الكريم."),
            QuranVerse(2, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge.", "الذي تصمد إليه الخلائق في حوائجها ورغائبها."),
            QuranVerse(3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born,", "تنزيه الله عن الولد والوالد والشريك."),
            QuranVerse(4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent.'", "ليس كمثله شيء وهو السميع البصير.")
        )),
        Surah(113, "الفلق", "Al-Falaq", "The Daybreak", 5, "مكية", listOf(
            QuranVerse(1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, 'I seek refuge in the Lord of daybreak", "الاستعاذة برب الصبح وفالق الإصباح."),
            QuranVerse(2, "مِن شَرِّ مَا خَلَقَ", "From the evil of that which He created", "من شر جميع المخلوقات."),
            QuranVerse(3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles", "من شر ظلمة الليل إذا أقبلت."),
            QuranVerse(4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots", "من شر السحرة والحاسدين."),
            QuranVerse(5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies.'", "الحماية والتحصين من عين وحسد الحاسد.")
        )),
        Surah(114, "الناس", "An-Nas", "Mankind", 6, "مكية", listOf(
            QuranVerse(1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Say, 'I seek refuge in the Lord of mankind,", "الاعتصام بخالق البشر ومدبر أمورهم."),
            QuranVerse(2, "مَلِكِ النَّاسِ", "The Sovereign of mankind,", "الملك الحق الذي لا ملك سواه."),
            QuranVerse(3, "إِلَٰهِ النَّاسِ", "The God of mankind,", "المعبود بحق وحده."),
            QuranVerse(4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "From the evil of the retreating whisperer -", "الشيطان الذي يوسوس عند الغفلة ويخنس عند ذكر الله."),
            QuranVerse(5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Who whispers into the breasts of mankind -", "يلقي الشبهات والشهوات في الصدور."),
            QuranVerse(6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "From among the jinn and mankind.'", "التحصن من وسوسة شياطين الإنس والجن.")
        ))
    )

    fun getSurahByNumber(number: Int): Surah? {
        return surahsList.find { it.number == number }
    }

    val readersList = listOf(
        "مشاري بن راشد العفاسي",
        "عبد الباسط عبد الصمد",
        "محمود خليل الحصري",
        "سعود الشريم",
        "ماهر المعيقلي"
    )
}
