package com.example.data.model

import androidx.compose.ui.graphics.vector.ImageVector

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
    val verses: List<QuranVerse> = emptyList()
)

data class QuranVerse(
    val ayahNumber: Int,
    val textArabic: String,
    val translation: String,
    val tafsirSummary: String = ""
)

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
    val audioUrl: String = ""
) {
    MAKKAH(
        titleArabic = "أذان الحرم المكي (الشيخ علي ملا)",
        descriptionArabic = "المقام الحجازي التراثي العريق والمهيب",
        location = "مكة المكرمة",
        audioUrl = "https://download.quranicaudio.com/adhan/makkah.mp3"
    ),
    MADINAH(
        titleArabic = "أذان المسجد النبوي (الشيخ عصام بخاري)",
        descriptionArabic = "أذان السكينة والخشوع والوقار المدني",
        location = "المدينة المنورة",
        audioUrl = "https://download.quranicaudio.com/adhan/madinah.mp3"
    ),
    AL_AQSA(
        titleArabic = "أذان المسجد الأقصى المبارك",
        descriptionArabic = "نبرة مقدسية شجية وصوت يفيض بالإيمان",
        location = "القدس الشريف",
        audioUrl = "https://download.quranicaudio.com/adhan/alaqsa.mp3"
    ),
    MISHARY(
        titleArabic = "أذان الشيخ مشاري راشد العفاسي",
        descriptionArabic = "صوت ندي خاشع ونبرة عذبة تأسر القلوب",
        location = "الكويت",
        audioUrl = "https://download.quranicaudio.com/adhan/mishari.mp3"
    ),
    ABDULBASIT(
        titleArabic = "أذان الشيخ عبد الباسط عبد الصمد",
        descriptionArabic = "صوت نقي وأداء تاريخي من أروع ما سُمع",
        location = "مصر",
        audioUrl = "https://download.quranicaudio.com/adhan/abdulbasit.mp3"
    ),
    QATAMI(
        titleArabic = "أذان الشيخ ناصر القطامي",
        descriptionArabic = "نبرة مؤثرة وروحانية تأخذك إلى آفاق الخشوع",
        location = "الرياض",
        audioUrl = "https://download.quranicaudio.com/adhan/qatami.mp3"
    ),
    TAKBEER_CHIME(
        titleArabic = "تكبيرات ونغمة سكينة هادئة",
        descriptionArabic = "تنبيه لطيف وهادئ مناسب للأماكن العامة والعمل",
        location = "نغمة ناعمة",
        audioUrl = ""
    );

    val arabicName: String get() = titleArabic
}

