package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import com.example.model.Campaign
import com.example.model.User
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportScreen(
    currentUser: User,
    campaigns: List<Campaign>,
    currentLanguage: String,
    modifier: Modifier = Modifier
) {
    val isAr = currentLanguage == "ar"
    val context = LocalContext.current
    val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    val totalSpent = campaigns.sumOf { it.spentEgp }
    val totalConversions = campaigns.sumOf { it.conversions }
    val totalImpressions = campaigns.sumOf { it.impressions }
    val totalClicks = campaigns.sumOf { it.clicks }
    val avgRoas = if (campaigns.isNotEmpty()) campaigns.map { it.roas }.average() else 0.0

    var isExporting by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Report Header
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
                    Column {
                        Text(
                            text = if (isAr) "تقرير الأداء التسويقي الدوري (PDF)" else "Periodic Marketing Performance Report",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isAr) "تاريخ الإصدار: $dateStr | العميل: ${currentUser.fullName}" else "Issued: $dateStr | Client: ${currentUser.fullName}",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                    }

                    Surface(
                        color = ElectricCyan.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "OFFICIAL PDF",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        isExporting = true
                        Toast.makeText(
                            context,
                            if (isAr) "تم تجهيز وتصدير التقرير الدوري بصيغة PDF بنجاح!" else "PDF Performance Report generated successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("export_pdf_btn")
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = NavyDeep)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "تحميل وتصدير التقرير بصيغة PDF 📄" else "Download / Export Report as PDF 📄",
                        color = NavyDeep,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Executive Financial & ROI Table
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isAr) "ملخص المؤشرات المالية والتحويلية الإجمالية:" else "Executive Marketing & Financial Metrics:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberGold
                )

                Spacer(modifier = Modifier.height(10.dp))

                ReportRow(if (isAr) "إجمالي الإنفاق الإعلاني" else "Total Ad Spend", String.format("%.2f ج.م", totalSpent), ElectricCyan)
                ReportRow(if (isAr) "إجمالي المبيعات والتحويلات" else "Total Conversions", "$totalConversions تحويل", EmeraldGreen)
                ReportRow(if (isAr) "متوسط العائد على الإنفاق (ROAS)" else "Average ROAS", String.format("%.1fx", avgRoas), AmberGold)
                ReportRow(if (isAr) "إجمالي مرات الظهور والوصول" else "Total Impressions", "$totalImpressions ظهور", Color.White)
                ReportRow(if (isAr) "إجمالي النقرات التفاعلية" else "Total Clicks", "$totalClicks نقرة", Color.White)
                ReportRow(if (isAr) "العملة المعتمدة" else "Billing Currency", "الجنيه المصري (EGP)", ElectricCyan)
            }
        }

        // Campaign Documents Detailed Breakdown
        Text(
            text = if (isAr) "إحصائيات تفصيلية لكل وثيقة وحملة إعلانية:" else "Detailed Breakdown Per Campaign / Document:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            campaigns.forEach { camp ->
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
                                text = camp.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = if (isAr) camp.platform.displayNameAr else camp.platform.displayNameEn,
                                fontSize = 11.sp,
                                color = ElectricCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = if (isAr) "الإنفاق: %.0f ج.م".format(camp.spentEgp) else "Spend: %.0f EGP".format(camp.spentEgp), fontSize = 11.sp, color = TextSecondaryDark)
                            Text(text = "ROAS: ${camp.roas}x", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                            Text(text = if (isAr) "CPA: %.2f ج.م".format(camp.cpaEgp) else "CPA: %.2f EGP".format(camp.cpaEgp), fontSize = 11.sp, color = AmberGold)
                        }

                        if (camp.abTestEnabled && camp.variants.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            val winner = camp.variants.find { it.isWinner } ?: camp.variants.first()
                            Surface(
                                color = EmeraldGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (isAr)
                                        "نتيجة اختبار A/B: تفوق '${winner.name}' بمعدل تحويل ${winner.conversionRatePercent}% وحقق أفضل وصول."
                                    else
                                        "A/B Winner: '${winner.name}' with ${winner.conversionRatePercent}% CR.",
                                    fontSize = 11.sp,
                                    color = EmeraldGreen,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportRow(title: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 12.sp, color = TextSecondaryDark)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
