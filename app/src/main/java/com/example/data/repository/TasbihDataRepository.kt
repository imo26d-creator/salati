package com.example.data.repository

data class PresetDhikr(
    val id: String,
    val arabicText: String,
    val transliteration: String = "",
    val translation: String = "",
    val defaultTarget: Int = 33,
    val category: TasbihCategory = TasbihCategory.TAHMID_TASBIH,
    val virtueArabic: String = "",
    val hadithReference: String = "",
    val isCustom: Boolean = false
)

enum class TasbihCategory(val titleArabic: String, val iconEmoji: String) {
    ALL("الكل", "✨"),
    TAHMID_TASBIH("التسبيح والتحميد", "📿"),
    ISTIGHFAR("الاستغفار والتوبة", "🤍"),
    SALAWAT("الصلاة على النبي ﷺ", "🌸"),
    HAWQALA_TAWAKKUL("الحوقلة والتوكل", "🛡️"),
    QURANIC_DUAS("أدعية القرآن والسنة", "📖"),
    CUSTOM("أذكاري المخصصة", "✏️")
}

object TasbihDataRepository {

    val presetDhikrList: List<PresetDhikr> = listOf(
        PresetDhikr(
            id = "tasbih_subhanallah",
            arabicText = "سُبْحَانَ اللَّهِ",
            transliteration = "Subhan Allah",
            translation = "Glory be to Allah",
            defaultTarget = 33,
            category = TasbihCategory.TAHMID_TASBIH,
            virtueArabic = "تُكتب له ألف حسنة أو تُحط عنه ألف خطيئة، وتغرس له نخلة في الجنة.",
            hadithReference = "صحيح مسلم"
        ),
        PresetDhikr(
            id = "tasbih_alhamdulillah",
            arabicText = "الْحَمْدُ لِلَّهِ",
            transliteration = "Alhamdulillah",
            translation = "All praise is due to Allah",
            defaultTarget = 33,
            category = TasbihCategory.TAHMID_TASBIH,
            virtueArabic = "«والحمد لله تملأ الميزان»، وهي أحب الكلام إلى الله وأفضل الدعاء.",
            hadithReference = "صحيح مسلم والترمذي"
        ),
        PresetDhikr(
            id = "tasbih_allahuakbar",
            arabicText = "اللَّهُ أَكْبَرُ",
            transliteration = "Allahu Akbar",
            translation = "Allah is the Greatest",
            defaultTarget = 33,
            category = TasbihCategory.TAHMID_TASBIH,
            virtueArabic = "تعظيم لشأن الخالق سبحانه، وتكفير للذنوب والخطايا ورفعة في الدرجات.",
            hadithReference = "سنن الترمذي"
        ),
        PresetDhikr(
            id = "tasbih_subhan_bihamdih_azim",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            transliteration = "Subhan Allahi wa bihamdihi, Subhan Allahil Azeem",
            translation = "Glory and praise be to Allah, Glory be to Allah the Almighty",
            defaultTarget = 100,
            category = TasbihCategory.TAHMID_TASBIH,
            virtueArabic = "«كلمتان خفيفتان على اللسان، ثقيلتان في الميزان، حبيبتان إلى الرحمن».",
            hadithReference = "متفق عليه (البخاري ومسلم)"
        ),
        PresetDhikr(
            id = "tasbih_tahlil_complete",
            arabicText = "لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "La ilaha illallah wahdahu la shareeka lah...",
            translation = "None has the right to be worshipped except Allah alone...",
            defaultTarget = 100,
            category = TasbihCategory.TAHMID_TASBIH,
            virtueArabic = "كانت له عدل عشر رقاب، وكتبت له مئة حسنة، ومحيت عنه مئة سيئة، وكانت له حرزاً من الشيطان.",
            hadithReference = "صحيح البخاري ومسلم"
        ),
        PresetDhikr(
            id = "tasbih_istighfar_short",
            arabicText = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ",
            transliteration = "Astaghfirullahal Azeem wa atoobu ilayh",
            translation = "I seek forgiveness from Allah the Almighty and repent to Him",
            defaultTarget = 100,
            category = TasbihCategory.ISTIGHFAR,
            virtueArabic = "«من لزم الاستغفار جعل الله له من كل هم فرجاً، ومن كل ضيق مخرجاً، ورزقه من حيث لا يحتسب».",
            hadithReference = "سنن أبي داود وأحمد"
        ),
        PresetDhikr(
            id = "tasbih_sayyid_istighfar",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ",
            transliteration = "Sayyid al-Istighfar",
            translation = "The Master of Forgiveness Supplication",
            defaultTarget = 3,
            category = TasbihCategory.ISTIGHFAR,
            virtueArabic = "«من قالها موقناً بها حين يمسي فمات من ليلته دخل الجنة، ومن قالها موقناً بها حين يصبح فمات من يومه دخل الجنة».",
            hadithReference = "صحيح البخاري"
        ),
        PresetDhikr(
            id = "tasbih_salawat_ibrahimiyyah",
            arabicText = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            transliteration = "As-Salat Al-Ibrahimiyyah",
            translation = "Sending blessings and peace upon Prophet Muhammad ﷺ",
            defaultTarget = 100,
            category = TasbihCategory.SALAWAT,
            virtueArabic = "«من صلى عليّ صلاة واحدة صلى الله عليه بها عشراً، وحطت عنه عشر خطيئات، ورفعت له عشر درجات».",
            hadithReference = "سنن النسائي وأحمد"
        ),
        PresetDhikr(
            id = "tasbih_salawat_short",
            arabicText = "اللَّهُمَّ صَلِّ وَسَلِّمْ وَبَارِكْ عَلَى نَبِيِّنَا مُحَمَّدٍ",
            transliteration = "Allahumma salli wa sallim ala Nabiyyina Muhammad",
            translation = "O Allah, send blessings and peace upon our Prophet Muhammad",
            defaultTarget = 100,
            category = TasbihCategory.SALAWAT,
            virtueArabic = "سبب لكفاية الهموم ومغفرة الذنوب ونيل شفاعة النبي ﷺ يوم القيامة.",
            hadithReference = "سنن الترمذي"
        ),
        PresetDhikr(
            id = "tasbih_hawqala",
            arabicText = "لاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "La hawla wa la quwwata illa billahil Aliyyil Azeem",
            translation = "There is no power and no strength except with Allah the Most High",
            defaultTarget = 100,
            category = TasbihCategory.HAWQALA_TAWAKKUL,
            virtueArabic = "«كنز من كنوز الجنة»، وباب عظيم من أبواب الفرج وتيسير الأمور ودفع البلاء.",
            hadithReference = "متفق عليه"
        ),
        PresetDhikr(
            id = "tasbih_baqiyat_salihat",
            arabicText = "سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلاَ إِلَهَ إِلاَّ اللَّهُ، وَاللَّهُ أَكْبَرُ",
            transliteration = "Subhan Allah, walhamdulillah, wa la ilaha illallah, wallahu Akbar",
            translation = "The Everlasting Good Deeds (Al-Baqiyat As-Salihat)",
            defaultTarget = 100,
            category = TasbihCategory.TAHMID_TASBIH,
            virtueArabic = "«هن الباقيات الصالحات، وأحب الكلام إلى الله تعالى، وغراس الجنة».",
            hadithReference = "صحيح مسلم والترمذي"
        ),
        PresetDhikr(
            id = "tasbih_dua_yunus",
            arabicText = "لاَ إِلَهَ إِلاَّ أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
            transliteration = "La ilaha illa Anta subhanaka inni kuntu minaz-zalimeen",
            translation = "None has the right to be worshipped except You; Glory be to You, I was of the wrongdoers",
            defaultTarget = 40,
            category = TasbihCategory.QURANIC_DUAS,
            virtueArabic = "«دعوة ذي النون إذ دعا وهو في بطن الحوت: لم يدعُ بها رجل مسلم في شيء قط إلا استجاب الله له».",
            hadithReference = "سنن الترمذي وصحيح الجامع"
        ),
        PresetDhikr(
            id = "tasbih_hasbunallah",
            arabicText = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            transliteration = "Hasbunallahu wa ni'mal wakeel",
            translation = "Allah is sufficient for us, and He is the best Disposer of affairs",
            defaultTarget = 100,
            category = TasbihCategory.HAWQALA_TAWAKKUL,
            virtueArabic = "قالها إبراهيم عليه السلام حين أُلقي في النار، وقالها النبي ﷺ وأصحابه يوم أُحد، فكانت النصرة والسكينة.",
            hadithReference = "صحيح البخاري"
        ),
        PresetDhikr(
            id = "tasbih_ya_hayyu_ya_qayyum",
            arabicText = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ وَلاَ تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ",
            transliteration = "Ya Hayyu Ya Qayyum bi rahmatika astagheeth...",
            translation = "O Ever-Living, O Sustainer, by Your mercy I seek assistance...",
            defaultTarget = 33,
            category = TasbihCategory.QURANIC_DUAS,
            virtueArabic = "الاسم الأعظم ودعاء الكرب وطلب الهداية والصلاح في كل أمر.",
            hadithReference = "سنن الترمذي والحاكم"
        ),
        PresetDhikr(
            id = "tasbih_free_flow",
            arabicText = "ذكر حر ومطلق (بدون حد)",
            transliteration = "Free Dhikr Counter",
            translation = "Continuous Open Tasbih without limits",
            defaultTarget = 1000,
            category = TasbihCategory.TAHMID_TASBIH,
            virtueArabic = "«والذاكرين الله كثيراً والذاكرات أعد الله لهم مغفرة وأجراً عظيماً».",
            hadithReference = "سورة الأحزاب: ٣٥"
        )
    )
}
