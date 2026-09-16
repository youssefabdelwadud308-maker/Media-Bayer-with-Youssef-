package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MediaBuyerRepository
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.*
import com.example.util.WhatsAppHelper

@Composable
fun AuthScreen(
    currentLanguage: String,
    onRegister: (fullName: String, email: String, phone: String, password: String, age: Int, role: UserRole, gmCode: String) -> Pair<Boolean, String>,
    onLogin: (identifier: String, password: String) -> Pair<Boolean, String>,
    onQuickSwitch: (userId: String) -> Unit,
    availableUsers: List<User>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAr = currentLanguage == "ar"

    // Default Tab: 0 = Login, 1 = Register
    var selectedTab by remember { mutableIntStateOf(0) }

    // GM Login State
    var gmPasscodeInput by remember { mutableStateOf("") }
    var isGmPasswordVisible by remember { mutableStateOf(false) }

    // Client / Supervisor Login State
    var loginIdentifier by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var isClientPasswordVisible by remember { mutableStateOf(false) }

    // Register Form State (name, email, phone, age, password, role, gmCode)
    var regFullName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regAge by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regGmCode by remember { mutableStateOf("") }
    var regRole by remember { mutableStateOf(UserRole.CLIENT) }
    var isRegPasswordVisible by remember { mutableStateOf(false) }
    var isRegGmCodeVisible by remember { mutableStateOf(false) }

    // Status Feedback
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccessStatus by remember { mutableStateOf(false) }

    // Dedicated Pending Approval State Holder for Supervisors
    var pendingApprovalSupervisor by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Platform Brand Header
        Surface(
            color = NavyCard,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Logo",
                    tint = ElectricCyan,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (isAr) "منصة الميديا باير الذكي" else "Smart Media Buyer AI",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = if (isAr) "نظام الدخول الآمن لإدارة الحملات والميزانيات بالجنيه المصري" else "Secure Authentication & Media Buying Platform",
            fontSize = 12.sp,
            color = TextSecondaryDark,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Primary Navigation Tabs
        Surface(
            color = NavySurface,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Button(
                    onClick = {
                        selectedTab = 0
                        statusMessage = null
                        pendingApprovalSupervisor = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 0) ElectricCyan else Color.Transparent,
                        contentColor = if (selectedTab == 0) NavyDeep else TextSecondaryDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    elevation = null,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tab_login")
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "تسجيل الدخول" else "Sign In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = {
                        selectedTab = 1
                        statusMessage = null
                        pendingApprovalSupervisor = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 1) ElectricCyan else Color.Transparent,
                        contentColor = if (selectedTab == 1) NavyDeep else TextSecondaryDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    elevation = null,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tab_register")
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "إنشاء حساب جديد" else "Create Account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- DEDICATED PENDING-APPROVAL STATE CARD FOR SUPERVISORS ---
        if (pendingApprovalSupervisor != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(AmberGold)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("pending_approval_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = AmberGold.copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.HourglassTop,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) "طلب المشرف قيد المراجعة والاعتماد ⏳" else "Supervisor Pending Approval ⏳",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AmberGold
                            )
                            Text(
                                text = if (isAr) "الحالة: معلّق (Pending GM Approval)" else "Status: Pending GM Approval",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = pendingApprovalSupervisor ?: "",
                        fontSize = 12.sp,
                        color = Color.White,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Notice box
                    Surface(
                        color = AmberGold.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAr)
                                    "حسابات المشرفين مقيدة بقرار المدير العام Youssef Johnny للحفاظ على أمان المنصة والميزانيات."
                                else
                                    "Supervisor accounts are locked until approved by General Manager Youssef Johnny.",
                                fontSize = 11.sp,
                                color = AmberGold,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // WhatsApp Fast-Track Approval
                    Button(
                        onClick = { WhatsAppHelper.openWhatsApp(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("whatsapp_approval_request_btn")
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAr) "طلب تسريع الاعتماد عبر واتساب (01284575019)" else "Expedite Approval via WhatsApp",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            pendingApprovalSupervisor = null
                            selectedTab = 0
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(if (isAr) "العودة لتسجيل الدخول" else "Back to Sign In", fontSize = 12.sp)
                    }
                }
            }
        }

        // Status Feedback Banner (if any message)
        if (statusMessage != null && pendingApprovalSupervisor == null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSuccessStatus) EmeraldGreen.copy(alpha = 0.2f) else CrimsonRed.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(if (isSuccessStatus) EmeraldGreen else CrimsonRed)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("auth_status_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Icon(
                        imageVector = if (isSuccessStatus) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = if (isSuccessStatus) EmeraldGreen else CrimsonRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = statusMessage ?: "",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // =========================================================================
        // TAB 0: LOGIN (General Manager VIP Gate & Client / Supervisor Sign In)
        // =========================================================================
        if (selectedTab == 0) {

            // --- 1. GENERAL MANAGER VIP GATE ---
            Card(
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(AmberGold)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gm_login_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = AmberGold.copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = AmberGold, modifier = Modifier.size(22.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) "تسجيل دخول المدير العام: Youssef Johnny 👑" else "General Manager Gate: Youssef Johnny 👑",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AmberGold
                            )
                            Text(
                                text = if (isAr) "مقيد برمز الأمان السري المشفر (Access Code: 308380)" else "Strictly restricted by secret access code 308380",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isAr) "أدخل رمز الدخول السري للمدير العام:" else "Enter GM Secret Access Code:",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = gmPasscodeInput,
                        onValueChange = { gmPasscodeInput = it },
                        placeholder = { Text(if (isAr) "اكتب رمز الدخول السري (308380)..." else "Enter secret passcode 308380...") },
                        leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = AmberGold) },
                        trailingIcon = {
                            IconButton(onClick = { isGmPasswordVisible = !isGmPasswordVisible }) {
                                Icon(
                                    imageVector = if (isGmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password",
                                    tint = AmberGold
                                )
                            }
                        },
                        visualTransformation = if (isGmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            val clean = MediaBuyerRepository.normalizeArabicDigitsAndText(gmPasscodeInput)
                            val res = onLogin(MediaBuyerRepository.GM_SECRET_PASSCODE, clean)
                            statusMessage = res.second
                            isSuccessStatus = res.first
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberGold,
                            unfocusedBorderColor = NavyCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("gm_passcode_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val clean = MediaBuyerRepository.normalizeArabicDigitsAndText(gmPasscodeInput)
                            val res = onLogin(MediaBuyerRepository.GM_SECRET_PASSCODE, clean)
                            statusMessage = res.second
                            isSuccessStatus = res.first
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("btn_gm_login")
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = NavyDeep)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAr) "التحقق والدخول كمدير عام (Youssef Johnny)" else "Verify & Sign In as General Manager",
                            color = NavyDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- 2. CLIENT & SUPERVISOR LOGIN CARD ---
            Card(
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAr) "تسجيل دخول العملاء والمشرفين" else "Client & Supervisor Login",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Identifier (Phone or Email)
                    OutlinedTextField(
                        value = loginIdentifier,
                        onValueChange = { loginIdentifier = it },
                        label = { Text(if (isAr) "البريد الإلكتروني أو رقم الهاتف" else "Email or Phone Number") },
                        placeholder = { Text(if (isAr) "مثال: 01xxxxxxxxx أو your@email.com" else "Email or Phone") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ElectricCyan) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = NavyCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_identifier_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        label = { Text(if (isAr) "كلمة المرور" else "Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricCyan) },
                        trailingIcon = {
                            IconButton(onClick = { isClientPasswordVisible = !isClientPasswordVisible }) {
                                Icon(
                                    imageVector = if (isClientPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password",
                                    tint = ElectricCyan
                                )
                            }
                        },
                        visualTransformation = if (isClientPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            val res = onLogin(loginIdentifier, loginPassword)
                            if (!res.first && res.second.contains("Pending Approval", ignoreCase = true)) {
                                pendingApprovalSupervisor = res.second
                            } else {
                                statusMessage = res.second
                                isSuccessStatus = res.first
                            }
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = NavyCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Button
                    Button(
                        onClick = {
                            if (loginIdentifier.isBlank()) {
                                statusMessage = if (isAr) "يرجى إدخال البريد الإلكتروني أو رقم الهاتف" else "Please enter email or phone number"
                                isSuccessStatus = false
                            } else {
                                val res = onLogin(loginIdentifier, loginPassword)
                                if (!res.first && res.second.contains("Pending Approval", ignoreCase = true)) {
                                    pendingApprovalSupervisor = res.second
                                } else {
                                    statusMessage = res.second
                                    isSuccessStatus = res.first
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_client_login")
                    ) {
                        Icon(Icons.Default.Login, contentDescription = null, tint = NavyDeep)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAr) "تسجيل الدخول للمنصة" else "Sign In to Platform",
                            color = NavyDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAr) "ليس لديك حساب بعد؟" else "Don't have an account?",
                            fontSize = 12.sp,
                            color = TextSecondaryDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "إنشاء حساب جديد" else "Register Now",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            modifier = Modifier.clickable {
                                selectedTab = 1
                                statusMessage = null
                                pendingApprovalSupervisor = null
                            }
                        )
                    }
                }
            }

        } else {
            // =========================================================================
            // TAB 1: REGISTRATION (Secure form with Name, Email, Phone, Age, and GM code)
            // =========================================================================
            Card(
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isAr) "إنشاء حساب جديد بالمنصة 🚀" else "Register New Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isAr) "يرجى تعبئة الحقول المطلوبة (الاسم، البريد، الهاتف، والعمر):" else "Fill in the required fields (Name, Email, Phone, Age):",
                        fontSize = 11.sp,
                        color = TextSecondaryDark
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Role Selection Cards
                    Text(
                        text = if (isAr) "اختر نوع الحساب المطلوب:" else "Select Account Type:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option 1: Client
                    Surface(
                        color = if (regRole == UserRole.CLIENT) ElectricCyan.copy(alpha = 0.15f) else NavyCard,
                        shape = RoundedCornerShape(12.dp),
                        border = if (regRole == UserRole.CLIENT) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ElectricCyan)) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { regRole = UserRole.CLIENT }
                            .testTag("role_client_option")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            RadioButton(
                                selected = regRole == UserRole.CLIENT,
                                onClick = { regRole = UserRole.CLIENT },
                                colors = RadioButtonDefaults.colors(selectedColor = ElectricCyan)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isAr) "حساب عميل (أصحاب المتاجر والشركات) 💼" else "Client Account (Business Owner)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isAr) "تفعيل فوري لإنشاء الحملات الإعلانية ومتابعة أداء الذكاء الاصطناعي." else "Instant activation to launch ads & manage budget.",
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option 2: Supervisor (Requires GM Approval)
                    Surface(
                        color = if (regRole == UserRole.SUPERVISOR) AmberGold.copy(alpha = 0.15f) else NavyCard,
                        shape = RoundedCornerShape(12.dp),
                        border = if (regRole == UserRole.SUPERVISOR) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AmberGold)) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { regRole = UserRole.SUPERVISOR }
                            .testTag("role_supervisor_option")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            RadioButton(
                                selected = regRole == UserRole.SUPERVISOR,
                                onClick = { regRole = UserRole.SUPERVISOR },
                                colors = RadioButtonDefaults.colors(selectedColor = AmberGold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (isAr) "حساب مشرف إعلاني 🛡️" else "Supervisor Account 🛡️",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (regRole == UserRole.SUPERVISOR) AmberGold else Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = AmberGold.copy(alpha = 0.25f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = if (isAr) "قيد الموافقة" else "Pending Approval",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AmberGold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = if (isAr)
                                        "⚠️ يتطلب مراجعة واعتماد رسمي من المدير العام Youssef Johnny قبل السماح بالدخول."
                                    else
                                        "Requires strict approval from General Manager Youssef Johnny before login.",
                                    fontSize = 11.sp,
                                    color = if (regRole == UserRole.SUPERVISOR) AmberGold.copy(alpha = 0.9f) else TextSecondaryDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option 3: General Manager (Strict Secret Key 308380)
                    Surface(
                        color = if (regRole == UserRole.GENERAL_MANAGER) AmberGold.copy(alpha = 0.2f) else NavyCard,
                        shape = RoundedCornerShape(12.dp),
                        border = if (regRole == UserRole.GENERAL_MANAGER) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AmberGold)) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { regRole = UserRole.GENERAL_MANAGER }
                            .testTag("role_gm_option")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            RadioButton(
                                selected = regRole == UserRole.GENERAL_MANAGER,
                                onClick = { regRole = UserRole.GENERAL_MANAGER },
                                colors = RadioButtonDefaults.colors(selectedColor = AmberGold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isAr) "المدير العام: Youssef Johnny 👑" else "General Manager: Youssef Johnny 👑",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AmberGold
                                )
                                Text(
                                    text = if (isAr) "🔒 الوصول مقيد حصرياً برمز الأمان السري (Access Code: 308380)." else "🔒 Strictly restricted by secret access code (308380).",
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. NAME FIELD
                    OutlinedTextField(
                        value = regFullName,
                        onValueChange = { regFullName = it },
                        label = { Text(if (isAr) "الاسم بالكامل (Name)" else "Full Name") },
                        placeholder = { Text(if (isAr) "مثال: يوسف عبد الودود أو اسم النشاط" else "e.g. John Doe") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ElectricCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = NavyCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_name_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. EMAIL FIELD
                    OutlinedTextField(
                        value = regEmail,
                        onValueChange = { regEmail = it },
                        label = { Text(if (isAr) "البريد الإلكتروني (Email)" else "Email Address") },
                        placeholder = { Text("example@domain.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ElectricCyan) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = NavyCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. PHONE FIELD
                    OutlinedTextField(
                        value = regPhone,
                        onValueChange = { regPhone = it },
                        label = { Text(if (isAr) "رقم الهاتف / الواتساب (Phone)" else "Phone / WhatsApp Number") },
                        placeholder = { Text("01xxxxxxxxx") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ElectricCyan) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = NavyCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_phone_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 4. AGE FIELD
                    OutlinedTextField(
                        value = regAge,
                        onValueChange = { input ->
                            // Allow only digits
                            val cleanDigits = input.filter { it.isDigit() }
                            if (cleanDigits.length <= 3) {
                                regAge = cleanDigits
                            }
                        },
                        label = { Text(if (isAr) "العمر بالسنوات (Age)" else "Age (Years)") },
                        placeholder = { Text(if (isAr) "مثال: 28" else "e.g. 28") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = ElectricCyan) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = NavyCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_age_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5. PASSWORD FIELD
                    OutlinedTextField(
                        value = regPassword,
                        onValueChange = { regPassword = it },
                        label = { Text(if (isAr) "كلمة المرور (Password)" else "Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricCyan) },
                        trailingIcon = {
                            IconButton(onClick = { isRegPasswordVisible = !isRegPasswordVisible }) {
                                Icon(
                                    imageVector = if (isRegPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = ElectricCyan
                                )
                            }
                        },
                        visualTransformation = if (isRegPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = NavyCard,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_field")
                    )

                    // 6. GM SECRET KEY / ACCESS CODE (Visible when GM role selected)
                    AnimatedVisibility(visible = regRole == UserRole.GENERAL_MANAGER) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = regGmCode,
                                onValueChange = { regGmCode = it },
                                label = { Text(if (isAr) "رمز الأمان السري للمدير العام (Secret Access Code)" else "GM Secret Access Code (308380)") },
                                placeholder = { Text("308380") },
                                leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = AmberGold) },
                                trailingIcon = {
                                    IconButton(onClick = { isRegGmCodeVisible = !isRegGmCodeVisible }) {
                                        Icon(
                                            imageVector = if (isRegGmCodeVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = AmberGold
                                        )
                                    }
                                },
                                visualTransformation = if (isRegGmCodeVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AmberGold,
                                    unfocusedBorderColor = AmberGold.copy(alpha = 0.5f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_gm_secret_field")
                            )

                            Text(
                                text = if (isAr)
                                    "🔒 هذا الدور مخصص للأستاذ Youssef Johnny ومقيد برمز الدخول السري (308380)."
                                else
                                    "🔒 Restricted exclusively to Youssef Johnny via access code 308380.",
                                fontSize = 10.sp,
                                color = AmberGold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // SUBMIT BUTTON WITH COMPREHENSIVE VALIDATIONS
                    Button(
                        onClick = {
                            // Validation checks
                            val nameClean = regFullName.trim()
                            val emailClean = regEmail.trim()
                            val phoneClean = regPhone.trim()
                            val parsedAge = regAge.trim().toIntOrNull() ?: 0

                            if (nameClean.length < 2) {
                                statusMessage = if (isAr) "يرجى إدخال الاسم بالكامل بشكل صحيح" else "Please enter your full name"
                                isSuccessStatus = false
                                return@Button
                            }

                            if (!emailClean.contains("@") || !emailClean.contains(".")) {
                                statusMessage = if (isAr) "يرجى كتابة عنوان بريد إلكتروني صالح (example@domain.com)" else "Please enter a valid email address"
                                isSuccessStatus = false
                                return@Button
                            }

                            if (phoneClean.length < 7) {
                                statusMessage = if (isAr) "يرجى إدخال رقم هاتف صحيح للتواصل" else "Please enter a valid phone number"
                                isSuccessStatus = false
                                return@Button
                            }

                            if (parsedAge < 16 || parsedAge > 99) {
                                statusMessage = if (isAr) "يرجى إدخال عمر صحيح بين 16 و 99 عاماً" else "Please enter a valid age between 16 and 99"
                                isSuccessStatus = false
                                return@Button
                            }

                            if (regPassword.trim().length < 3) {
                                statusMessage = if (isAr) "كلمة المرور يجب أن لا تقل عن 3 خانات" else "Password must be at least 3 characters"
                                isSuccessStatus = false
                                return@Button
                            }

                            // Secret Key Validation for General Manager Youssef Johnny
                            if (regRole == UserRole.GENERAL_MANAGER) {
                                val normGmCode = MediaBuyerRepository.normalizeArabicDigitsAndText(regGmCode)
                                val normPass = MediaBuyerRepository.normalizeArabicDigitsAndText(regPassword)
                                if (normGmCode != MediaBuyerRepository.GM_SECRET_PASSCODE && normPass != MediaBuyerRepository.GM_SECRET_PASSCODE) {
                                    statusMessage = if (isAr)
                                        "رمز الأمان السري غير صالح! الوصول لصلاحيات المدير العام مقيد حصرياً للأستاذ 'Youssef Johnny' برمز الدخول (308380)."
                                    else
                                        "Invalid secret access code! General Manager access is strictly restricted to Youssef Johnny via access code 308380."
                                    isSuccessStatus = false
                                    return@Button
                                }
                            }

                            // Execute registration
                            val res = onRegister(nameClean, emailClean, phoneClean, regPassword, parsedAge, regRole, regGmCode)
                            if (res.first && regRole == UserRole.SUPERVISOR) {
                                // Trigger Supervisor Pending Approval State
                                pendingApprovalSupervisor = if (isAr) {
                                    "تم استلام طلب تسجيل المشرف بنجاح باسم: ($nameClean).\n" +
                                    "البريد: $emailClean | الهاتف: $phoneClean | العمر: $parsedAge سنة.\n\n" +
                                    "الحساب حالياً في حالة (قيد المراجعة والاعتماد) بانتظار موافقة المدير العام Youssef Johnny لمنح الصلاحيات الإشرافية."
                                } else {
                                    "Supervisor application submitted successfully for: ($nameClean).\n" +
                                    "Email: $emailClean | Phone: $phoneClean | Age: $parsedAge.\n\n" +
                                    "Your account is in Pending Approval state waiting for General Manager Youssef Johnny's authorization."
                                }
                                statusMessage = null
                                isSuccessStatus = true
                            } else {
                                statusMessage = res.second
                                isSuccessStatus = res.first
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (regRole) {
                                UserRole.GENERAL_MANAGER -> AmberGold
                                UserRole.SUPERVISOR -> AmberGold
                                UserRole.CLIENT -> ElectricCyan
                            }
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("auth_submit_register_btn")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = NavyDeep)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (regRole) {
                                UserRole.GENERAL_MANAGER -> if (isAr) "التحقق من رمز الأمان وإنشاء حساب المدير العام 👑" else "Verify Secret Key & Register GM"
                                UserRole.SUPERVISOR -> if (isAr) "إرسال طلب تسجيل المشرف للاعتماد 🛡️" else "Submit Supervisor Application"
                                UserRole.CLIENT -> if (isAr) "تأكيد إنشاء الحساب وبدء الحملات 🚀" else "Register & Launch Campaigns"
                            },
                            color = NavyDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAr) "لديك حساب بالفعل؟" else "Already have an account?",
                            fontSize = 12.sp,
                            color = TextSecondaryDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "تسجيل الدخول" else "Sign In",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            modifier = Modifier.clickable {
                                selectedTab = 0
                                statusMessage = null
                                pendingApprovalSupervisor = null
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // WHATSAPP 24/7 CUSTOMER SUPPORT CARD (Dedicated button for number 01284575019)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E3A2F)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    WhatsAppHelper.openWhatsApp(context)
                }
                .testTag("whatsapp_support_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = Color(0xFF25D366),
                        shape = CircleShape,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "WhatsApp",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAr) "الدعم الفني المباشر عبر واتساب 💬" else "Direct WhatsApp Support 💬",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = if (isAr) "خدمة العملاء واعتماد الحسابات: ${WhatsAppHelper.PHONE_DISPLAY}" else "Customer Service: ${WhatsAppHelper.PHONE_DISPLAY}",
                            fontSize = 12.sp,
                            color = Color(0xFF25D366),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        WhatsAppHelper.openWhatsApp(context)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("whatsapp_open_btn")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "محادثة فورية عبر واتساب (${WhatsAppHelper.PHONE_DISPLAY})" else "Chat Now on WhatsApp",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
