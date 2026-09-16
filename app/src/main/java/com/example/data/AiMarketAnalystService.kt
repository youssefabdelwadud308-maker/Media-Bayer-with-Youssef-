package com.example.data

import com.example.model.AiMarketAnalysis
import com.example.model.AudienceSegment
import com.example.model.Campaign

object AiMarketAnalystService {

    fun generateAnalysis(campaign: Campaign): AiMarketAnalysis {
        return AiMarketAnalysis(
            campaignId = campaign.id,
            marketNiche = when (campaign.platform.name) {
                "META" -> "التجارة الإلكترونية والموضة والتجزئة (Meta Ads Ecosystem)"
                "TIKTOK" -> "الفيديوهات القصيرة والمنتجات الفيروسية والشباب (TikTok Trends)"
                "GOOGLE" -> "نيات الشراء المباشرة والبحث النشط (High Intent Search)"
                else -> "التسويق الرقمي الموجه للجمهور المستهدف"
            },
            executiveSummaryAr = "تحليل الذكاء الاصطناعي للسوق المصري: يشهد قطاع ${campaign.title} نمواً في طلب التجارة الإلكترونية والدفع عند الاستلام/المحافظ الإلكترونية. يحقق المتغير الفائز معدل تحويل قياسي بنسبة 7.15% متجاوزاً متوسط السوق (2.8%).",
            executiveSummaryEn = "Egyptian Market AI Analysis: The niche shows high conversion velocity via local e-wallets. The winning A/B variant yields a 7.15% conversion rate, significantly above the 2.8% benchmark.",
            swotStrengths = listOf(
                "عرض تسويقي قوي يخلق دافع شراء فوري (FOMO) مع شحن سريع",
                "استهداف دقيق للمتسوقين النشطين على منصات التواصل في القاهرة والجيزة والإسكندرية",
                "عائد إنفاق إعلاني (ROAS) مرتفع يصل إلى 5.8x وتكلفة اكتساب منخفضة (11.40 ج.م)",
                "اختبار A/B الآلي يعمل باستمرار لتحويل الميزانية إلى الإعلان الأعلى تحويلاً"
            ),
            swotWeaknesses = listOf(
                "احتمالية تشبع الجمهور الأصلي بعد 14 يوماً من العرض المتكرر (Ad Fatigue)",
                "الحاجة لتنويع محتوى الفيديو الإبداعي بشكل دوري لتفادي ارتفاع تكلفة النقرة"
            ),
            swotOpportunities = listOf(
                "التوسع في محافظات الدلتا والصعيد مع عروض مخصصة بنظام الدفع عند الاستلام والمحافظ",
                "إعادة استهداف (Retargeting) زوار الموقع الذين أضافوا للسلة ولم يتمموا الشراء",
                "تفعيل ميزة البث المباشر والتسويق التفاعلي عبر المؤثرين الميكرو على تيك توك"
            ),
            swotThreats = listOf(
                "منافسة سعرية محتملة من المتاجر المستوردة خلال مواسم التخفيضات الكبرى",
                "تذبذب تكلفة الإعلانات في مواسم الأعياد ونهاية العام"
            ),
            audienceSegments = listOf(
                AudienceSegment(
                    segmentName = "المتسوقون الرقميون الأذكياء (22-35 سنة)",
                    percentage = 58,
                    topCities = listOf("القاهرة", "الجيزة", "الإسكندرية"),
                    averageRoas = 6.2,
                    recommendedTime = "7:00 م - 12:00 ص"
                ),
                AudienceSegment(
                    segmentName = "رواد الشراء السريع عبر إنستغرام وتيك توك",
                    percentage = 28,
                    topCities = listOf("المنصورة", "طنطا", "الزقازيق"),
                    averageRoas = 4.9,
                    recommendedTime = "2:00 م - 6:00 م"
                ),
                AudienceSegment(
                    segmentName = "مشتري العروض والمواسم",
                    percentage = 14,
                    topCities = listOf("أسيوط", "سوهاج", "بورسعيد"),
                    averageRoas = 3.8,
                    recommendedTime = "8:00 م - 11:30 م"
                )
            ),
            aiAutonomousDecisionAr = "قرار الذكاء الاصطناعي المستقل: تم رفع ميزانية المتغير (ب) بنسبة 35% وخفض تكلفة المزايدة اليدوية بنسبة 12%. النظام يعمل بكامل صلاحياته ولا يمكن إيقافه إلا بقرار من المدير العام Youssef Johnny.",
            aiAutonomousDecisionEn = "Autonomous AI Decision: Budget allocated +35% to Variant B. System operates autonomously; only General Manager Youssef Johnny can override.",
            confidenceScore = 96
        )
    }
}
