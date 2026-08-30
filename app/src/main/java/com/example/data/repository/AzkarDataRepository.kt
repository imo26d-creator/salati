package com.example.data.repository

import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem

object AzkarDataRepository {

    val allAzkar: List<DhikrItem> = listOf(
        // Morning Azkar
        DhikrItem(
            id = "m1",
            arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
            translation = "We have entered a new morning and with it all dominion is Allah's. Praise is to Allah. None has the right to be worshipped except Allah alone, without partner.",
            targetCount = 1,
            category = DhikrCategory.MORNING,
            virtue = "من قالها حين يصبح وحين يمسي كفته من كل شيء.",
            reference = "صحيح مسلم"
        ),
        DhikrItem(
            id = "m2",
            arabicText = "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ.",
            translation = "O Allah, by You we enter the morning and by You we enter the evening, by You we live and by You we die, and to You is the Resurrection.",
            targetCount = 1,
            category = DhikrCategory.MORNING,
            reference = "سنن الترمذي"
        ),
        DhikrItem(
            id = "m3",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ.",
            translation = "O Allah, You are my Lord, none has the right to be worshipped but You. You created me and I am Your servant...",
            targetCount = 1,
            category = DhikrCategory.MORNING,
            virtue = "سيد الاستغفار: من قالها موقناً بها فمات من يومه أو ليلته دخل الجنة.",
            reference = "صحيح البخاري"
        ),
        DhikrItem(
            id = "m4",
            arabicText = "بِسْمِ اللَّهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
            translation = "In the name of Allah with whose Name nothing can harm in the earth or in the heaven, and He is the All-Hearing, All-Knowing.",
            targetCount = 3,
            category = DhikrCategory.MORNING,
            virtue = "لم يضره شيء.",
            reference = "سنن أبي داود والترمذي"
        ),
        DhikrItem(
            id = "m5",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ: عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ.",
            translation = "Glory is to Allah and praise is to Him, by the number of His creation and His pleasure and weight of His Throne.",
            targetCount = 3,
            category = DhikrCategory.MORNING,
            virtue = "تعدل ساعات طويلة من الذكر والتسبيح.",
            reference = "صحيح مسلم"
        ),
        DhikrItem(
            id = "m6",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ.",
            translation = "Glory be to Allah and His is the praise.",
            targetCount = 100,
            category = DhikrCategory.MORNING,
            virtue = "حُطّت خطاياه وإن كانت مثل زبد البحر، ولم يأتِ أحد يوم القيامة بأفضل مما جاء به.",
            reference = "صحيح البخاري ومسلم"
        ),

        // Evening Azkar
        DhikrItem(
            id = "e1",
            arabicText = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
            translation = "We have entered the evening and with it all dominion is Allah's. Praise is to Allah.",
            targetCount = 1,
            category = DhikrCategory.EVENING,
            reference = "صحيح مسلم"
        ),
        DhikrItem(
            id = "e2",
            arabicText = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ.",
            translation = "I seek refuge in the Perfect Words of Allah from the evil of what He has created.",
            targetCount = 3,
            category = DhikrCategory.EVENING,
            virtue = "لم يضره شيء في تلك الليلة.",
            reference = "صحيح مسلم"
        ),
        DhikrItem(
            id = "e3",
            arabicText = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لاَ إِلَـهَ إِلاَّ أَنْتَ.",
            translation = "O Allah, grant my body health, grant my hearing health, grant my sight health. None has the right to be worshipped except You.",
            targetCount = 3,
            category = DhikrCategory.EVENING,
            reference = "سنن أبي داود"
        ),

        // After Prayer Azkar
        DhikrItem(
            id = "p1",
            arabicText = "أَسْتَغْفِرُ اللهَ (3 مرات)، اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ، تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ.",
            translation = "I ask Allah for forgiveness (3 times). O Allah, You are Peace and from You comes peace. Blessed are You, O Owner of majesty and honor.",
            targetCount = 1,
            category = DhikrCategory.AFTER_PRAYER,
            reference = "صحيح مسلم"
        ),
        DhikrItem(
            id = "p2",
            arabicText = "سُبْحَانَ اللهِ (33)، الْحَمْدُ لِلَّهِ (33)، اللهُ أَكْبَرُ (33)، ثُمَّ تَمَامَ الْمِائَةِ: لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
            translation = "Subhan Allah (33), Alhamdulillah (33), Allahu Akbar (33), and complete the hundred with Tawheed.",
            targetCount = 33,
            category = DhikrCategory.AFTER_PRAYER,
            virtue = "غُفرت خطاياه وإن كانت مثل زبد البحر.",
            reference = "صحيح مسلم"
        ),

        // Sleep Azkar
        DhikrItem(
            id = "s1",
            arabicText = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي، وَبِكَ أَرْفَعُهُ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ.",
            translation = "In Your Name my Lord, I lay down, and in Your Name I rise...",
            targetCount = 1,
            category = DhikrCategory.SLEEP,
            reference = "صحيح البخاري ومسلم"
        ),
        DhikrItem(
            id = "s2",
            arabicText = "اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ.",
            translation = "O Allah, protect me from Your punishment on the Day You resurrect Your servants.",
            targetCount = 3,
            category = DhikrCategory.SLEEP,
            reference = "سنن أبي داود"
        ),

        // Wake up Azkar
        DhikrItem(
            id = "w1",
            arabicText = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ.",
            translation = "All praise is to Allah, who gave us life after having given us death, and unto Him is the resurrection.",
            targetCount = 1,
            category = DhikrCategory.WAKEUP,
            reference = "صحيح البخاري"
        ),

        // Home Azkar
        DhikrItem(
            id = "h1",
            arabicText = "بِسْمِ اللهِ، تَوَكَّلْتُ عَلَى اللهِ، وَلاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللهِ.",
            translation = "In the name of Allah, I place my trust in Allah, and there is no power nor strength except by Allah.",
            targetCount = 1,
            category = DhikrCategory.HOME,
            virtue = "يُقال له: هُديت وكُفيت ووُقيت، وتنحى عنه الشيطان.",
            reference = "سنن الترمذي"
        ),

        // Mosque Azkar
        DhikrItem(
            id = "mq1",
            arabicText = "اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ (عند الدخول).",
            translation = "O Allah, open the gates of Your mercy for me.",
            targetCount = 1,
            category = DhikrCategory.MOSQUE,
            reference = "صحيح مسلم"
        ),

        // Travel Azkar
        DhikrItem(
            id = "tr1",
            arabicText = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ.",
            translation = "Glory unto Him who has subjected this to us, and we were not capable of handling it, and to our Lord we return.",
            targetCount = 1,
            category = DhikrCategory.TRAVEL,
            reference = "سورة الزخرف / صحيح مسلم"
        ),

        // Istikhara
        DhikrItem(
            id = "ist1",
            arabicText = "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ، وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ، وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ، فَإِنَّكَ تَقْدِرُ وَلاَ أَقْدِرُ، وَتَعْلَمُ وَلاَ أَعْلَمُ، وَأَنْتَ عَلاَّمُ الْغُيُوبِ...",
            translation = "O Allah, I consult You through Your Knowledge, and seek strength through Your Power...",
            targetCount = 1,
            category = DhikrCategory.ISTIKHARA,
            virtue = "دعاء الاستخارة عند العزم على أمر للطمأنينة والتوفيق.",
            reference = "صحيح البخاري"
        ),

        // Distress & Relief
        DhikrItem(
            id = "dis1",
            arabicText = "لاَ إِلَهَ إِلاَّ أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ.",
            translation = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
            targetCount = 7,
            category = DhikrCategory.DISTRESS,
            virtue = "دعاء ذي النون: ما دعا به مكروب إلا فرج الله عنه.",
            reference = "جامع الترمذي"
        ),
        DhikrItem(
            id = "dis2",
            arabicText = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ وَلاَ تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ.",
            translation = "O Ever-Living, O Sustainer, in Your mercy I seek relief...",
            targetCount = 3,
            category = DhikrCategory.DISTRESS,
            reference = "سنن الترمذي"
        ),

        // Sustenance & Blessing
        DhikrItem(
            id = "sus1",
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْماً نَافِعاً، وَرِزْقاً طَيِّباً، وَعَمَلاً مُتَقَبَّلاً.",
            translation = "O Allah, I ask You for beneficial knowledge, good provision, and acceptable deeds.",
            targetCount = 1,
            category = DhikrCategory.SUSTENANCE,
            reference = "سنن ابن ماجه"
        ),
        DhikrItem(
            id = "sus2",
            arabicText = "اللَّهُمَّ اكْفِنِي بِحَلاَلِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ.",
            translation = "O Allah, suffice me with what is lawful against what is prohibited, and enrich me by Your grace from all besides You.",
            targetCount = 3,
            category = DhikrCategory.SUSTENANCE,
            virtue = "لقضاء الدين وجلب الرزق الحلال والبركة.",
            reference = "جامع الترمذي"
        )
    )

    fun getAzkarByCategory(category: DhikrCategory): List<DhikrItem> {
        return allAzkar.filter { it.category == category }
    }
}
