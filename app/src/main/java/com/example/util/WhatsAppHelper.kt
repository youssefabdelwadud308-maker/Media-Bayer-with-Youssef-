package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object WhatsAppHelper {
    const val PHONE_DISPLAY = "01284575019"
    const val PHONE_INTL = "201284575019"

    fun openWhatsApp(
        context: Context,
        customMessage: String = "مرحباً خدمة عملاء منصة الميديا باير، أود المساعدة بخصوص الحملات الإعلانية وشحن الميزانية."
    ) {
        try {
            val encodedMessage = Uri.encode(customMessage)
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$PHONE_INTL&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to web browser or dialer
            try {
                val webUri = Uri.parse("https://wa.me/$PHONE_INTL?text=${Uri.encode(customMessage)}")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(webIntent)
            } catch (e2: Exception) {
                Toast.makeText(
                    context,
                    "يرجى التواصل مباشرة على واتساب الرقم: $PHONE_DISPLAY",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
