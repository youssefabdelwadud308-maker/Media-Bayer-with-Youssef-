package com.example.model

enum class PaymentMethodType(
    val titleAr: String,
    val titleEn: String,
    val providerInfo: String
) {
    VODAFONE_CASH("فودافون كاش (Vodafone Cash)", "Vodafone Cash", "تحويل لمحفظة فودافون كاش 01023456789"),
    ORANGE_CASH("أورانج كاش (Orange Cash)", "Orange Cash", "تحويل لمحفظة أورانج كاش 01223456789"),
    INSTAPAY("انستاباي (InstaPay)", "InstaPay Egypt", "تحويل لحساب انستاباي IPA: youssef.media@instapay"),
    VISA_MASTERCARD("بطاقة بنكية (فيزا / ماستركارد)", "Visa / MasterCard", "دفع فوري عبر بوابة الدفع الإلكتروني"),
    FAWRY("فوري وأمان", "Fawry / Aman", "كود دفع فوري بصلاحية 24 ساعة")
}

data class PaymentGatewayConfig(
    val id: String,
    val type: PaymentMethodType,
    val isEnabled: Boolean = true,
    val feePercentage: Double = 0.0,
    val walletNumberOrAccount: String = "01023456789",
    val instructionsAr: String = "يتم التحويل وتأكيد المعاملة فورياً عبر رقم العملية",
    val instructionsEn: String = "Transfer to the specified number and enter the transaction ID for instant verification"
)

data class PaymentTransaction(
    val id: String,
    val userId: String,
    val userName: String,
    val amountEgp: Double,
    val method: PaymentMethodType,
    val transactionRef: String,
    val isAutoRenewal: Boolean = false,
    val status: TransactionStatus = TransactionStatus.APPROVED,
    val createdAt: Long = System.currentTimeMillis()
)

enum class TransactionStatus(val titleAr: String, val titleEn: String) {
    PENDING("قيد المراجعة", "Pending"),
    APPROVED("مكتملة ومؤكدة", "Approved"),
    REJECTED("مرفوضة", "Rejected")
}
