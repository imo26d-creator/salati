package com.example.data.repository

import com.example.data.model.QuranReciter
import com.example.data.model.QuranVerse
import com.example.data.model.Surah

object QuranDataRepository {

    val recitersList: List<QuranReciter> = listOf(
        QuranReciter(
            id = "mishari_alafasy",
            nameArabic = "مشاري بن راشد العفاسي",
            nameEnglish = "Mishary Rashid Alafasy",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server8.mp3quran.net/afs/",
            description = "تلاوة عذبة وندية تمتاز بالخشوع والوضوح التام",
            emojiAvatar = "🎙️"
        ),
        QuranReciter(
            id = "abdulbasit_murattal",
            nameArabic = "عبد الباسط عبد الصمد (مرتل)",
            nameEnglish = "Abdulbasit Abdulsamad (Murattal)",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server7.mp3quran.net/basit/",
            description = "الصوت الذهبي الخالد وإتقان فريد لأحكام التجويد",
            emojiAvatar = "✨"
        ),
        QuranReciter(
            id = "abdulbasit_mujawwad",
            nameArabic = "عبد الباسط عبد الصمد (مجود)",
            nameEnglish = "Abdulbasit Abdulsamad (Mujawwad)",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server7.mp3quran.net/basit_mojawwad/",
            description = "روائع التلاوة المجودة والمقامات القرآنية الأصيلة",
            emojiAvatar = "🕌"
        ),
        QuranReciter(
            id = "maher_al_muaiqly",
            nameArabic = "ماهر المعيقلي",
            nameEnglish = "Maher Al-Muaiqly",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server12.mp3quran.net/maher/",
            description = "إمام وخطيب المسجد الحرام وتلاوة تفيض بالسكينة",
            emojiAvatar = "🕋"
        ),
        QuranReciter(
            id = "al_hussary",
            nameArabic = "محمود خليل الحصري",
            nameEnglish = "Mahmoud Khalil Al-Hussary",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server13.mp3quran.net/husr/",
            description = "شيخ المقارئ المصرية وأدق تلاوة تعليمية ومخارج حروف",
            emojiAvatar = "📜"
        ),
        QuranReciter(
            id = "al_minshawi_murattal",
            nameArabic = "محمد صديق المنشاوي (مرتل)",
            nameEnglish = "Mohamed Siddiq Al-Minshawi (Murattal)",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server10.mp3quran.net/minsh/",
            description = "الصوت الباكي الحزين والتلاوة المؤثرة الخاشعة",
            emojiAvatar = "🌿"
        ),
        QuranReciter(
            id = "saad_al_ghamdi",
            nameArabic = "سعد الغامدي",
            nameEnglish = "Saad Al-Ghamdi",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server7.mp3quran.net/s_gmd/",
            description = "تلاوة شجية وإيقاع متزن محبوب لدى ملايين المسلمين",
            emojiAvatar = "🕊️"
        ),
        QuranReciter(
            id = "yasser_al_dossari",
            nameArabic = "ياسر الدوسري",
            nameEnglish = "Yasser Al-Dossari",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server11.mp3quran.net/yasser/",
            description = "إمام المسجد الحرام ونبرة حماسية آسرة",
            emojiAvatar = "🌟"
        ),
        QuranReciter(
            id = "islam_sobhi",
            nameArabic = "إسلام صبحي",
            nameEnglish = "Islam Sobhi",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server14.mp3quran.net/islam/",
            description = "صوت شبابي هادئ يبعث الطمأنينة والراحة النفسية",
            emojiAvatar = "🌙"
        ),
        QuranReciter(
            id = "ahmed_al_ajmi",
            nameArabic = "أحمد بن علي العجمي",
            nameEnglish = "Ahmed Al-Ajmi",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server10.mp3quran.net/ajm/",
            description = "تلاوة قوية مميزة وأداء مؤثر",
            emojiAvatar = "📖"
        ),
        QuranReciter(
            id = "saud_al_shuraim",
            nameArabic = "سعود الشريم",
            nameEnglish = "Saud Al-Shuraim",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server7.mp3quran.net/shur/",
            description = "إمام الحرم المكي الشريف سابقاً ونبرة جهورية وقورة",
            emojiAvatar = "🕋"
        ),
        QuranReciter(
            id = "abdulrahman_al_sudais",
            nameArabic = "عبد الرحمن السديس",
            nameEnglish = "Abdul Rahman Al-Sudais",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server11.mp3quran.net/sds/",
            description = "رئيس الشؤون الدينية بالمسجد الحرام وإمام الحرم التاريخي",
            emojiAvatar = "🕋"
        ),
        QuranReciter(
            id = "abu_bakr_al_shatri",
            nameArabic = "أبو بكر الشاطري",
            nameEnglish = "Abu Bakr Al-Shatri",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server11.mp3quran.net/shatri/",
            description = "ترتيل هادئ ورائع بأسلوب متفرد",
            emojiAvatar = "🎧"
        ),
        QuranReciter(
            id = "hazza_al_balushi",
            nameArabic = "هزاع البلوشي",
            nameEnglish = "Hazza Al Balushi",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server11.mp3quran.net/hazza/",
            description = "صوت شجي ومريح للأعصاب مع تدبر عميق",
            emojiAvatar = "✨"
        ),
        QuranReciter(
            id = "nasser_al_qatami",
            nameArabic = "ناصر القطامي",
            nameEnglish = "Nasser Al-Qatami",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server6.mp3quran.net/qtm/",
            description = "قراءة مؤثرة ومقامات روحانية مميزة",
            emojiAvatar = "🌺"
        ),
        QuranReciter(
            id = "khaled_al_jaleel",
            nameArabic = "خالد الجليل",
            nameEnglish = "Khaled Al-Jaleel",
            rewayaArabic = "حفص عن عاصم",
            serverUrl = "https://server10.mp3quran.net/jleel/",
            description = "تلاوة تبكي القلوب وتقشعر لها الأبدان",
            emojiAvatar = "💫"
        )
    )

    fun getAudioUrl(reciter: QuranReciter, surahNumber: Int): String {
        val formattedSurah = String.format("%03d.mp3", surahNumber)
        val base = if (reciter.serverUrl.endsWith("/")) reciter.serverUrl else "${reciter.serverUrl}/"
        return "$base$formattedSurah"
    }

    // Complete list of all 114 Surahs of the Holy Quran
    val surahsList: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", "The Opening", 7, "مكية", 1, 1, listOf(
            QuranVerse(1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "أعظم سورة في القرآن الكريم، تسمى أم الكتاب والسبع المثاني."),
            QuranVerse(2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "[All] praise is [due] to Allah, Lord of the worlds.", "الحمد والشكر لله وحده المستحق للعبادة وخالق الخلق أجمعين."),
            QuranVerse(3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful,", "صفات رحمة الله الشاملة لجميع خلقه في الدنيا والخاصة بالمؤمنين في الآخرة."),
            QuranVerse(4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense.", "الملك المتصرف في يوم القيامة والحساب والجزاء."),
            QuranVerse(5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "It is You we worship and You we ask for help.", "إخلاص العبادة والاستعانة بالله وحده لا شريك له."),
            QuranVerse(6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path -", "طلب الهداية والثبات على دين الحق والإسلام."),
            QuranVerse(7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.", "طريق النبيين والصديقين والشهداء والصالحين.")
        )),
        Surah(2, "البقرة", "Al-Baqarah", "The Cow", 286, "مدنية", 2, 1, listOf(
            QuranVerse(1, "الم", "Alif, Lam, Meem.", "حروف مقطعة لبيان إعجاز القرآن الكريم."),
            QuranVerse(2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "This is the Book about which there is no doubt, a guidance for those conscious of Allah -", "كتاب منزل من عند الله لا شك فيه يهدي القلوب الحية."),
            QuranVerse(3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ", "Who believe in the unseen, establish prayer, and spend out of what We have provided for them,", "أول صفات المتقين الفائزين برضوان الله."),
            QuranVerse(255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep...", "آية الكرسي: أعظم آية في كتاب الله تعالى، تحفظ قارئها من كل شر."),
            QuranVerse(285, "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ", "The Messenger has believed in what was revealed to him from his Lord, and [so have] the believers...", "خواتيم سورة البقرة: من قرأهما في ليلة كفتاه."),
            QuranVerse(286, "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا", "Allah does not charge a soul except [with that within] its capacity...", "دعاء جامع لطلب العفو والرحمة والمغفرة والنصر.")
        )),
        Surah(3, "آل عمران", "Ali 'Imran", "Family of Imran", 200, "مدنية", 50, 3, listOf(
            QuranVerse(1, "الم", "Alif, Lam, Meem.", "حروف مقطعة لبيان إعجاز القرآن."),
            QuranVerse(2, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of existence.", "إثبات وحدانية الله وقيوميته."),
            QuranVerse(18, "شَهِدَ اللَّهُ أَنَّهُ لَا إِلَٰهَ إِلَّا هُوَ وَالْمَلَائِكَةُ وَأُولُو الْعِلْمِ قَائِمًا بِالْقِسْطِ", "Allah witnesses that there is no deity except Him, and [so do] the angels and those of knowledge...", "أعظم شهادة بالتوحيد والعدل.")
        )),
        Surah(4, "النساء", "An-Nisa", "The Women", 176, "مدنية", 77, 4),
        Surah(5, "المائدة", "Al-Ma'idah", "The Table Spread", 120, "مدنية", 106, 6),
        Surah(6, "الأنعام", "Al-An'am", "The Cattle", 165, "مكية", 128, 7),
        Surah(7, "الأعراف", "Al-A'raf", "The Heights", 206, "مكية", 151, 8),
        Surah(8, "الأنفال", "Al-Anfal", "The Spoils of War", 75, "مدنية", 177, 9),
        Surah(9, "التوبة", "At-Tawbah", "The Repentance", 129, "مدنية", 187, 10),
        Surah(10, "يونس", "Yunus", "Jonah", 109, "مكية", 208, 11),
        Surah(11, "هود", "Hud", "Hud", 123, "مكية", 221, 11),
        Surah(12, "يوسف", "Yusuf", "Joseph", 111, "مكية", 235, 12, listOf(
            QuranVerse(1, "الر ۚ تِلْكَ آيَاتُ الْكِتَابِ الْمُبِينِ", "Alif, Lam, Ra. These are the verses of the clear Book.", "كتاب مبين للحق والهدى."),
            QuranVerse(3, "نَحْنُ نَقُصُّ عَلَيْكَ أَحْسَنَ الْقَصَصِ بِمَا أَوْحَيْنَا إِلَيْكَ هَٰذَا الْقُرْآنَ", "We relate to you, [O Muhammad], the best of stories in what We have revealed to you of this Qur'an...", "أحسن القصص لما فيها من العبر والصبر والفرج بعد الشدة.")
        )),
        Surah(13, "الرعد", "Ar-Ra'd", "The Thunder", 43, "مدنية", 249, 13, listOf(
            QuranVerse(28, "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ", "Those who have believed and whose hearts are assured by the remembrance of Allah. Unquestionably, by the remembrance of Allah hearts are assured.", "فضل ذكر الله في طمأنينة وسكينة النفوس.")
        )),
        Surah(14, "إبراهيم", "Ibrahim", "Abraham", 52, "مكية", 255, 13),
        Surah(15, "الحجر", "Al-Hijr", "The Rocky Tract", 99, "مكية", 262, 14),
        Surah(16, "النحل", "An-Nahl", "The Bee", 128, "مكية", 267, 14),
        Surah(17, "الإسراء", "Al-Isra", "The Night Journey", 111, "مكية", 282, 15),
        Surah(18, "الكهف", "Al-Kahf", "The Cave", 110, "مكية", 293, 15, listOf(
            QuranVerse(1, "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا", "[All] praise is [due] to Allah, who has sent down upon His Servant the Book and has not made therein any deviance.", "استحباب قراءة سورة الكهف كل يوم جمعة تُضيء ما بين الجمعتين."),
            QuranVerse(10, "إِذْ أَوَى الْفِتْيَةُ إِلَى الْكَهْفِ فَقَالُوا رَبَّنَا آتِنَا مِن لَّدُنكَ رَحْمَةً وَهَيِّئْ لَنَا مِنْ أَمْرِنَا رَشَدًا", "When the youths retreated to the cave and said, 'Our Lord, grant us from Yourself mercy and prepare for us from our affair right guidance.'", "دعاء الفتية بالرحمة والرشد عند الشدائد."),
            QuranVerse(46, "الْمَالُ وَالْبَنُونَ زِينَةُ الْحَيَاةِ الدُّنْيَا ۖ وَالْبَاقِيَاتُ الصَّالِحَاتُ خَيْرٌ عِندَ رَبِّكَ ثَوَابًا وَخَيْرٌ أَمَلًا", "Wealth and children are [but] adornment of the worldly life. But the enduring good deeds are better to your Lord for reward and better for [one's] hope.", "بيان فضل الباقيات الصالحات (سبحان الله، والحمد لله، ولا إله إلا الله، والله أكبر)."),
            QuranVerse(110, "قُلْ إِنَّمَا أَنَا بَشَرٌ مِّثْلُكُمْ يُوحَىٰ إِلَيَّ أَنَّمَا إِلَٰهُكُمْ إِلَٰهٌ وَاحِدٌ ۖ فَمَن كَانَ يَرْجُو لِقَاءَ رَبِّهِ فَلْيَعْمَلْ عَمَلًا صَالِحًا وَلَا يُشْرِكْ بِعِبَادَةِ رَبِّهِ أَحَدًا", "Say, 'I am only a man like you, to whom has been revealed that your god is one God. So whoever would hope for the meeting with his Lord - let him do righteous work...'", "خاتمة سورة الكهف: شرطا قبول العمل الإخلاص والمتابعة.")
        )),
        Surah(19, "مريم", "Maryam", "Mary", 98, "مكية", 305, 16),
        Surah(20, "طه", "Ta-Ha", "Ta-Ha", 135, "مكية", 312, 16),
        Surah(21, "الأنبياء", "Al-Anbiya", "The Prophets", 112, "مكية", 322, 17, listOf(
            QuranVerse(87, "وَذَا النُّونِ إِذ ذَّهَبَ مُغَاضِبًا فَظَنَّ أَن لَّن نَّقْدِرَ عَلَيْهِ فَنَادَىٰ فِي الظُّلُمَاتِ أَن لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ", "And [mention] the man of the fish, when he went off in anger and thought that We would not decree [anything] upon him...", "دعاء يونس عليه السلام (دعوة ذي النون) لتفريج كل هم وكرب.")
        )),
        Surah(22, "الحج", "Al-Hajj", "The Pilgrimage", 78, "مدنية", 332, 17),
        Surah(23, "المؤمنون", "Al-Mu'minun", "The Believers", 118, "مكية", 342, 18),
        Surah(24, "النور", "An-Nur", "The Light", 64, "مدنية", 350, 18, listOf(
            QuranVerse(35, "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ ۚ مَثَلُ نُورِهِ كَمِشْكَاةٍ فِيهَا مِصْبَاحٌ ۖ الْمِصْبَاحُ فِي زُجَاجَةٍ ۖ الزُّجَاجَةُ كَأَنَّهَا كَوْكَبٌ دُرِّيٌّ يُوقَدُ مِن شَجَرَةٍ مُّبَارَكَةٍ زَيْتُونَةٍ لَّا شَرْقِيَّةٍ وَلَا غَرْبِيَّةٍ يَكَادُ زَيْتُهَا يُضِيءُ وَلَوْ لَمْ تَمْسَسْهُ نَارٌ ۚ نُّورٌ عَلَىٰ نُورٍ ۗ يَهْدِي اللَّهُ لِنُورِهِ مَن يَشَاءُ", "Allah is the Light of the heavens and the earth. The example of His light is like a niche within which is a lamp...", "آية النور العظيمة: بيان نور الله وهدايته لقلوب المؤمنين.")
        )),
        Surah(25, "الفرقان", "Al-Furqan", "The Criterion", 77, "مكية", 359, 18),
        Surah(26, "الشعراء", "Ash-Shu'ara", "The Poets", 227, "مكية", 367, 19),
        Surah(27, "النمل", "An-Naml", "The Ant", 93, "مكية", 377, 19),
        Surah(28, "القصص", "Al-Qasas", "The Stories", 88, "مكية", 385, 20),
        Surah(29, "العنكبوت", "Al-'Ankabut", "The Spider", 69, "مكية", 396, 20),
        Surah(30, "الروم", "Ar-Rum", "The Romans", 60, "مكية", 404, 21),
        Surah(31, "لقمان", "Luqman", "Luqman", 34, "مكية", 411, 21),
        Surah(32, "السجدة", "As-Sajdah", "The Prostration", 30, "مكية", 415, 21),
        Surah(33, "الأحزاب", "Al-Ahzab", "The Combined Forces", 73, "مدنية", 418, 21, listOf(
            QuranVerse(56, "إِنَّ اللَّهَ وَمَلَائِكَتَهُ يُصَلُّونَ عَلَى النَّبِيِّ ۚ يَا أَيُّهَا الَّذِينَ آمَنُوا صَلُّوا عَلَيْهِ وَسَلِّمُوا تَسْلِيمًا", "Indeed, Allah confers blessing upon the Prophet, and His angels [ask Him to do so]. O you who have believed, ask [Allah to confer] blessing upon him and ask [Allah to grant him] peace.", "فضل الصلاة على النبي محمد ﷺ.")
        )),
        Surah(34, "سبأ", "Saba", "Sheba", 54, "مكية", 428, 22),
        Surah(35, "فاطر", "Fatir", "Originator", 45, "مكية", 434, 22),
        Surah(36, "يس", "Ya-Sin", "Ya-Sin", 83, "مكية", 440, 22, listOf(
            QuranVerse(1, "يس", "Ya-Sin.", "حروف مقطعة وقسم بكتاب الله العزيز."),
            QuranVerse(2, "وَالْقُرْآنِ الْحَكِيمِ", "By the wise Qur'an.", "قسم بالقرآن الحكيم المحكم المنزه عن الخطأ."),
            QuranVerse(58, "سَلَامٌ قَوْلًا مِّن رَّبٍّ رَّحِيمٍ", "'Peace,' a word from a Merciful Lord.", "تحية أهل الجنة من ربهم الرحيم الكريم.")
        )),
        Surah(37, "الصافات", "As-Saffat", "Those who set the Ranks", 182, "مكية", 446, 23),
        Surah(38, "ص", "Sad", "The Letter 'Saad'", 88, "مكية", 453, 23),
        Surah(39, "الزمر", "Az-Zumar", "The Troops", 75, "مكية", 458, 23, listOf(
            QuranVerse(53, "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا ۚ إِنَّهُ هُوَ الْغَفُورُ الرَّحِيمُ", "Say, 'O My servants who have transgressed against themselves [by sinning], do not despair of the mercy of Allah. Indeed, Allah forgives all sins...'", "أرجى آية في كتاب الله لفتح باب التوبة والأمل.")
        )),
        Surah(40, "غافر", "Ghafir", "The Forgiver", 85, "مكية", 467, 24),
        Surah(41, "فصلت", "Fussilat", "Explained in Detail", 54, "مكية", 477, 24),
        Surah(42, "الشورى", "Ash-Shuraa", "The Consultation", 53, "مكية", 483, 25),
        Surah(43, "الزخرف", "Az-Zukhruf", "The Ornaments of Gold", 89, "مكية", 489, 25),
        Surah(44, "الدخان", "Ad-Dukhan", "The Smoke", 59, "مكية", 496, 25),
        Surah(45, "الجاثية", "Al-Jathiyah", "The Crouching", 37, "مكية", 499, 25),
        Surah(46, "الأحقاف", "Al-Ahqaf", "The Wind-Curved Sandhills", 35, "مكية", 502, 26),
        Surah(47, "محمد", "Muhammad", "Muhammad", 38, "مدنية", 507, 26),
        Surah(48, "الفتح", "Al-Fath", "The Victory", 29, "مدنية", 511, 26, listOf(
            QuranVerse(1, "إِنَّا فَتَحْنَا لَكَ فَتْحًا مُّبِينًا", "Indeed, We have given you, [O Muhammad], a clear conquest", "بشارة الفتح والتمكين للنبي ﷺ وأصحابه.")
        )),
        Surah(49, "الحجرات", "Al-Hujurat", "The Rooms", 18, "مدنية", 515, 26),
        Surah(50, "ق", "Qaf", "The Letter 'Qaf'", 45, "مكية", 518, 26),
        Surah(51, "الذاريات", "Adh-Dhariyat", "The Winnowing Winds", 60, "مكية", 520, 26),
        Surah(52, "الطور", "At-Tur", "The Mount", 49, "مكية", 523, 27),
        Surah(53, "النجم", "An-Najm", "The Star", 62, "مكية", 526, 27),
        Surah(54, "القمر", "Al-Qamar", "The Moon", 55, "مكية", 528, 27),
        Surah(55, "الرحمن", "Ar-Rahman", "The Beneficent", 78, "مدنية", 531, 27, listOf(
            QuranVerse(1, "الرَّحْمَٰنُ", "The Entirely Merciful", "افتتاح باسم الرحمن لبيان عظيم نعمه."),
            QuranVerse(2, "عَلَّمَ الْقُرْآنَ", "Taught the Qur'an,", "أعظم نعمة أنزلها على عباده."),
            QuranVerse(13, "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ", "So which of the favors of your Lord would you deny?", "تذكير مستمر بنعم الله وآلائه على الثقلين الإنس والجن.")
        )),
        Surah(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", 96, "مكية", 534, 27, listOf(
            QuranVerse(1, "إِذَا وَقَعَتِ الْوَاقِعَةُ", "When the Occurrence occurs,", "سورة الواقعة وأهوال يوم القيامة."),
            QuranVerse(2, "لَيْسَ لِوَقْعَتِهَا كَاذِبَةٌ", "There is, at its occurrence, no denial.", "حق لا ريب فيه.")
        )),
        Surah(57, "الحديد", "Al-Hadid", "The Iron", 29, "مدنية", 537, 27),
        Surah(58, "المجادلة", "Al-Mujadila", "The Pleading Woman", 22, "مدنية", 542, 28),
        Surah(59, "الحشر", "Al-Hashr", "The Exile", 24, "مدنية", 545, 28, listOf(
            QuranVerse(22, "هُوَ اللَّهُ الَّذِي لَا إِلَٰهَ إِلَّا هُوَ ۖ عَالِمُ الْغَيْبِ وَالشَّهَادَةِ ۖ هُوَ الرَّحْمَٰنُ الرَّحِيمُ", "He is Allah, other than whom there is no deity, Knower of the unseen and the witnessed...", "خواتيم سورة الحشر وأسماء الله الحسنى.")
        )),
        Surah(60, "الممتحنة", "Al-Mumtahanah", "She that is to be examined", 13, "مدنية", 549, 28),
        Surah(61, "الصف", "As-Saf", "The Ranks", 14, "مدنية", 551, 28),
        Surah(62, "الجمعة", "Al-Jumu'ah", "The Congregation", 11, "مدنية", 553, 28, listOf(
            QuranVerse(9, "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا نُودِيَ لِلصَّلَاةِ مِن يَوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَىٰ ذِكْرِ اللَّهِ وَذَرُوا الْبَيْعَ", "O you who have believed, when [the adhan] is called for the prayer on the day of Jumu'ah [Friday], then proceed to the remembrance of Allah and leave trade...", "وجوب السعي لصلاة الجمعة.")
        )),
        Surah(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", 11, "مدنية", 554, 28),
        Surah(64, "التغابن", "At-Taghabun", "The Mutual Disillusion", 18, "مدنية", 556, 28),
        Surah(65, "الطلاق", "At-Talaq", "The Divorce", 12, "مدنية", 558, 28, listOf(
            QuranVerse(2, "وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ", "And whoever fears Allah - He will make for him a way out and will provide for him from where he does not expect.", "وعد الله للمتقين بالمخرج والرزق واليسر.")
        )),
        Surah(66, "التحريم", "At-Tahrim", "The Prohibition", 12, "مدنية", 560, 28),
        Surah(67, "الملك", "Al-Mulk", "The Sovereignty", 30, "مكية", 562, 29, listOf(
            QuranVerse(1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Blessed is He in whose hand is dominion, and He is over all things competent -", "سورة تبارك المنجية من عذاب القبر."),
            QuranVerse(2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -", "الغاية من الخلق هو الابتلاء والعمل الصالح.")
        )),
        Surah(68, "القلم", "Al-Qalam", "The Pen", 52, "مكية", 564, 29, listOf(
            QuranVerse(4, "وَإِنَّكَ لَعَلَىٰ خُلُقٍ عَظِيمٍ", "And indeed, you are of a great moral character.", "تزكية الله عز وجل لأخلاق نبيه المصطفى ﷺ.")
        )),
        Surah(69, "الحاقة", "Al-Haqqah", "The Inevitable", 52, "مكية", 566, 29),
        Surah(70, "المعارج", "Al-Ma'arij", "The Ascending Stairways", 44, "مكية", 568, 29),
        Surah(71, "نوح", "Nuh", "Noah", 28, "مكية", 570, 29, listOf(
            QuranVerse(10, "فَقُلْتُ اسْتَغْفِرُوا رَبَّكُمْ إِنَّهُ كَانَ غَفَّارًا يُرْسِلِ السَّمَاءَ عَلَيْكُم مِّدْرَارًا", "And said, 'Ask forgiveness of your Lord. Indeed, He is ever a Perpetual Forgiver. He will send [rain from] the sky upon you in [continuing] showers'", "فضل الاستغفار في نزول الغيث وسعة الرزق والذرية.")
        )),
        Surah(72, "الجن", "Al-Jinn", "The Jinn", 28, "مكية", 572, 29),
        Surah(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", 20, "مكية", 574, 29, listOf(
            QuranVerse(4, "أَوْ زِدْ عَلَيْهِ وَرَتِّلِ الْقُرْآنَ تَرْتِيلًا", "Or add to it, and recite the Qur'an with measured recitation.", "الأمر بترتيل القرآن وتدبره في قيام الليل.")
        )),
        Surah(74, "المدثر", "Al-Muddaththir", "The Cloaked One", 56, "مكية", 575, 29),
        Surah(75, "القيامة", "Al-Qiyamah", "The Resurrection", 40, "مكية", 577, 29),
        Surah(76, "الإنسان", "Al-Insan", "Man", 31, "مدنية", 578, 29),
        Surah(77, "المرسلات", "Al-Mursalat", "The Emissaries", 50, "مكية", 580, 29),
        Surah(78, "النبأ", "An-Naba", "The Tidings", 40, "مكية", 582, 30, listOf(
            QuranVerse(1, "عَمَّ يَتَسَاءَلُونَ", "About what are they asking one another?", "افتتاح الجزء الثلاثين (جزء عم)."),
            QuranVerse(2, "عَنِ النَّبَإِ الْعَظِيمِ", "About the great news -", "عن يوم القيامة والبعث بعد الموت.")
        )),
        Surah(79, "النازعات", "An-Nazi'at", "Those who drag forth", 46, "مكية", 583, 30),
        Surah(80, "عبس", "'Abasa", "He Frowned", 42, "مكية", 585, 30),
        Surah(81, "التكوير", "At-Takwir", "The Overthrowing", 29, "مكية", 586, 30),
        Surah(82, "الانفطار", "Al-Infitar", "The Cleaving", 19, "مكية", 587, 30),
        Surah(83, "المطففين", "Al-Mutaffifin", "The Defrauding", 36, "مكية", 587, 30),
        Surah(84, "الانشقاق", "Al-Inshiqaq", "The Splitting Open", 25, "مكية", 589, 30),
        Surah(85, "البروج", "Al-Buruj", "The Mansions of the Stars", 22, "مكية", 590, 30),
        Surah(86, "الطارق", "At-Tariq", "The Morning Star", 17, "مكية", 591, 30),
        Surah(87, "الأعلى", "Al-A'la", "The Most High", 19, "مكية", 591, 30, listOf(
            QuranVerse(1, "سَبِّحِ اسْمَ رَبِّكَ الْأَعْلَى", "Exalt the name of your Lord, the Most High,", "استحباب التسبيح بقول (سبحان ربي الأعلى).")
        )),
        Surah(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming", 26, "مكية", 592, 30),
        Surah(89, "الفجر", "Al-Fajr", "The Dawn", 30, "مكية", 593, 30, listOf(
            QuranVerse(1, "وَالْفَجْرِ وَلَيَالٍ عَشْرٍ", "By the dawn, And [by] the ten nights", "قسم بعشر ذي الحجة المباركة.")
        )),
        Surah(90, "البلد", "Al-Balad", "The City", 20, "مكية", 594, 30),
        Surah(91, "الشمس", "Ash-Shams", "The Sun", 15, "مكية", 595, 30, listOf(
            QuranVerse(9, "قَدْ أَفْلَحَ مَن زَكَّاهَا وَقَدْ خَابَ مَن دَسَّاهَا", "He has succeeded who purifies it, And he has failed who instills it [with corruption].", "الفلاح الحقيقي في تزكية النفس بطاعة الله.")
        )),
        Surah(92, "الليل", "Al-Layl", "The Night", 21, "مكية", 595, 30),
        Surah(93, "الضحى", "Ad-Duhaa", "The Morning Hours", 11, "مكية", 596, 30, listOf(
            QuranVerse(1, "وَالضُّحَىٰ وَاللَّيْلِ إِذَا سَجَىٰ", "By the morning brightness, And [by] the night when it covers with darkness,", "تثبيت وطمأنينة لقلب النبي ﷺ."),
            QuranVerse(5, "وَلَسَوْفَ يُعْطِيكَ رَبُّكَ فَتَرْضَىٰ", "And your Lord is going to give you, and you will be satisfied.", "بشارة العطاء العظيم والرضا.")
        )),
        Surah(94, "الشرح", "Ash-Sharh", "The Relief", 8, "مكية", 596, 30, listOf(
            QuranVerse(1, "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "Did We not expand for you, [O Muhammad], your breast?", "انشراح الصدر ونزول السكينة."),
            QuranVerse(5, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا إِنَّ مَعَ الْعُسْرِ يُسْرًا", "For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.", "تأكيد تفريج الكرب وأن العسر لا يغلب يسرين.")
        )),
        Surah(95, "التين", "At-Tin", "The Fig", 8, "مكية", 597, 30),
        Surah(96, "العلق", "Al-'Alaq", "The Clot", 19, "مكية", 597, 30, listOf(
            QuranVerse(1, "اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ", "Recite in the name of your Lord who created -", "أول ما نزل من القرآن الكريم على النبي ﷺ بغار حراء.")
        )),
        Surah(97, "القدر", "Al-Qadr", "The Power", 5, "مكية", 598, 30, listOf(
            QuranVerse(1, "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ", "Indeed, We sent the Qur'an down during the Night of Decree.", "فضل ليلة القدر المباركة في شهر رمضان."),
            QuranVerse(3, "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ", "The Night of Decree is better than a thousand months.", "ثواب العبادة فيها يوازي أكثر من 83 عاماً.")
        )),
        Surah(98, "البينة", "Al-Bayyinah", "The Clear Proof", 8, "مدنية", 598, 30),
        Surah(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", 8, "مدنية", 599, 30, listOf(
            QuranVerse(7, "فَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍ خَيْرًا يَرَهُ وَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍ شَرًّا يَرَهُ", "So whoever does an atom's weight of good will see it, And whoever does an atom's weight of evil will see it.", "عدالة الحساب ودقة ميزان الأعمال.")
        )),
        Surah(100, "العاديات", "Al-'Adiyat", "The Courser", 11, "مكية", 599, 30),
        Surah(101, "القارعة", "Al-Qari'ah", "The Calamity", 11, "مكية", 600, 30),
        Surah(102, "التكاثر", "At-Takathur", "The Rivalry in world increase", 8, "مكية", 600, 30),
        Surah(103, "العصر", "Al-'Asr", "The Declining Day", 3, "مكية", 601, 30, listOf(
            QuranVerse(1, "وَالْعَصْرِ", "By time,", "قسم بالزمان وعمر الإنسان."),
            QuranVerse(2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "Indeed, mankind is in loss,", "خسارة من ضيع عمره."),
            QuranVerse(3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.", "أركان النجاة الأربعة.")
        )),
        Surah(104, "الهمزة", "Al-Humazah", "The Traducer", 9, "مكية", 601, 30),
        Surah(105, "الفيل", "Al-Fil", "The Elephant", 5, "مكية", 601, 30),
        Surah(106, "قريش", "Quraysh", "Quraysh", 4, "مكية", 602, 30),
        Surah(107, "الماعون", "Al-Ma'un", "The Small Kindnesses", 7, "مكية", 602, 30),
        Surah(108, "الكوثر", "Al-Kawthar", "The Abundance", 3, "مكية", 602, 30, listOf(
            QuranVerse(1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "Indeed, We have granted you, [O Muhammad], al-Kawthar.", "نهر الكوثر والخير العظيم في الجنة."),
            QuranVerse(2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "So pray to your Lord and sacrifice [to Him alone].", "الأمر بإخلاص الصلاة والنحر لله تعالى.")
        )),
        Surah(109, "الكافرون", "Al-Kafirun", "The Disbelievers", 6, "مكية", 603, 30, listOf(
            QuranVerse(1, "قُلْ يَا أَيُّهَا الْكَافِرُونَ", "Say, 'O disbelievers,", "سورة البراءة من الشرك."),
            QuranVerse(6, "لَكُمْ دِينُكُمْ وَلِيَ دِينِ", "For you is your religion, and for me is my religion.'", "إخلاص التوحيد لله وحده.")
        )),
        Surah(110, "النصر", "An-Nasr", "The Divine Support", 3, "مدنية", 603, 30, listOf(
            QuranVerse(1, "إِذَا جَاءَ نَصْرُ اللَّهِ وَالْفَتْحُ", "When the victory of Allah has come and the conquest,", "بشارة نصر دين الله ودخول الناس أفواجاً."),
            QuranVerse(3, "فَسَبِّحْ بِحَمْدِ رَبِّكَ وَاسْتَغْفِرْهُ ۚ إِنَّهُ كَانَ تَوَّابًا", "Then exalt [Him] with praise of your Lord and ask forgiveness of Him. Indeed, He is ever Accepting of repentance.", "ختام الأعمال الصالحة بالتسبيح والاستغفار.")
        )),
        Surah(111, "المسد", "Al-Masad", "The Palm Fiber", 5, "مكية", 603, 30),
        Surah(112, "الإخلاص", "Al-Ikhlas", "The Sincerity", 4, "مكية", 604, 30, listOf(
            QuranVerse(1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, 'He is Allah, [who is] One,", "توحيد الله وتفرده بالألوهية، تعدل ثلث القرآن الكريم."),
            QuranVerse(2, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge.", "الذي تصمد إليه الخلائق في حوائجها ورغائبها."),
            QuranVerse(3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born,", "تنزيه الله عن الولد والوالد والشريك."),
            QuranVerse(4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent.'", "ليس كمثله شيء وهو السميع البصير.")
        )),
        Surah(113, "الفلق", "Al-Falaq", "The Daybreak", 5, "مكية", 604, 30, listOf(
            QuranVerse(1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, 'I seek refuge in the Lord of daybreak", "الاستعاذة برب الصبح وفالق الإصباح."),
            QuranVerse(2, "مِن شَرِّ مَا خَلَقَ", "From the evil of that which He created", "من شر جميع المخلوقات."),
            QuranVerse(3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles", "من شر ظلمة الليل إذا أقبلت."),
            QuranVerse(4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots", "من شر السحرة والحاسدين."),
            QuranVerse(5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies.'", "الحماية والتحصين من عين وحسد الحاسد.")
        )),
        Surah(114, "الناس", "An-Nas", "Mankind", 6, "مكية", 604, 30, listOf(
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

    fun getSurahsByJuz(juzNumber: Int): List<Surah> {
        return surahsList.filter { it.juzNumber == juzNumber }
    }
}
