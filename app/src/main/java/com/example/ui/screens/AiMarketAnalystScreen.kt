package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun AiMarketAnalystScreen(
    currentUser: User,
    analysis: AiMarketAnalysis?,
    selectedCampaign: Campaign?,
    isGlobalAiActive: Boolean,
    currentLanguage: String,
    onToggleGlobalAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = currentLanguage == "ar"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI Autonomous Status Header
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = ElectricCyan.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Box(modifier = Modifier.padding(8.dp)) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(24.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isAr) "محلل السوق والميديا باير الذكي" else "AI Market Analyst & Autonomous Engine",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isGlobalAiActive) (if (isAr) "يعمل بكامل الصلاحيات الآلية" else "Autonomous Active") else (if (isAr) "متوقف بقرار إداري" else "Halted by GM"),
                                fontSize = 11.sp,
                                color = if (isGlobalAiActive) EmeraldGreen else CrimsonRed
                            )
                        }
                    }

                    // Confidence badge
                    Surface(
                        color = AmberGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${analysis?.confidenceScore ?: 95}% AI Confidence",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Authority Rule Banner (Only General Manager can stop AI)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentUser.role == UserRole.GENERAL_MANAGER) AmberGold.copy(alpha = 0.15f) else NavyCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (currentUser.role == UserRole.GENERAL_MANAGER) Icons.Default.VerifiedUser else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (currentUser.role == UserRole.GENERAL_MANAGER) AmberGold else ElectricCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAr) "قانون الصلاحيات الصارم للنظام:" else "System Authority Rule:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentUser.role == UserRole.GENERAL_MANAGER) AmberGold else Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isAr)
                                "الذكاء الاصطناعي يمتلك كل الصلاحيات في تحليل السوق وإدارة الحملات ونقاط القوة والضعف، والوحيد المخول بإيقافه أو استئنافه هو المدير العام (Youssef Johnny 👑)."
                            else
                                "The AI holds full autonomous authority to run and optimize campaigns. Only General Manager Youssef Johnny is authorized to override or halt it.",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )

                        // If user is General Manager, show the actual control switch!
                        if (currentUser.role == UserRole.GENERAL_MANAGER) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onToggleGlobalAi,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isGlobalAiActive) CrimsonRed else EmeraldGreen
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("gm_override_ai_btn")
                            ) {
                                Icon(
                                    imageVector = if (isGlobalAiActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isAr)
                                        (if (isGlobalAiActive) "إيقاف الذكاء الاصطناعي اضطرارياً (صلاحية Youssef Johnny)" else "إعادة تشغيل الذكاء الاصطناعي الذاتي")
                                    else
                                        (if (isGlobalAiActive) "Halt AI Autonomous Engine (GM Youssef Johnny)" else "Resume AI Autonomous Engine"),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Executive Summary
        if (analysis != null) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isAr) "الملخص التنفيذي وتحليل السوق المصري:" else "Egyptian Market AI Executive Summary:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isAr) analysis.executiveSummaryAr else analysis.executiveSummaryEn,
                        fontSize = 12.sp,
                        color = TextPrimaryDark,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = EmeraldGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isAr) "⚡ " + analysis.aiAutonomousDecisionAr else "⚡ " + analysis.aiAutonomousDecisionEn,
                            fontSize = 11.sp,
                            color = EmeraldGreen,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // SWOT Analysis Grid (Strengths, Weaknesses, Opportunities, Threats)
            Text(
                text = if (isAr) "تحليل SWOT التفصيلي للحملة والمنافسين:" else "Campaign & Competitor SWOT Matrix:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Strengths (نقاط القوة)
            SwotCategoryCard(
                title = if (isAr) "نقاط القوة (Strengths)" else "Strengths",
                points = analysis.swotStrengths,
                headerColor = EmeraldGreen,
                icon = Icons.Default.ThumbUp
            )

            // Weaknesses (نقاط الضعف)
            SwotCategoryCard(
                title = if (isAr) "نقاط الضعف والتحديات (Weaknesses)" else "Weaknesses",
                points = analysis.swotWeaknesses,
                headerColor = AmberDark,
                icon = Icons.Default.Warning
            )

            // Opportunities (الفرص المتاحة)
            SwotCategoryCard(
                title = if (isAr) "الفرص التسويقية للتوسع (Opportunities)" else "Opportunities",
                points = analysis.swotOpportunities,
                headerColor = ElectricCyan,
                icon = Icons.Default.TrendingUp
            )

            // Threats (التهديدات والمنافسة)
            SwotCategoryCard(
                title = if (isAr) "التهديدات ومخاطر السوق (Threats)" else "Threats",
                points = analysis.swotThreats,
                headerColor = CrimsonRed,
                icon = Icons.Default.Security
            )

            // Target Audience Segments Breakdown
            Text(
                text = if (isAr) "شرائح الجمهور وتوزيع المحافظات الأعلى طلباً:" else "Target Audience & High Demand Egyptian Cities:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                analysis.audienceSegments.forEach { seg ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = seg.segmentName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${seg.percentage}%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (isAr)
                                    "المحافظات الأكثر تفاعلاً: ${seg.topCities.joinToString("، ")} | العائد المتوقع: ${seg.averageRoas}x | أفضل وقت: ${seg.recommendedTime}"
                                else
                                    "Top Cities: ${seg.topCities.joinToString(", ")} | Target ROAS: ${seg.averageRoas}x | Peak Time: ${seg.recommendedTime}",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwotCategoryCard(
    title: String,
    points: List<String>,
    headerColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = headerColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = headerColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                points.forEach { pt ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "• ", color = headerColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = pt,
                            fontSize = 12.sp,
                            color = TextPrimaryDark,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}
