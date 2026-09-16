package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.UserRole
import com.example.ui.components.AppTopBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.util.WhatsAppHelper
import com.example.viewmodel.MediaBuyerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                MediaBuyerApp()
            }
        }
    }
}

enum class NavigationTab(val titleAr: String, val titleEn: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    DASHBOARD("الرئيسية", "Dashboard", Icons.Default.Dashboard),
    AI_MARKET("تحليل AI", "AI SWOT", Icons.Default.Psychology),
    WALLET("المحفظة", "Wallet", Icons.Default.AccountBalanceWallet),
    SUPPORT("الدعم 24/7", "Live Chat", Icons.Default.SupportAgent),
    REPORTS("التقارير والأمان", "Reports & FAQ", Icons.Default.Assessment),
    GENERAL_MANAGER("المدير العام 👑", "GM Center 👑", Icons.Default.WorkspacePremium)
}

@Composable
fun MediaBuyerApp(
    viewModel: MediaBuyerViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()
    val campaigns by viewModel.campaigns.collectAsStateWithLifecycle()
    val alerts by viewModel.alerts.collectAsStateWithLifecycle()
    val gateways by viewModel.gateways.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val longTermServices by viewModel.longTermServices.collectAsStateWithLifecycle()
    val platformIntegrations by viewModel.platformIntegrations.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val developerPrompts by viewModel.developerPrompts.collectAsStateWithLifecycle()
    val faqs by viewModel.faqs.collectAsStateWithLifecycle()
    val isGlobalAiActive by viewModel.isGlobalAiAutonomousActive.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val selectedCampaign by viewModel.selectedCampaign.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val isAr = currentLanguage == "ar"
    val layoutDir = if (isAr) LayoutDirection.Rtl else LayoutDirection.Ltr

    LaunchedEffect(Unit) {
        viewModel.initContext(context)
    }

    var currentTab by remember { mutableStateOf(NavigationTab.DASHBOARD) }
    var showCreateCampaignDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDir) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = NavyDeep
        ) {
            if (currentUser == null) {
                // Auth Screen (Login default & Registration)
                AuthScreen(
                    currentLanguage = currentLanguage,
                    onRegister = { name, email, phone, password, age, role, gmCode ->
                        viewModel.register(name, email, phone, password, age, role, gmCode)
                    },
                    onLogin = { identifier, password ->
                        viewModel.login(identifier, password)
                    },
                    onQuickSwitch = { userId ->
                        viewModel.quickSwitchUser(userId)
                    },
                    availableUsers = users
                )
            } else {
                val user = currentUser!!
                val isGm = user.role == UserRole.GENERAL_MANAGER

                Scaffold(
                    topBar = {
                        AppTopBar(
                            currentUser = user,
                            currentLanguage = currentLanguage,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onLogout = { viewModel.logout() },
                            onOpenProfileMenu = {}
                        )
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = {
                                WhatsAppHelper.openWhatsApp(context)
                            },
                            containerColor = Color(0xFF25D366),
                            contentColor = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("whatsapp_fab")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "WhatsApp",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "خدمة العملاء (01284575019)" else "WhatsApp Support",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = NavySurface,
                            contentColor = ElectricCyan,
                            modifier = Modifier.testTag("app_nav_bar")
                        ) {
                            val availableTabs = if (isGm) {
                                NavigationTab.values().toList()
                            } else {
                                NavigationTab.values().filter { it != NavigationTab.GENERAL_MANAGER }
                            }

                            availableTabs.forEach { tab ->
                                val isSelected = currentTab == tab
                                val isGmTab = tab == NavigationTab.GENERAL_MANAGER

                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { currentTab = tab },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.titleEn,
                                            tint = if (isSelected) (if (isGmTab) AmberGold else ElectricCyan) else TextSecondaryDark
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = if (isAr) tab.titleAr else tab.titleEn,
                                            fontSize = 9.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) (if (isGmTab) AmberGold else ElectricCyan) else TextSecondaryDark
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = if (isGmTab) AmberGold.copy(alpha = 0.2f) else ElectricCyan.copy(alpha = 0.2f)
                                    ),
                                    modifier = Modifier.testTag("tab_${tab.name}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (currentTab) {
                            NavigationTab.DASHBOARD -> {
                                ClientDashboardScreen(
                                    currentUser = user,
                                    campaigns = campaigns,
                                    alerts = alerts,
                                    currentLanguage = currentLanguage,
                                    onLaunchCampaignClick = { showCreateCampaignDialog = true },
                                    onNavigateToAiMarket = { currentTab = NavigationTab.AI_MARKET },
                                    onNavigateToPayments = { currentTab = NavigationTab.WALLET },
                                    onNavigateToSupport = { currentTab = NavigationTab.SUPPORT },
                                    onNavigateToReports = { currentTab = NavigationTab.REPORTS },
                                    onSelectCampaign = { camp ->
                                        viewModel.selectCampaign(camp)
                                        currentTab = NavigationTab.AI_MARKET
                                    },
                                    onToggleCampaign = { viewModel.toggleCampaignStatus(it) },
                                    onOptimizeCampaign = { viewModel.optimizeCampaign(it) },
                                    onAddBudget = { campId, amt -> viewModel.addBudgetToCampaign(campId, amt) },
                                    onDeleteCampaign = { viewModel.deleteCampaign(it) }
                                )
                            }
                            NavigationTab.AI_MARKET -> {
                                AiMarketAnalystScreen(
                                    currentUser = user,
                                    analysis = viewModel.getAnalysisForCurrentCampaign(),
                                    selectedCampaign = selectedCampaign,
                                    isGlobalAiActive = isGlobalAiActive,
                                    currentLanguage = currentLanguage,
                                    onToggleGlobalAi = { viewModel.toggleGlobalAi(user) }
                                )
                            }
                            NavigationTab.WALLET -> {
                                PaymentScreen(
                                    currentUser = user,
                                    gateways = gateways,
                                    transactions = transactions,
                                    currentLanguage = currentLanguage,
                                    onSubmitPayment = { amt, method, ref, autoRenew ->
                                        viewModel.submitPayment(amt, method, ref, autoRenew)
                                    }
                                )
                            }
                            NavigationTab.SUPPORT -> {
                                SupportChatScreen(
                                    currentUser = user,
                                    messages = chatMessages,
                                    currentLanguage = currentLanguage,
                                    onSendMessage = { viewModel.sendChatMessage(it) }
                                )
                            }
                            NavigationTab.REPORTS -> {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    var reportSubTab by remember { mutableStateOf(0) }
                                    TabRow(
                                        selectedTabIndex = reportSubTab,
                                        containerColor = NavySurface,
                                        contentColor = ElectricCyan
                                    ) {
                                        Tab(
                                            selected = reportSubTab == 0,
                                            onClick = { reportSubTab = 0 },
                                            text = { Text(if (isAr) "تقرير الأداء (PDF)" else "PDF Report", fontSize = 11.sp) }
                                        )
                                        Tab(
                                            selected = reportSubTab == 1,
                                            onClick = { reportSubTab = 1 },
                                            text = { Text(if (isAr) "الأمان والأسئلة الشائعة" else "Security & FAQ", fontSize = 11.sp) }
                                        )
                                    }

                                    if (reportSubTab == 0) {
                                        ReportScreen(
                                            currentUser = user,
                                            campaigns = campaigns,
                                            currentLanguage = currentLanguage
                                        )
                                    } else {
                                        FaqAndSecurityScreen(
                                            faqs = faqs,
                                            longTermServices = longTermServices,
                                            currentLanguage = currentLanguage,
                                            onExportBackup = { viewModel.exportBackup() }
                                        )
                                    }
                                }
                            }
                            NavigationTab.GENERAL_MANAGER -> {
                                GeneralManagerScreen(
                                    currentGmUser = user,
                                    users = users,
                                    campaigns = campaigns,
                                    longTermServices = longTermServices,
                                    platformIntegrations = platformIntegrations,
                                    gateways = gateways,
                                    developerPrompts = developerPrompts,
                                    isGlobalAiActive = isGlobalAiActive,
                                    currentLanguage = currentLanguage,
                                    onExecuteGmPrompt = { prompt ->
                                        viewModel.executeGmPrompt(prompt, user)
                                    },
                                    onToggleGlobalAi = {
                                        viewModel.toggleGlobalAi(user)
                                    },
                                    onApproveSupervisor = { supId ->
                                        viewModel.approveSupervisor(supId, user)
                                    },
                                    onRejectSupervisor = { supId ->
                                        viewModel.rejectSupervisor(supId, user)
                                    },
                                    onUpdateSupervisorPermissions = { supId, perms ->
                                        viewModel.updateSupervisorPermissions(supId, perms, user)
                                    },
                                    onAddLongTermService = { service ->
                                        viewModel.addLongTermService(service, user)
                                    },
                                    onDeleteLongTermService = { serviceId ->
                                        viewModel.deleteLongTermService(serviceId, user)
                                    },
                                    onTogglePlatform = { platformId ->
                                        viewModel.togglePlatform(platformId, user)
                                    },
                                    onAddPlatform = { key, ar, en ->
                                        viewModel.addPlatformIntegration(key, ar, en, user)
                                    },
                                    onUpdateGateway = { gw ->
                                        viewModel.updateGatewayConfig(gw, user)
                                    }
                                )
                            }
                        }

                        // Create Campaign Dialog
                        if (showCreateCampaignDialog) {
                            CreateCampaignDialog(
                                currentLanguage = currentLanguage,
                                onDismiss = { showCreateCampaignDialog = false },
                                onCreateCampaign = { title, platform, goal, total, daily, days, varA, varB, aud ->
                                    viewModel.addCampaign(title, platform, goal, total, daily, days, varA, varB, aud)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
