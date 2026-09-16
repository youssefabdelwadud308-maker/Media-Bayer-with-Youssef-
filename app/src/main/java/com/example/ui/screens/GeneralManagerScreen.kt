package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralManagerScreen(
    currentGmUser: User,
    users: List<User>,
    campaigns: List<Campaign>,
    longTermServices: List<LongTermService>,
    platformIntegrations: List<PlatformIntegrationConfig>,
    gateways: List<PaymentGatewayConfig>,
    developerPrompts: List<DeveloperPromptAction>,
    isGlobalAiActive: Boolean,
    currentLanguage: String,
    onExecuteGmPrompt: (prompt: String) -> Unit,
    onToggleGlobalAi: () -> Unit,
    onApproveSupervisor: (supervisorId: String) -> Unit,
    onRejectSupervisor: (supervisorId: String) -> Unit,
    onUpdateSupervisorPermissions: (supervisorId: String, perms: SupervisorPermissions) -> Unit,
    onAddLongTermService: (service: LongTermService) -> Unit,
    onDeleteLongTermService: (serviceId: String) -> Unit,
    onTogglePlatform: (platformId: String) -> Unit,
    onAddPlatform: (key: String, nameAr: String, nameEn: String) -> Unit,
    onUpdateGateway: (config: PaymentGatewayConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = currentLanguage == "ar"

    // Security check: If not GM, show strict access denied!
    if (currentGmUser.role != UserRole.GENERAL_MANAGER) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(NavyDeep)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isAr) "منطقة محظورة - لوحة التحكم الحصرية للمدير العام" else "Restricted Area - General Manager Only",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CrimsonRed
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isAr)
                            "هذه الشاشة محمية بأعلى درجات التشفير ولا يحق لأي مشرف أو عميل الاطلاع عليها، وهي مخصصة فقط للمدير العام (Youssef Johnny 👑)."
                        else
                            "Strictly isolated for General Manager Youssef Johnny. Supervisors and clients are prohibited from viewing this console.",
                        fontSize = 12.sp,
                        color = TextSecondaryDark
                    )
                }
            }
        }
        return
    }

    var activeTab by remember { mutableStateOf(0) } // 0: AI Prompt Console, 1: Supervisors, 2: Services, 3: Platforms & Gateways
    var promptInputText by remember { mutableStateOf("") }
    var showAddServiceDialog by remember { mutableStateOf(false) }
    var showAddPlatformDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Manager Golden Crown Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AmberGold)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = AmberGold.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(modifier = Modifier.padding(10.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = AmberGold,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isAr) "لوحة التحكم العليا - المدير العام" else "Command Center - General Manager",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "👑 " + if (isAr) "Youssef Johnny (كل الصلاحيات الإدارية والتقنية)" else "Youssef Johnny (Full Administrative Authority)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGold
                                )
                            }
                        }

                        // Badge
                        Surface(
                            color = AmberGold,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "VIP GM",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isAr)
                            "هذه اللوحة خاصة بك وحدك يا أستاذ Youssef Johnny؛ تتيح لك تعديل كود وتكوين التطبيق عبر التحدث المباشر مع الذكاء الاصطناعي، اعتماد المشرفين، إدارة الخدمات طويلة الأمد، وبوابات الدفع، مع إمكانية إيقاف الذكاء الاصطناعي بأي لحظة."
                        else
                            "Exclusive GM control panel: Prompt AI to live-patch features, approve supervisors, add long-term services, configure gateways, and control the AI autonomous engine.",
                        fontSize = 11.sp,
                        color = TextSecondaryDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Client Portal Link Copy Card for GM
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                    val clientUrl = "https://ais-pre-7kcdjlbcyfefsmbwzekmwk-421234073214.europe-west3.run.app"

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyDeep),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ElectricCyan.copy(alpha = 0.4f))),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Share, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isAr) "رابط المنصة لمشاركته مع العملاء:" else "Client Portal Public URL:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ElectricCyan
                                    )
                                }
                                Button(
                                    onClick = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(clientUrl))
                                        android.widget.Toast.makeText(
                                            context,
                                            if (isAr) "تم نسخ رابط الموقع للعملاء بنجاح!" else "Client platform link copied!",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isAr) "نسخ الرابط" else "Copy", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = clientUrl,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NavyCard)
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }

                    // Global AI Autonomous Kill Switch
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyCard)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) "المتحكم العام بالذكاء الاصطناعي (AI Kill Switch)" else "Master AI Autonomous Switch",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isGlobalAiActive) (if (isAr) "الذكاء الاصطناعي يدير الحملات ذاتياً" else "AI running campaigns autonomously") else (if (isAr) "الذكاء الاصطناعي متوقف بقرارك" else "AI is stopped by GM"),
                                fontSize = 10.sp,
                                color = if (isGlobalAiActive) EmeraldGreen else CrimsonRed
                            )
                        }

                        Button(
                            onClick = onToggleGlobalAi,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isGlobalAiActive) CrimsonRed else EmeraldGreen
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("gm_master_ai_switch_btn")
                        ) {
                            Text(
                                text = if (isGlobalAiActive) (if (isAr) "إيقاف AI" else "Stop AI") else (if (isAr) "تشغيل AI" else "Start AI"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Pending Supervisors Alert for GM
        val pendingSupervisors = users.filter { it.role == UserRole.SUPERVISOR && !it.isApproved }
        if (pendingSupervisors.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = AmberGold.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AmberGold)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { activeTab = 1 }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(Icons.Default.NotificationImportant, contentDescription = null, tint = AmberGold, modifier = Modifier.size(26.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) "⚠️ لديك ${pendingSupervisors.size} طلب انضمام مشرفين جديد!" else "⚠️ ${pendingSupervisors.size} Pending Supervisor Request(s)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = AmberGold
                            )
                            Text(
                                text = if (isAr)
                                    "الحسابات متوقفة ولن تتمكن من الدخول إلا بعد موافقتك. اضغط هنا للاطلاع والاعتماد."
                                else
                                    "Accounts locked until approved. Tap to review and activate.",
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                        Button(
                            onClick = { activeTab = 1 },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(if (isAr) "مراجعة واعتماد" else "Review", color = NavyDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Sub Tabs Row
        item {
            ScrollableTabRow(
                selectedTabIndex = activeTab,
                containerColor = NavySurface,
                contentColor = ElectricCyan,
                edgePadding = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = { Text(if (isAr) "💬 تعديل التطبيق بالـ AI" else "AI App Modifier", fontSize = 11.sp) }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = {
                        Text(
                            text = if (isAr) {
                                if (pendingSupervisors.isNotEmpty()) "👥 المشرفين (${pendingSupervisors.size} معلّق) ⚠️" else "👥 إدارة المشرفين"
                            } else {
                                if (pendingSupervisors.isNotEmpty()) "👥 Supervisors (${pendingSupervisors.size}) ⚠️" else "Supervisors"
                            },
                            fontSize = 11.sp,
                            fontWeight = if (pendingSupervisors.isNotEmpty()) FontWeight.Bold else FontWeight.Normal,
                            color = if (pendingSupervisors.isNotEmpty()) AmberGold else Color.Unspecified
                        )
                    }
                )
                Tab(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    text = { Text(if (isAr) "⭐ الخدمات طويلة الأمد" else "Services", fontSize = 11.sp) }
                )
                Tab(
                    selected = activeTab == 3,
                    onClick = { activeTab = 3 },
                    text = { Text(if (isAr) "🌐 المنصات وبوابات الدفع" else "Platforms & Gateways", fontSize = 11.sp) }
                )
            }
        }

        // TAB 0: Interactive AI Prompt to Modify App Live
        if (activeTab == 0) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Terminal, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAr) "تحدث مع المطور والذكاء الاصطناعي لتعديل التطبيق في أي وقت:" else "Talk to AI Developer to Live-Modify App:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = if (isAr)
                                "اكتب أي ميزة ترغب بإضافتها أو تعديلها (مثل: تفعيل منصة جديدة، إضافة بوابة دفع، تعديل الصلاحيات) وسيتم تنفيذها فورياً."
                            else
                                "Ask the AI developer to patch or add any feature (e.g. new gateways, platforms, permission adjustments).",
                            fontSize = 11.sp,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = promptInputText,
                            onValueChange = { promptInputText = it },
                            placeholder = {
                                Text(
                                    if (isAr) "مثال: أضف بوابة دفع فوري جديدة أو فعل تكامل لينكد إن وإكس..." else "e.g. Add Fawry gateway or enable LinkedIn & X Ads integration...",
                                    fontSize = 11.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("gm_prompt_input"),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (promptInputText.isNotBlank()) {
                                    onExecuteGmPrompt(promptInputText)
                                    promptInputText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .align(Alignment.End)
                                .testTag("execute_gm_prompt_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "تنفيذ التعديل فوراً ⚡" else "Execute Live Patch ⚡",
                                color = NavyDeep,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = if (isAr) "سجل التعديلات والترقيات المنفذة للمدير العام:" else "Executed Modifications Log for GM:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            items(developerPrompts) { promptAction ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "طلب: \"${promptAction.requestPrompt}\"",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGold
                            )
                            Surface(
                                color = EmeraldGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = promptAction.executedActionType,
                                    fontSize = 9.sp,
                                    color = EmeraldGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = promptAction.responseSummary,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // TAB 1: Supervisor Approvals & Permissions
        if (activeTab == 1) {
            val supervisors = users.filter { it.role == UserRole.SUPERVISOR }

            item {
                Text(
                    text = if (isAr) "المشرفون (يتطلب كل حساب موافقة رسمية من المدير العام):" else "Supervisors (Strict GM Approval Required):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            items(supervisors) { supervisor ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (supervisor.isApproved) NavySurface else AmberGold.copy(alpha = 0.12f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = supervisor.fullName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${supervisor.email} | ${supervisor.phoneNumber}",
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }

                            Surface(
                                color = if (supervisor.isApproved) EmeraldGreen.copy(alpha = 0.2f) else CrimsonRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (supervisor.isApproved) (if (isAr) "معتمد ومفعل" else "Approved") else (if (isAr) "قيد انتظار موافقتك" else "Pending Approval"),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (supervisor.isApproved) EmeraldGreen else CrimsonRed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Permissions Toggles
                        Text(
                            text = if (isAr) "صلاحيات المشرف:" else "Supervisor Permissions:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = supervisor.permissions.canViewAnalytics,
                                onClick = {
                                    onUpdateSupervisorPermissions(
                                        supervisor.id,
                                        supervisor.permissions.copy(canViewAnalytics = !supervisor.permissions.canViewAnalytics)
                                    )
                                },
                                label = { Text(if (isAr) "رؤية التحليلات" else "Analytics", fontSize = 10.sp) }
                            )

                            FilterChip(
                                selected = supervisor.permissions.canEditCampaigns,
                                onClick = {
                                    onUpdateSupervisorPermissions(
                                        supervisor.id,
                                        supervisor.permissions.copy(canEditCampaigns = !supervisor.permissions.canEditCampaigns)
                                    )
                                },
                                label = { Text(if (isAr) "تعديل الحملات" else "Edit Ads", fontSize = 10.sp) }
                            )

                            FilterChip(
                                selected = supervisor.permissions.canAnswerSupport,
                                onClick = {
                                    onUpdateSupervisorPermissions(
                                        supervisor.id,
                                        supervisor.permissions.copy(canAnswerSupport = !supervisor.permissions.canAnswerSupport)
                                    )
                                },
                                label = { Text(if (isAr) "رد الدعم" else "Support", fontSize = 10.sp) }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Approve / Revoke Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (!supervisor.isApproved) {
                                Button(
                                    onClick = { onApproveSupervisor(supervisor.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("approve_sup_${supervisor.id}")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isAr) "اعتماد المشرف وتفعيله" else "Approve Supervisor", color = NavyDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { onRejectSupervisor(supervisor.id) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(if (isAr) "إلغاء الاعتماد وتجميد الحساب" else "Suspend Supervisor", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: Long-Term Services Management (Add / Edit / Delete)
        if (activeTab == 2) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isAr) "إدارة الخدمات طويلة الأمد (إضافة / تعديل / حذف):" else "Long-Term Services Manager:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Button(
                        onClick = { showAddServiceDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("add_new_service_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAr) "إضافة خدمة جديدة" else "Add Service", color = NavyDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(longTermServices) { service ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
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

                        Text(
                            text = if (isAr) "المدة: ${service.durationMonths} أشهر" else "Duration: ${service.durationMonths} months",
                            fontSize = 11.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { onDeleteLongTermService(service.id) },
                                colors = ButtonDefaults.textButtonColors(contentColor = CrimsonRed)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isAr) "حذف الخدمة" else "Delete", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // TAB 3: Platforms & Gateways
        if (activeTab == 3) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isAr) "منصات التواصل والتجارة الإلكترونية المتصلة:" else "Social & E-Commerce Integrations:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Button(
                        onClick = { showAddPlatformDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("add_platform_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAr) "إضافة منصة جديدة" else "Add Platform", color = NavyDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(platformIntegrations) { platform ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = if (isAr) platform.nameAr else platform.nameEn,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = platform.apiStatus,
                                fontSize = 11.sp,
                                color = if (platform.isEnabled) EmeraldGreen else CrimsonRed
                            )
                        }

                        Switch(
                            checked = platform.isEnabled,
                            onCheckedChange = { onTogglePlatform(platform.id) },
                            colors = SwitchDefaults.colors(checkedThumbColor = ElectricCyan)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isAr) "تكوين بوابات الدفع المصرية:" else "Egyptian Payment Gateways Config:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            items(gateways) { gw ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) gw.type.titleAr else gw.type.titleEn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGold
                            )
                            Text(
                                text = gw.walletNumberOrAccount,
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }

                        Switch(
                            checked = gw.isEnabled,
                            onCheckedChange = { onUpdateGateway(gw.copy(isEnabled = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldGreen)
                        )
                    }
                }
            }
        }
    }

    // Add Long Term Service Dialog
    if (showAddServiceDialog) {
        var newTitleAr by remember { mutableStateOf("") }
        var newTitleEn by remember { mutableStateOf("") }
        var newDescAr by remember { mutableStateOf("") }
        var newPriceText by remember { mutableStateOf("25000") }
        var newMonthsText by remember { mutableStateOf("6") }

        AlertDialog(
            onDismissRequest = { showAddServiceDialog = false },
            title = { Text(if (isAr) "إضافة خدمة طويلة الأمد جديدة" else "Add Long-Term Service") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newTitleAr,
                        onValueChange = { newTitleAr = it },
                        label = { Text(if (isAr) "اسم الخدمة بالعربية" else "Service Title (AR)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTitleEn,
                        onValueChange = { newTitleEn = it },
                        label = { Text(if (isAr) "اسم الخدمة بالإنجليزية" else "Service Title (EN)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDescAr,
                        onValueChange = { newDescAr = it },
                        label = { Text(if (isAr) "الوصف والمميزات" else "Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPriceText,
                        onValueChange = { newPriceText = it },
                        label = { Text(if (isAr) "السعر بالجنيه المصري" else "Price in EGP") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val price = newPriceText.toDoubleOrNull() ?: 20000.0
                        val months = newMonthsText.toIntOrNull() ?: 6
                        val newService = LongTermService(
                            id = "lts_${System.currentTimeMillis()}",
                            titleAr = newTitleAr.ifBlank { "باقة إعلانية مخصصة طويلة الأمد" },
                            titleEn = newTitleEn.ifBlank { "Custom Long-Term Package" },
                            descriptionAr = newDescAr.ifBlank { "إدارة شاملة وتحسين مستمر للمبيعات والميزانيات." },
                            descriptionEn = "Comprehensive advertising management.",
                            durationMonths = months,
                            priceEgp = price,
                            featuresAr = listOf("مراقبة 24 ساعة", "تحسين A/B فوري", "تقارير PDF دورية"),
                            featuresEn = listOf("24/7 Monitoring", "Instant A/B optimization", "Periodic PDF Reports")
                        )
                        onAddLongTermService(newService)
                        showAddServiceDialog = false
                    }
                ) {
                    Text(if (isAr) "إضافة وحفظ" else "Save Service")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddServiceDialog = false }) {
                    Text(if (isAr) "إلغاء" else "Cancel")
                }
            }
        )
    }

    // Add Platform Dialog
    if (showAddPlatformDialog) {
        var pKey by remember { mutableStateOf("") }
        var pNameAr by remember { mutableStateOf("") }
        var pNameEn by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddPlatformDialog = false },
            title = { Text(if (isAr) "إضافة تكامل منصة إعلانية جديدة" else "Add New Ad Platform Integration") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = pNameAr,
                        onValueChange = { pNameAr = it },
                        label = { Text(if (isAr) "اسم المنصة بالعربية (مثال: بنترست للأعمال)" else "Platform Name (AR)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = pNameEn,
                        onValueChange = { pNameEn = it },
                        label = { Text(if (isAr) "اسم المنصة بالإنجليزية (e.g. Pinterest Ads)" else "Platform Name (EN)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val key = if (pKey.isBlank()) "p_${System.currentTimeMillis()}" else pKey
                        onAddPlatform(key, pNameAr.ifBlank { "منصة إعلانية مخصصة" }, pNameEn.ifBlank { "Custom Ad Platform" })
                        showAddPlatformDialog = false
                    }
                ) {
                    Text(if (isAr) "تفعيل المنصة" else "Enable Integration")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPlatformDialog = false }) {
                    Text(if (isAr) "إلغاء" else "Cancel")
                }
            }
        )
    }
}
