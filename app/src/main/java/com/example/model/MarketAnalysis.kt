package com.example.model

data class SwotItem(
    val category: String, // Strengths, Weaknesses, Opportunities, Threats
    val points: List<String>
)

data class AudienceSegment(
    val segmentName: String,
    val percentage: Int,
    val topCities: List<String>,
    val averageRoas: Double,
    val recommendedTime: String
)

data class AiMarketAnalysis(
    val campaignId: String,
    val marketNiche: String,
    val executiveSummaryAr: String,
    val executiveSummaryEn: String,
    val swotStrengths: List<String>,
    val swotWeaknesses: List<String>,
    val swotOpportunities: List<String>,
    val swotThreats: List<String>,
    val audienceSegments: List<AudienceSegment>,
    val aiAutonomousDecisionAr: String,
    val aiAutonomousDecisionEn: String,
    val confidenceScore: Int = 94,
    val lastUpdated: Long = System.currentTimeMillis()
)
