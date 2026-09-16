package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.ChatMessage
import com.example.model.User
import com.example.ui.theme.*
import com.example.util.WhatsAppHelper

@Composable
fun SupportChatScreen(
    currentUser: User,
    messages: List<ChatMessage>,
    currentLanguage: String,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = currentLanguage == "ar"
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep)
            .padding(16.dp)
    ) {
        // Chat Header
        Card(
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(14.dp)
            ) {
                Surface(
                    color = EmeraldGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isAr) "الدعم الفني الذكي المباشر (24/7)" else "Smart Live Technical Support (24/7)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isAr) "متاح دائماً لحل المشكلات التقنية وفحص البيكسل وتأكيد المدفوعات" else "Instant problem resolution & campaign troubleshooting",
                        fontSize = 11.sp,
                        color = TextSecondaryDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // WhatsApp Direct Customer Service Card
        val context = LocalContext.current
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A2F)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = Color(0xFF25D366),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAr) "خدمة العملاء المباشرة عبر واتساب" else "Direct WhatsApp Support",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Text(
                        text = "01284575019",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF25D366)
                    )
                }
                Button(
                    onClick = {
                        WhatsAppHelper.openWhatsApp(context)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = if (isAr) "فتح واتساب 💬" else "Chat Now",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Suggestion Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SuggestionChip(
                onClick = { onSendMessage(if (isAr) "كيف أشحن عبر فودافون كاش أو انستاباي؟" else "How to deposit via Vodafone Cash?") },
                label = { Text(if (isAr) "شحن المحافظ" else "E-Wallets", fontSize = 10.sp) }
            )
            SuggestionChip(
                onClick = { onSendMessage(if (isAr) "كيف يحسن الذكاء الاصطناعي اختبار A/B؟" else "How does AI optimize A/B?") },
                label = { Text(if (isAr) "اختبار A/B" else "A/B Testing", fontSize = 10.sp) }
            )
            SuggestionChip(
                onClick = { onSendMessage(if (isAr) "تواصل مع الإدارة أو Youssef Johnny" else "Contact GM Youssef Johnny") },
                label = { Text(if (isAr) "المدير العام" else "General Manager", fontSize = 10.sp) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                val isMe = !msg.isFromAiOrSupport

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe) ElectricCyan else NavySurface
                        ),
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (isMe) 14.dp else 2.dp,
                            bottomEnd = if (isMe) 2.dp else 14.dp
                        ),
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.senderName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMe) NavyDeep else AmberGold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.messageText,
                                fontSize = 13.sp,
                                color = if (isMe) NavyDeep else Color.White,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Input Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text(if (isAr) "اكتب مشكلتك أو استفسارك هنا..." else "Type message...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("support_input_field"),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onSendMessage(inputText)
                        inputText = ""
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = ElectricCyan),
                modifier = Modifier.testTag("send_support_btn")
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = NavyDeep)
            }
        }
    }
}
