package com.example.data.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.R

enum class PrayerType(
    val arabicName: String,
    val englishName: String,
    val description: String
) {
    FAJR("الفجر", "Fajr", "صلاة الصبح - ركعتان فرض"),
    SUNRISE("الشروق", "Sunrise", "وقت طلوع الشمس"),
    DHUHR("الظهر", "Dhuhr", "صلاة الظهيرة - 4 ركعات"),
    ASR("العصر", "Asr", "الصلاة الوسطى - 4 ركعات"),
    MAGHRIB("المغرب", "Maghrib", "صلاة الغروب - 3 ركعات"),
    ISHA("العشاء", "Isha", "صلاة العتمة - 4 ركعات")
}

data class PrayerTimeInfo(
    val type: PrayerType,
    val timeFormatted: String,
    val hours: Int,
    val minutes: Int,
    val timestampMillis: Long,
    val isPast: Boolean,
    val isNext: Boolean,
    val remainingMillis: Long
)

enum class PrayerStatus {
    NOT_RECORDED,
    ON_TIME,
    LATE,
    MISSED
}

data class DailyPrayerStatus(
    val dateString: String,
    val statusMap: Map<PrayerType, PrayerStatus> = emptyMap()
)

data class DhikrItem(
    val id: String,
    val arabicText: String,
    val translation: String,
    val targetCount: Int,
    val category: DhikrCategory,
    val virtue: String = "",
    val reference: String = ""
)

enum class DhikrCategory(val titleArabic: String, val iconName: String) {
    MORNING("أذكار الصباح", "wb_sunny"),
    EVENING("أذكار المساء", "nightlight_round"),
    AFTER_PRAYER("أذكار بعد الصلاة", "mosque"),
    SLEEP("أذكار النوم", "bedtime"),
    WAKEUP("أذكار الاستيقاظ", "alarm"),
    HOME("دخول وخروج المنزل", "home"),
    MOSQUE("أذكار المسجد", "temple_buddhist"),
    TRAVEL("أذكار السفر", "flight"),
    ISTIKHARA("دعاء الاستخارة", "stars"),
    DISTRESS("عند الكرب وتفريج الهم", "sentiment_dissatisfied"),
    SUSTENANCE("أدعية الرزق والبركة", "account_balance_wallet"),
    MISCELLANEOUS("أذكار متنوعة", "favorite");

    val arabicName: String get() = titleArabic
}

data class Surah(
    val number: Int,
    val arabicName: String,
    val englishName: String,
    val translationMeaning: String,
    val totalVerses: Int,
    val revelationType: String, // مكية أو مدنية
    val pageStart: Int = 1,
    val juzNumber: Int = 1,
    val verses: List<QuranVerse> = emptyList()
)

data class QuranVerse(
    val ayahNumber: Int,
    val textArabic: String,
    val translation: String = "",
    val tafsirSummary: String = ""
)

data class QuranReciter(
    val id: String,
    val nameArabic: String,
    val nameEnglish: String,
    val rewayaArabic: String = "حفص عن عاصم",
    val serverUrl: String,
    val description: String = "",
    val emojiAvatar: String = "🎙️"
)

enum class QuranRepeatMode(val titleArabic: String) {
    OFF("بدون تكرار"),
    ONE("تكرار السورة الحالية"),
    ALL("تشغيل السور متتالية")
}


data class DuaItem(
    val id: String,
    val title: String,
    val arabicText: String,
    val translation: String,
    val category: String,
    val source: String
)

data class IslamicOccasion(
    val titleArabic: String,
    val hijriDate: String,
    val gregorianDateApprox: String,
    val description: String,
    val isFastingRecommended: Boolean = false,
    val daysRemaining: Int = 0
)

enum class CalculationMethod(val titleArabic: String, val fajrAngle: Double, val ishaAngle: Double) {
    UMM_AL_QURA("أم القرى (مكة المكرمة)", 18.5, 90.0), // Isha 90 min after Maghrib
    MUSLIM_WORLD_LEAGUE("رابطة العالم الإسلامي", 18.0, 17.0),
    EGYPTIAN("الهيئة المصرية العامة للمساحة", 19.5, 17.5),
    ISNA("أمريكا الشمالية (ISNA)", 15.0, 15.0),
    KARACHI("جامعة العلوم الإسلامية بكراتشي", 18.0, 18.0);

    val arabicName: String get() = titleArabic
}

enum class JuristicMethod(val titleArabic: String) {
    STANDARD("الجمهور (شافعي، مالكي، حنبلي)"),
    HANAFI("الحنفي (ظل الشيء مثليه)");

    val arabicName: String get() = titleArabic
}

enum class MuezzinVoice(
    val titleArabic: String,
    val descriptionArabic: String,
    val location: String,
    val audioUrl: String = "",
    val rawResId: Int = R.raw.adhan_makkah
) {
    MAKKAH(
        titleArabic = "أذان الحرم المكي (الشيخ علي ملا)",
        descriptionArabic = "المقام الحجازي التراثي العريق والمهيب",
        location = "مكة المكرمة",
        audioUrl = "https://raw.githubusercontent.com/Kiwifu/adhan-mp3/main/Ali_Ibn_Ahmad_Mala_6_-_Al_Haram_Al_Maki_(%D8%B9%D9%84%D9%8A_%D8%A8%D9%86_%D8%A3%D8%AD%D9%85%D8%AF_%D9%85%D9%84%D8%A7_-_%D8%A7%D9%84%D8%AD%D8%B1%D9%85_%D8%A7%D9%84%D9%85%D9%83%D9%8A).mp3",
        rawResId = R.raw.adhan_makkah
    ),
    MADINAH(
        titleArabic = "أذان المسجد النبوي الشريف",
        descriptionArabic = "أذان السكينة والخشوع والوقار المدني",
        location = "المدينة المنورة",
        audioUrl = "https://raw.githubusercontent.com/Kiwifu/adhan-mp3/main/Adhan_Al_Haram_Al_Madani_-_Al_Madinah_1_(%D8%A3%D8%B0%D8%A7%D9%86_%D8%A7%D9%84%D8%AD%D8%B1%D9%85_%D8%A7%D9%84%D9%85%D8%AF%D9%86%D9%8A_-_%D8%A7%D9%84%D9%85%D8%AF%D9%8A%D9%86%D8%A9_%D8%A7%D9%84%D9%85%D9%86%D9%88%D8%B1%D8%A9).mp3",
        rawResId = R.raw.adhan_madinah
    ),
    AL_AQSA(
        titleArabic = "أذان المسجد الأقصى المبارك",
        descriptionArabic = "نبرة مقدسية شجية وصوت يفيض بالإيمان",
        location = "القدس الشريف",
        audioUrl = "https://raw.githubusercontent.com/Kiwifu/adhan-mp3/main/Adhan_Al_Aqsa_-_Jerusalem_(%D8%A3%D8%B0%D8%A7%D9%86_%D8%A7%D9%84%D9%85%D8%B3%D8%AC%D8%AF_%D8%A7%D9%84%D8%A3%D9%82%D8%B5%D9%89_-_%D8%A7%D9%84%D9%82%D8%AF%D8%B3).mp3",
        rawResId = R.raw.adhan_alaqsa
    ),
    MISHARY(
        titleArabic = "أذان الشيخ مشاري راشد العفاسي",
        descriptionArabic = "صوت ندي خاشع ونبرة عذبة تأسر القلوب",
        location = "الكويت",
        audioUrl = "https://raw.githubusercontent.com/Kiwifu/adhan-mp3/main/Mishary_Rashid_Alafasy_1_-_Kuwait_(%D9%85%D8%B4%D8%A7%D8%B1%D9%8A_%D8%B1%D8%A7%D8%B4%D8%AF_%D8%A7%D9%84%D8%B9%D9%81%D8%A7%D8%B3%D9%8A_-_%D8%A7%D9%84%D9%83%D9%88%D9%8A%D8%AA).mp3",
        rawResId = R.raw.adhan_mishary
    ),
    ABDULBASIT(
        titleArabic = "أذان الشيخ عبد الباسط عبد الصمد",
        descriptionArabic = "صوت نقي وأداء تاريخي من أروع ما سُمع",
        location = "مصر",
        audioUrl = "https://raw.githubusercontent.com/Kiwifu/adhan-mp3/main/Abdulbasit_Abdusamad_1_-_Egypt_(%D8%B9%D8%A8%D8%AF_%D8%A7%D9%84%D8%A8%D8%A7%D8%B3%D8%B7_%D8%B9%D8%A8%D8%AF_%D8%A7%D9%84%D8%B5%D9%85%D8%AF_-_%D9%85%D8%B5%D8%B1).mp3",
        rawResId = R.raw.adhan_abdulbasit
    ),
    QATAMI(
        titleArabic = "أذان الشيخ ناصر القطامي",
        descriptionArabic = "نبرة مؤثرة وروحانية تأخذك إلى آفاق الخشوع",
        location = "الرياض",
        audioUrl = "https://raw.githubusercontent.com/Kiwifu/adhan-mp3/main/Nasser_Al_Qatami_-_HQ_(%D9%86%D8%A7%D8%B5%D8%B1_%D8%A7%D9%84%D9%82%D8%B7%D8%A7%D9%85%D9%8A).mp3",
        rawResId = R.raw.adhan_qatami
    ),
    TAKBEERAT(
        titleArabic = "تكبيرات الأذان الشرعية (الله أكبر)",
        descriptionArabic = "تكبيرات بصوت خاشع ومؤثر خالٍ تماماً من أي موسيقى",
        location = "تسجيل شرعي",
        audioUrl = "",
        rawResId = R.raw.adhan_takbeerat
    ),
    RECITER_AYAH(
        titleArabic = "تلاوة قرآنية مباركة (الشيخ مشاري)",
        descriptionArabic = "قراءة آيات بينات من القرآن الكريم بصوت عذب خاشع",
        location = "تلاوة خاشعة",
        audioUrl = "https://server8.mp3quran.net/afs/001.mp3",
        rawResId = R.raw.adhan_takbeerat
    );

    val arabicName: String get() = titleArabic
}

enum class AzanAlertType(
    val titleArabic: String,
    val descriptionArabic: String
) {
    FULL_AZAN("أذان كامل", "رفع الأذان كاملاً بصوت المؤذن المختار"),
    TAKBEER_ONLY("تكبيرات فقط", "رفع تكبيرات: الله أكبر، الله أكبر بصوت خاشع"),
    RECITER_VOICE("صوت مقرئ (تلاوة)", "تلاوة آيات عطرة لتنبيه دخول الوقت دون أي موسيقى"),
    VIBRATE_ONLY("اهتزاز فقط", "تنبيه بالاهتزاز الخاشع دون صوت"),
    SILENT("صامت", "إشعار مرئي على الشاشة فقط")
}

data class PrayerAzanConfig(
    val prayerType: PrayerType,
    val isEnabled: Boolean = true,
    val muezzin: MuezzinVoice = MuezzinVoice.MAKKAH,
    val volume: Float = 0.85f,
    val alertType: AzanAlertType = AzanAlertType.FULL_AZAN,
    val preAlertMinutes: Int = 3
)

