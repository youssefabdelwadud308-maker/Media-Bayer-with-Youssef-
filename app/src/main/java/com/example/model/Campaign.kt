package com.example.model

enum class AdPlatform(val displayNameAr: String, val displayNameEn: String, val iconKey: String) {
    META("ميتا (فيسبوك وإنستغرام)", "Meta (Facebook & IG)", "meta"),
    TIKTOK("تيك توك للأعمال", "TikTok Ads", "tiktok"),
    GOOGLE("إعلانات جوجل ويوتيوب", "Google & YouTube Ads", "google"),
    SNAPCHAT("سناب شات", "Snapchat Ads", "snapchat"),
    LINKEDIN("لينكد إن للأعمال", "LinkedIn Ads", "linkedin"),
    X_TWITTER("إكس (تويتر سابقاً)", "X (Twitter)", "x"),
    SHOPIFY("شوبيفاي (تجارة إلكترونية)", "Shopify Store", "shopify"),
    SALLA("منصة سلة", "Salla E-Commerce", "salla")
}

enum class CampaignGoal(val titleAr: String, val titleEn: String) {
    CONVERSIONS("تحويلات ومبيعات مباشرة (Highest ROI)", "Conversions & Direct Sales"),
    LEADS("جمع بيانات عملاء محتملين (Leads)", "Lead Generation"),
    TRAFFIC("جلب زيارات للمتجر أو الموقع", "Website / Store Traffic"),
    MESSAGES("رسائل وتواصل مباشر (واتساب ومسنجر)", "Direct WhatsApp & Messenger"),
    APP_INSTALLS("تثبيت تطبيقات الهواتف", "App Installs"),
    BRAND_AWARENESS("زيادة الوعي بالعلامة والوصول", "Brand Awareness & Reach")
}

enum class CampaignStatus(val titleAr: String, val titleEn: String) {
    ACTIVE("نشطة وشغالة", "Active"),
    AI_OPTIMIZING("الذكاء الاصطناعي يُحسن الأداء", "AI Optimizing"),
    PAUSED("متوقفة مؤقتاً", "Paused"),
    BUDGET_EXHAUSTED("الميزانية نفدت (بحاجة تجديد)", "Budget Depleted"),
    COMPLETED("مكتملة", "Completed")
}

data class AbTestVariant(
    val id: String,
    val name: String, // "المتغير أ (نسخة الفيديو)" vs "المتغير ب (نسخة العرض الحصري)"
    val headline: String,
    val audienceTarget: String,
    val spentEgp: Double,
    val impressions: Long,
    val clicks: Long,
    val conversions: Int,
    val conversionRatePercent: Double,
    val isWinner: Boolean = false
)

data class CampaignAlert(
    val id: String,
    val campaignId: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: AlertType = AlertType.OPTIMIZATION,
    val isRead: Boolean = false
)

enum class AlertType {
    OPTIMIZATION,
    BUDGET_WARNING,
    HIGH_CONVERSION,
    AI_ACTION
}

data class Campaign(
    val id: String,
    val clientId: String,
    val clientName: String,
    val title: String,
    val platform: AdPlatform,
    val goal: CampaignGoal,
    val status: CampaignStatus,
    val totalBudgetEgp: Double,
    val dailyBudgetEgp: Double,
    val spentEgp: Double,
    val remainingDays: Int,
    val isAutoRenewalActive: Boolean = true,
    val isAiAutonomousEnabled: Boolean = true,
    val abTestEnabled: Boolean = true,
    val variants: List<AbTestVariant> = emptyList(),
    val impressions: Long = 0,
    val clicks: Long = 0,
    val conversions: Int = 0,
    val cpaEgp: Double = 0.0, // Cost per acquisition
    val roas: Double = 0.0, // Return on ad spend e.g. 4.8x
    val createdAt: Long = System.currentTimeMillis()
) {
    val ctrPercent: Double
        get() = if (impressions > 0) (clicks.toDouble() / impressions.toDouble()) * 100 else 0.0

    val remainingBudgetEgp: Double
        get() = (totalBudgetEgp - spentEgp).coerceAtLeast(0.0)
}
