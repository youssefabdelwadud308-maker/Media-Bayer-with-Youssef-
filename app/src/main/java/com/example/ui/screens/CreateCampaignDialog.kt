package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AdPlatform
import com.example.model.CampaignGoal
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCampaignDialog(
    currentLanguage: String,
    onDismiss: () -> Unit,
    onCreateCampaign: (
        title: String,
        platform: AdPlatform,
        goal: CampaignGoal,
        totalBudgetEgp: Double,
        dailyBudgetEgp: Double,
        days: Int,
        variantA: String,
        variantB: String,
        audience: String
    ) -> Unit
) {
    val isAr = currentLanguage == "ar"

    var title by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf(AdPlatform.META) }
    var selectedGoal by remember { mutableStateOf(CampaignGoal.CONVERSIONS) }
    var totalBudgetEgpText by remember { mutableStateOf("15000") }
    var dailyBudgetEgpText by remember { mutableStateOf("750") }
    var durationDaysText by remember { mutableStateOf("20") }
    var variantAHeadline by remember { mutableStateOf("فيديو ريلز جذاب: خصم 40% لفترة محدودة والشحن مجاني") }
    var variantBHeadline by remember { mutableStateOf("كاروسيل منتجات تفاعلي: اشترِ قطعة واحصل على الثانية بنصف السعر") }
    var targetAudience by remember { mutableStateOf("متسوقين إلكترونيين، أعمار 21-40، القاهرة والإسكندرية والجيزة والدلتا") }

    var expandedPlatform by remember { mutableStateOf(false) }
    var expandedGoal by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "إطلاق حملة ذكية واختبار A/B" else "Create AI Campaign & A/B Test",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = if (isAr) "يدير الذكاء الاصطناعي الحملة آلياً لتحقيق أعلى معدل تحويل وأقل تكلفة نقرة." else "AI will autonomously optimize variants for peak ROAS.",
                    fontSize = 11.sp,
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                )

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (isAr) "اسم الحملة الإعلانية" else "Campaign Title") },
                    placeholder = { Text(if (isAr) "مثال: حملة تخفيضات الأحذية الكبرى" else "e.g. Summer Sale Promo") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campaign_title_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Platform Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedPlatform,
                    onExpandedChange = { expandedPlatform = !expandedPlatform }
                ) {
                    OutlinedTextField(
                        value = if (isAr) selectedPlatform.displayNameAr else selectedPlatform.displayNameEn,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isAr) "المنصة الإعلانية" else "Ad Platform") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPlatform) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPlatform,
                        onDismissRequest = { expandedPlatform = false }
                    ) {
                        AdPlatform.values().forEach { platform ->
                            DropdownMenuItem(
                                text = { Text(if (isAr) platform.displayNameAr else platform.displayNameEn) },
                                onClick = {
                                    selectedPlatform = platform
                                    expandedPlatform = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Goal Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedGoal,
                    onExpandedChange = { expandedGoal = !expandedGoal }
                ) {
                    OutlinedTextField(
                        value = if (isAr) selectedGoal.titleAr else selectedGoal.titleEn,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isAr) "الهدف التسويقي (Campaign Goal)" else "Objective") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGoal) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGoal,
                        onDismissRequest = { expandedGoal = false }
                    ) {
                        CampaignGoal.values().forEach { goal ->
                            DropdownMenuItem(
                                text = { Text(if (isAr) goal.titleAr else goal.titleEn) },
                                onClick = {
                                    selectedGoal = goal
                                    expandedGoal = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Budget Row (in EGP)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = totalBudgetEgpText,
                        onValueChange = { totalBudgetEgpText = it },
                        label = { Text(if (isAr) "الميزانية الكلية (ج.م)" else "Total Budget (EGP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("total_budget_input")
                    )

                    OutlinedTextField(
                        value = dailyBudgetEgpText,
                        onValueChange = { dailyBudgetEgpText = it },
                        label = { Text(if (isAr) "الإنفاق اليومي (ج.م)" else "Daily Budget (EGP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("daily_budget_input")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = durationDaysText,
                    onValueChange = { durationDaysText = it },
                    label = { Text(if (isAr) "مدة الحملة (بالأيام)" else "Duration (Days)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // A/B Testing Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CompareArrows, contentDescription = null, tint = AmberGold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "إعداد اختبار A/B التلقائي (Split Testing)" else "Autonomous A/B Split Test Setup",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGold
                            )
                        }
                        Text(
                            text = if (isAr) "سيوجه الذكاء الاصطناعي 50% لكل نسخة ثم يحول الميزانية للمتغير الفائز تلقائياً." else "AI splits traffic 50/50 then doubles down on the winner.",
                            fontSize = 11.sp,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = variantAHeadline,
                            onValueChange = { variantAHeadline = it },
                            label = { Text(if (isAr) "عنوان وصيغة المتغير (أ)" else "Variant (A) Hook & Offer") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("variant_a_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = variantBHeadline,
                            onValueChange = { variantBHeadline = it },
                            label = { Text(if (isAr) "عنوان وصيغة المتغير (ب)" else "Variant (B) Hook & Offer") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("variant_b_input")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Target Audience
                OutlinedTextField(
                    value = targetAudience,
                    onValueChange = { targetAudience = it },
                    label = { Text(if (isAr) "الجمهور المستهدف والمحافظات" else "Audience & Targeting") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(if (isAr) "إلغاء" else "Cancel", color = TextSecondaryDark)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val totalBudget = totalBudgetEgpText.toDoubleOrNull() ?: 10000.0
                            val dailyBudget = dailyBudgetEgpText.toDoubleOrNull() ?: 500.0
                            val days = durationDaysText.toIntOrNull() ?: 15
                            onCreateCampaign(
                                if (title.isBlank()) (if (isAr) "حملة تسويقية ذكية" else "Smart AI Campaign") else title,
                                selectedPlatform,
                                selectedGoal,
                                totalBudget,
                                dailyBudget,
                                days,
                                variantAHeadline,
                                variantBHeadline,
                                targetAudience
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        modifier = Modifier.testTag("confirm_create_campaign_btn")
                    ) {
                        Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = NavyDeep)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "إطلاق الحملة فوراً 🚀" else "Launch Campaign 🚀",
                            color = NavyDeep,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
