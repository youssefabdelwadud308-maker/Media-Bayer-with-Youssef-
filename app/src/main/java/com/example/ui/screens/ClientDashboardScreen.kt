package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MediaBuyerRepository
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun ClientDashboardScreen(
    currentUser: User,
    campaigns: List<Campaign>,
    alerts: List<CampaignAlert>,
    currentLanguage: String,
    onLaunchCampaignClick: () -> Unit,
    onNavigateToAiMarket: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToReports: () -> Unit,
    onSelectCampaign: (Campaign) -> Unit,
    onToggleCampaign: (String) -> Unit = {},
    onOptimizeCampaign: (String) -> Unit = {},
    onAddBudget: (String, Double) -> Unit = { _, _ -> },
    onDeleteCampaign: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isAr = currentLanguage == "ar"

    val totalBudget = campaigns.sumOf { it.totalBudgetEgp }
    val totalSpent = campaigns.sumOf { it.spentEgp }
    val totalConversions = campaigns.sumOf { it.conversions }
    val totalImpressions = campaigns.sumOf { it.impressions }
    val totalClicks = campaigns.sumOf { it.clicks }
    val avgRoas = if (campaigns.isNotEmpty()) campaigns.map { it.roas }.average() else 0.0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.banner_mediabuyer_1789510996607),
                        contentDescription = "Media Buyer AI Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    )

                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isAr) "لوحة تحكم الميديا باير الذكي" else "Smart Media Buyer Dashboard",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Surface(
                                color = EmeraldGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(EmeraldGreen, RoundedCornerShape(4.dp))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isAr) "الذكاء الاصطناعي نشط" else "AI Active",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGreen
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (isAr)
                                "مرحباً ${currentUser.fullName}، نظام الميديا باير يحلل أداء حملاتك على مدار الساعة ويحول الميزانيات للمتغيرات ذات أعلى معدل تحويل."
                            else
                                "Welcome ${currentUser.fullName}, autonomous AI continuously reallocates budget to top converting A/B variants.",
                            fontSize = 12.sp,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // Share Portal with Clients Banner
        item {
            val context = LocalContext.current
            val clipboardManager = LocalClipboardManager.current
            val clientUrl = MediaBuyerRepository.CLIENT_PORTAL_URL

            Card(
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ElectricCyan.copy(alpha = 0.5f))),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("client_portal_banner_card")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(12.dp).fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "رابط المنصة لمشاركته مع العملاء:" else "Client Portal Public URL:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = clientUrl,
                            fontSize = 11.sp,
                            color = ElectricCyan,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(clientUrl))
                            Toast.makeText(
                                context,
                                if (isAr) "تم نسخ رابط الموقع للعملاء بنجاح!" else "Platform link copied!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAr) "نسخ الرابط" else "Copy", color = NavyDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Live Alerts Ticker
        if (alerts.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val topAlert = alerts.first()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            imageVector = when (topAlert.type) {
                                AlertType.AI_ACTION -> Icons.Default.AutoAwesome
                                AlertType.BUDGET_WARNING -> Icons.Default.NotificationsActive
                                AlertType.HIGH_CONVERSION -> Icons.Default.TrendingUp
                                else -> Icons.Default.Info
                            },
                            contentDescription = "Alert",
                            tint = if (topAlert.type == AlertType.BUDGET_WARNING) AmberGold else ElectricCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = topAlert.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = topAlert.message,
                                fontSize = 11.sp,
                                color = TextSecondaryDark,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }

        // Key Financial & Marketing KPIs
        item {
            Text(
                text = if (isAr) "مؤشرات الأداء اللحظية (بالجنيه المصري EGP):" else "Real-Time Performance Indicators (EGP):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan
            )

            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Total Budget
                    KpiCard(
                        title = if (isAr) "إجمالي الميزانية" else "Total Budget",
                        value = String.format("%.0f ج.م", totalBudget),
                        subtitle = if (isAr) "إنفاق: %.0f ج.م".format(totalSpent) else "Spent: %.0f EGP".format(totalSpent),
                        icon = Icons.Default.AccountBalanceWallet,
                        accentColor = ElectricCyan,
                        modifier = Modifier.weight(1f)
                    )

                    // ROAS
                    KpiCard(
                        title = if (isAr) "عائد الإنفاق ROAS" else "Average ROAS",
                        value = String.format("%.1fx", avgRoas),
                        subtitle = if (isAr) "أعلى من متوسط السوق" else "Above Benchmark",
                        icon = Icons.Default.TrendingUp,
                        accentColor = EmeraldGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Total Conversions
                    KpiCard(
                        title = if (isAr) "التحويلات الناجحة" else "Conversions",
                        value = "$totalConversions",
                        subtitle = if (isAr) "مبيعات وتواصل مؤكد" else "Verified Leads/Sales",
                        icon = Icons.Default.CheckCircle,
                        accentColor = AmberGold,
                        modifier = Modifier.weight(1f)
                    )

                    // Impressions & Clicks
                    KpiCard(
                        title = if (isAr) "الوصول والنقرات" else "Reach & Clicks",
                        value = "${totalImpressions / 1000}k",
                        subtitle = if (isAr) "نقرات: $totalClicks" else "Clicks: $totalClicks",
                        icon = Icons.Default.TouchApp,
                        accentColor = CyanTeal,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Navigation Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLaunchCampaignClick,
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("launch_campaign_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NavyDeep)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAr) "حملة جديدة" else "New Ad",
                        color = NavyDeep,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                OutlinedButton(
                    onClick = onNavigateToAiMarket,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_market_btn")
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = AmberGold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAr) "تحليل السوق AI" else "SWOT Analysis",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                OutlinedButton(
                    onClick = onNavigateToPayments,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("payments_wallet_btn")
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAr) "المحفظة" else "Wallet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Active Campaigns Section Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isAr) "الحملات الإعلانية واختبارات A/B المباشرة:" else "Active Campaigns & A/B Tests:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                TextButton(onClick = onNavigateToReports) {
                    Icon(Icons.Default.Assessment, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isAr) "تصدير تقرير PDF" else "Export PDF", color = ElectricCyan, fontSize = 11.sp)
                }
            }
        }

        // List of Active Campaigns
        items(campaigns) { campaign ->
            CampaignCard(
                campaign = campaign,
                isAr = isAr,
                onSelectCampaign = { onSelectCampaign(campaign) },
                onToggleCampaign = { onToggleCampaign(campaign.id) },
                onOptimizeCampaign = { onOptimizeCampaign(campaign.id) },
                onAddBudget = { amt -> onAddBudget(campaign.id, amt) },
                onDeleteCampaign = { onDeleteCampaign(campaign.id) }
            )
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = title, fontSize = 11.sp, color = TextSecondaryDark)
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 10.sp, color = accentColor)
        }
    }
}

@Composable
fun CampaignCard(
    campaign: Campaign,
    isAr: Boolean,
    onSelectCampaign: () -> Unit,
    onToggleCampaign: () -> Unit = {},
    onOptimizeCampaign: () -> Unit = {},
    onAddBudget: (Double) -> Unit = {},
    onDeleteCampaign: () -> Unit = {}
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showAddBudgetDialog by remember { mutableStateOf(false) }
    var budgetInput by remember { mutableStateOf("1000") }

    if (showAddBudgetDialog) {
        AlertDialog(
            onDismissRequest = { showAddBudgetDialog = false },
            containerColor = NavyCard,
            title = {
                Text(
                    text = if (isAr) "زيادة ميزانية الحملة 💰" else "Top Up Campaign Budget",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isAr) "أدخل المبلغ المراد إضافته للحملة (${campaign.title}):" else "Enter amount to add to campaign (${campaign.title}):",
                        color = TextSecondaryDark,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = budgetInput,
                        onValueChange = { budgetInput = it },
                        label = { Text(if (isAr) "المبلغ بالجنيه (EGP)" else "Amount in EGP") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = NavySurface,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(500.0, 1000.0, 2500.0).forEach { amt ->
                            SuggestionChip(
                                onClick = { budgetInput = amt.toInt().toString() },
                                label = { Text("+%.0f".format(amt), fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = budgetInput.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            onAddBudget(amt)
                            Toast.makeText(
                                context,
                                if (isAr) "تمت إضافة %.0f ج.م لميزانية الحملة بنجاح!".format(amt) else "Added %.0f EGP to budget!".format(amt),
                                Toast.LENGTH_SHORT
                            ).show()
                            showAddBudgetDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text(if (isAr) "إضافة الميزانية" else "Add Budget", color = NavyDeep, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBudgetDialog = false }) {
                    Text(if (isAr) "إلغاء" else "Cancel", color = TextSecondaryDark)
                }
            }
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("campaign_card_${campaign.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Platform & Status Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = ElectricCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isAr) campaign.platform.displayNameAr else campaign.platform.displayNameEn,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = when (campaign.status) {
                            CampaignStatus.AI_OPTIMIZING -> AmberGold.copy(alpha = 0.2f)
                            CampaignStatus.ACTIVE -> EmeraldGreen.copy(alpha = 0.2f)
                            else -> TextSecondaryDark.copy(alpha = 0.2f)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isAr) campaign.status.titleAr else campaign.status.titleEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (campaign.status) {
                                CampaignStatus.AI_OPTIMIZING -> AmberGold
                                CampaignStatus.ACTIVE -> EmeraldGreen
                                else -> Color.White
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Share Campaign Live Report
                    IconButton(
                        onClick = {
                            val report = MediaBuyerRepository.generateShareableReportText(campaign, isAr)
                            clipboardManager.setText(AnnotatedString(report))
                            Toast.makeText(
                                context,
                                if (isAr) "تم نسخ تقرير الحملة كاملاً مع رابط المنصة!" else "Campaign report & portal URL copied!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = ElectricCyan, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Campaign Title
            Text(
                text = campaign.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Budget Progress
            val progress = if (campaign.totalBudgetEgp > 0) (campaign.spentEgp / campaign.totalBudgetEgp).toFloat().coerceIn(0f, 1f) else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = ElectricCyan,
                trackColor = NavyCard
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isAr) "تم إنفاق: %.0f ج.م".format(campaign.spentEgp) else "Spent: %.0f EGP".format(campaign.spentEgp),
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
                Text(
                    text = if (isAr) "المتبقي: %.0f ج.م (باقي %d أيام)".format(campaign.remainingBudgetEgp, campaign.remainingDays) else "Remaining: %.0f EGP (%d days left)".format(campaign.remainingBudgetEgp, campaign.remainingDays),
                    fontSize = 11.sp,
                    color = AmberGold
                )
            }

            // A/B Testing Breakdown
            if (campaign.abTestEnabled && campaign.variants.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = NavyCard)
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CompareArrows, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "نتائج اختبار A/B المباشر بالذكاء الاصطناعي:" else "Live AI A/B Test Variants:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberGold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    campaign.variants.forEach { variant ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (variant.isWinner) EmeraldGreen.copy(alpha = 0.12f) else NavyCard
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = variant.name,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        if (variant.isWinner) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = EmeraldGreen,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = if (isAr) "🏆 الفائز بالتحويل" else "🏆 Winner",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = NavyDeep,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = variant.headline,
                                        fontSize = 11.sp,
                                        color = TextSecondaryDark,
                                        maxLines = 1
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${variant.conversionRatePercent}%",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (variant.isWinner) EmeraldGreen else Color.White
                                    )
                                    Text(
                                        text = if (isAr) "${variant.conversions} تحويل" else "${variant.conversions} conv.",
                                        fontSize = 10.sp,
                                        color = TextSecondaryDark
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Campaign Direct Action Buttons Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // AI Boost / Optimize Button
                Button(
                    onClick = {
                        onOptimizeCampaign()
                        Toast.makeText(
                            context,
                            if (isAr) "تم تحسين الحملة واختيار أعلى المتغيرات تحويلاً بالذكاء الاصطناعي ⚡!" else "AI optimization completed! Best variant boosted ⚡",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1.3f)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAr) "تحسين AI ⚡" else "AI Boost ⚡",
                        color = NavyDeep,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                // Pause / Resume Button
                OutlinedButton(
                    onClick = {
                        onToggleCampaign()
                        Toast.makeText(
                            context,
                            if (campaign.status == CampaignStatus.PAUSED)
                                (if (isAr) "تم استئناف الحملة بنجاح!" else "Campaign Resumed!")
                            else
                                (if (isAr) "تم إيقاف الحملة مؤقتاً" else "Campaign Paused"),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (campaign.status == CampaignStatus.PAUSED) EmeraldGreen else Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (campaign.status == CampaignStatus.PAUSED) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (campaign.status == CampaignStatus.PAUSED) (if (isAr) "تشغيل" else "Resume") else (if (isAr) "إيقاف" else "Pause"),
                        fontSize = 11.sp
                    )
                }

                // Add Budget Button
                OutlinedButton(
                    onClick = { showAddBudgetDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricCyan),
                    modifier = Modifier.weight(1.1f)
                ) {
                    Icon(Icons.Default.AddCard, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (isAr) "+ ميزانية" else "+ Budget",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer metrics & action
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "ROAS: ${campaign.roas}x",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen
                    )
                    Text(
                        text = if (isAr) "CPA: %.2f ج.م".format(campaign.cpaEgp) else "CPA: %.2f EGP".format(campaign.cpaEgp),
                        fontSize = 12.sp,
                        color = ElectricCyan
                    )
                }

                TextButton(onClick = onSelectCampaign) {
                    Text(if (isAr) "التفاصيل والتحليل AI" else "Details & SWOT", color = ElectricCyan, fontSize = 11.sp)
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
