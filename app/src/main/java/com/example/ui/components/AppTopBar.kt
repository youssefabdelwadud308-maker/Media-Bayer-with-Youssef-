package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import android.widget.Toast
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentUser: User?,
    currentLanguage: String,
    onToggleLanguage: () -> Unit,
    onLogout: () -> Unit,
    onOpenProfileMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier.testTag("app_top_bar"),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NavySurface,
            titleContentColor = Color.White
        ),
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (currentLanguage == "ar") "الميديا باير الذكي" else "Smart Media Buyer",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    // Verification chip
                    Surface(
                        color = Color(0xFF00E5FF).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "AI EGP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // General Manager Badge
                if (currentUser?.role == UserRole.GENERAL_MANAGER) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(AmberGold.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "👑 " + if (currentLanguage == "ar") "وسام المدير العام: Youssef Johnny" else "General Manager: Youssef Johnny",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )
                    }
                } else if (currentUser?.role == UserRole.SUPERVISOR) {
                    Text(
                        text = if (currentLanguage == "ar") "مشرف معتمد: ${currentUser.fullName}" else "Supervisor: ${currentUser.fullName}",
                        fontSize = 11.sp,
                        color = CyanTeal
                    )
                } else if (currentUser != null) {
                    Text(
                        text = if (currentLanguage == "ar") "عميل: ${currentUser.fullName}" else "Client: ${currentUser.fullName}",
                        fontSize = 11.sp,
                        color = TextSecondaryDark
                    )
                }
            }
        },
        actions = {
            val context = LocalContext.current
            val clipboardManager = LocalClipboardManager.current
            val clientUrl = "https://ais-pre-7kcdjlbcyfefsmbwzekmwk-421234073214.europe-west3.run.app"

            // WhatsApp Direct Support Icon
            IconButton(
                onClick = {
                    com.example.util.WhatsAppHelper.openWhatsApp(context)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp Support (01284575019)",
                    tint = Color(0xFF25D366)
                )
            }

            // Share / Copy Link for clients button
            IconButton(
                onClick = {
                    clipboardManager.setText(AnnotatedString(clientUrl))
                    Toast.makeText(
                        context,
                        if (currentLanguage == "ar") "تم نسخ رابط الموقع للعملاء بنجاح!" else "Client platform link copied to clipboard!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share Client Link",
                    tint = ElectricCyan
                )
            }

            // Language switch button
            TextButton(
                onClick = onToggleLanguage
            ) {
                Text(
                    text = if (currentLanguage == "ar") "EN" else "عربي",
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan
                )
            }

            if (currentUser != null) {
                // Balance badge
                Surface(
                    color = NavyCard,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Balance",
                            tint = AmberGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.0f ج.م", currentUser.balanceEgp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Switch / Logout button
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier.testTag("logout_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = CrimsonRed
                    )
                }
            }
        }
    )
}
