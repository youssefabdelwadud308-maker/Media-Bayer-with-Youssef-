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
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun PaymentScreen(
    currentUser: User,
    gateways: List<PaymentGatewayConfig>,
    transactions: List<PaymentTransaction>,
    currentLanguage: String,
    onSubmitPayment: (amountEgp: Double, method: PaymentMethodType, ref: String, isAutoRenew: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = currentLanguage == "ar"

    var selectedMethod by remember { mutableStateOf(PaymentMethodType.VODAFONE_CASH) }
    var amountText by remember { mutableStateOf("5000") }
    var transactionRef by remember { mutableStateOf("") }
    var isAutoRenewActive by remember { mutableStateOf(true) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val activeGateway = gateways.find { it.type == selectedMethod }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Card in EGP
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isAr) "رصيد المحفظة الإعلانية (EGP)" else "Ad Wallet Balance (EGP)",
                        fontSize = 13.sp,
                        color = TextSecondaryDark
                    )

                    Surface(
                        color = AmberGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isAr) "العملة: جنيه مصري" else "Currency: EGP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = String.format("%.2f ج.م", currentUser.balanceEgp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Auto-Renewal and Expiration Reminder Alert
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAr)
                                "نظام التجديد التلقائي نشط: يتم إرسال تنبيهات لحظية قبل 3 أيام من انتهاء ميزانية أي حملة للحفاظ على خوارزمية التعلم."
                            else
                                "Auto-renewal reminders active: alerts sent 3 days prior to budget expiration to protect campaign learning.",
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Deposit Section
        Text(
            text = if (isAr) "شحن الرصيد بالجنيه المصري (بوابات الدفع المصرية):" else "Top-Up Ad Balance (Egyptian Gateways):",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Payment Method Selectors
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PaymentMethodType.values().forEach { method ->
                val isSelected = selectedMethod == method
                val gw = gateways.find { it.type == method }
                val isEnabled = gw?.isEnabled ?: true

                if (isEnabled) {
                    Card(
                        onClick = { selectedMethod = method },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ElectricCyan.copy(alpha = 0.15f) else NavySurface
                        ),
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ElectricCyan)) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("method_${method.name}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedMethod = method },
                                colors = RadioButtonDefaults.colors(selectedColor = ElectricCyan)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isAr) method.titleAr else method.titleEn,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isAr) (gw?.instructionsAr ?: method.providerInfo) else (gw?.instructionsEn ?: method.providerInfo),
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Transfer Instructions Card
        if (activeGateway != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = AmberGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "تفاصيل التحويل للحساب/المحفظة:" else "Transfer Account Details:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeGateway.walletNumberOrAccount,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Deposit Amount & Transaction Code Form
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text(if (isAr) "المبلغ المراد شحنه (جنيه مصري)" else "Amount in EGP") },
                    leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = ElectricCyan) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("deposit_amount_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = transactionRef,
                    onValueChange = { transactionRef = it },
                    label = { Text(if (isAr) "رقم العملية / كود التحويل (من تطبيق المحفظة)" else "Transaction Reference ID") },
                    placeholder = { Text(if (isAr) "مثال: VF982341 أو رقم إيصال انستاباي" else "e.g. VF982341") },
                    leadingIcon = { Icon(Icons.Default.Receipt, contentDescription = null, tint = ElectricCyan) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transaction_ref_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Auto renewal switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAr) "تفعيل التجديد التلقائي للميزانية" else "Enable Budget Auto-Renewal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isAr) "تجديد آلي عند وصول الميزانية لـ 15% المتبقية" else "Auto-renews when 15% budget remaining",
                            fontSize = 10.sp,
                            color = TextSecondaryDark
                        )
                    }
                    Switch(
                        checked = isAutoRenewActive,
                        onCheckedChange = { isAutoRenewActive = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = ElectricCyan)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val amt = amountText.toDoubleOrNull() ?: 1000.0
                        onSubmitPayment(amt, selectedMethod, transactionRef, isAutoRenewActive)
                        showSuccessDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_deposit_btn")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NavyDeep)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "تأكيد الإيداع وإضافة الرصيد فوراً" else "Confirm & Deposit EGP",
                        color = NavyDeep,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Recent Transactions History
        Text(
            text = if (isAr) "سجل العمليات والمدفوعات الأخيرة:" else "Recent Transactions History:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            transactions.take(5).forEach { tx ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Surface(
                            color = EmeraldGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(modifier = Modifier.padding(6.dp)) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) tx.method.titleAr else tx.method.titleEn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Ref: ${tx.transactionRef}",
                                fontSize = 10.sp,
                                color = TextSecondaryDark
                            )
                        }
                        Text(
                            text = "+%.0f ج.م".format(tx.amountEgp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text(if (isAr) "تم استلام طلب الشحن بنجاح" else "Deposit Confirmed") },
            text = {
                Text(
                    if (isAr)
                        "تمت إضافة الرصيد إلى محفظتك الإعلانية بالجنيه المصري وتفعيل تنبيهات تجديد الميزانية للحملات النشطة."
                    else
                        "Your ad balance has been credited in EGP and renewal notifications are armed."
                )
            },
            confirmButton = {
                Button(onClick = { showSuccessDialog = false }) {
                    Text(if (isAr) "حسناً" else "OK")
                }
            }
        )
    }
}
