package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FaqItem
import com.example.model.LongTermService
import com.example.ui.theme.*

@Composable
fun FaqAndSecurityScreen(
    faqs: List<FaqItem>,
    longTermServices: List<LongTermService>,
    currentLanguage: String,
    onExportBackup: () -> String,
    modifier: Modifier = Modifier
) {
    val isAr = currentLanguage == "ar"
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var expandedFaqId by remember { mutableStateOf<String?>(null) }
    var backupJsonSnapshot by remember { mutableStateOf<String?>(null) }

    val filteredFaqs = remember(searchQuery, faqs) {
        if (searchQuery.isBlank()) faqs
        else faqs.filter {
            it.questionAr.contains(searchQuery, ignoreCase = true) ||
            it.answerAr.contains(searchQuery, ignoreCase = true) ||
            it.questionEn.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Security & Isolation Guarantee Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(EmeraldGreen)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = EmeraldGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isAr) "الأمان والتشفير المتقدم وحقوق العميل" else "Advanced Security & Client Protection Policy",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isAr) "تشفير 256-bit | حسابات منفصلة ومحمية | ضمان كامل للميزانية" else "256-bit Encryption | Isolated Tenant Architecture",
                            fontSize = 11.sp,
                            color = EmeraldGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isAr)
                        "كل حساب عميل معزول تماماً ومحمي بأعلى معايير الأمان لمنع أي تداخل بين البيانات الإعلانية أو الحسابات البنكية. ميزانياتك بالجنيه المصري تذهب مباشرة لحساباتك الإعلانية مع استرداد فوري لأي رصيد غير مستهلك."
                    else
                        "Every client account is securely isolated. Egyptian Pound ad balances are deployed directly with complete financial integrity and multi-tenant security.",
                    fontSize = 11.sp,
                    color = TextSecondaryDark,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Backup Trigger
                Button(
                    onClick = {
                        val snapshot = onExportBackup()
                        backupJsonSnapshot = snapshot
                        Toast.makeText(
                            context,
                            if (isAr) "تم إنشاء نسخة احتياطية مشفرة لبياناتك بنجاح!" else "Encrypted multi-point backup snapshot generated!",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("backup_data_btn")
                ) {
                    Icon(Icons.Default.Backup, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "إنشاء نسخة احتياطية آمنة للبيانات (Backup)" else "Create Multi-Point Data Backup",
                        color = ElectricCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (backupJsonSnapshot != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = backupJsonSnapshot ?: "",
                        fontSize = 10.sp,
                        color = EmeraldGreen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavyDeep, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                }
            }
        }

        // Long-Term Services Available for Subscription
        Text(
            text = if (isAr) "الباقات والخدمات طويلة الأمد المتاحة:" else "Available Long-Term Packages:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            longTermServices.filter { it.isAvailable }.forEach { service ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isAr) service.titleAr else service.titleEn,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGold
                            )

                            Text(
                                text = String.format("%.0f ج.م", service.priceEgp),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isAr) service.descriptionAr else service.descriptionEn,
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val features = if (isAr) service.featuresAr else service.featuresEn
                        features.take(3).forEach { feat ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Text(text = "✓ ", color = EmeraldGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = feat, fontSize = 11.sp, color = TextPrimaryDark)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    if (isAr) "تم اختيار ${service.titleAr}، سيتم تفعيل الخدمة فوراً عبر محفظتك!" else "Service selected!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = if (isAr) "الاشتراك بالخدمة" else "Subscribe",
                                color = NavyDeep,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // FAQs Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isAr) "قاعدة بيانات الأسئلة الشائعة:" else "Frequently Asked Questions (FAQ):",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // FAQ Search
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (isAr) "ابحث في الأسئلة الشائعة..." else "Search FAQs...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ElectricCyan) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("faq_search_input"),
            singleLine = true
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            filteredFaqs.forEach { faq ->
                val isExpanded = expandedFaqId == faq.id
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedFaqId = if (isExpanded) null else faq.id }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isAr) faq.questionAr else faq.questionEn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = ElectricCyan
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                HorizontalDivider(color = NavyCard)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isAr) faq.answerAr else faq.answerEn,
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Rights & Assurances Policy
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = if (isAr) "سياسة الخصوصية وحقوق العميل المعتمدة:" else "Privacy & Client Rights Charter:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isAr)
                        "جميع الحقوق محفوظة لمنصة الميديا باير الذكي تحت إدارة وإشراف المدير العام Youssef Johnny © 2025. نلتزم بحماية سرية إعلاناتك، استراتيجيات التسويق، وأموالك المودعة عبر فودافون كاش، أورانج كاش، انستاباي، والفيزا مع ضمان استرداد فوري وفق الشروط والأحكام."
                    else
                        "All rights reserved to Smart Media Buyer AI, supervised by General Manager Youssef Johnny © 2025. End-to-end data confidentiality guaranteed.",
                    fontSize = 10.sp,
                    color = TextSecondaryDark,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
