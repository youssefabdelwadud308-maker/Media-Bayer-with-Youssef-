package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AiMarketAnalystService
import com.example.data.MediaBuyerRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MediaBuyerViewModel(
    private val repository: MediaBuyerRepository = MediaBuyerRepository()
) : ViewModel() {

    val currentUser: StateFlow<User?> = repository.currentUser
    val users: StateFlow<List<User>> = repository.users
    val campaigns: StateFlow<List<Campaign>> = repository.campaigns
    val alerts: StateFlow<List<CampaignAlert>> = repository.alerts
    val gateways: StateFlow<List<PaymentGatewayConfig>> = repository.gateways
    val transactions: StateFlow<List<PaymentTransaction>> = repository.transactions
    val longTermServices: StateFlow<List<LongTermService>> = repository.longTermServices
    val platformIntegrations: StateFlow<List<PlatformIntegrationConfig>> = repository.platformIntegrations
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
    val developerPrompts: StateFlow<List<DeveloperPromptAction>> = repository.developerPrompts
    val faqs: StateFlow<List<FaqItem>> = repository.faqs
    val isGlobalAiAutonomousActive: StateFlow<Boolean> = repository.isGlobalAiAutonomousActive
    val currentLanguage: StateFlow<String> = repository.currentLanguage

    // Selected campaign for deep analysis / details
    private val _selectedCampaign = MutableStateFlow<Campaign?>(null)
    val selectedCampaign: StateFlow<Campaign?> = _selectedCampaign.asStateFlow()

    init {
        viewModelScope.launch {
            repository.campaigns.collect { list ->
                if (_selectedCampaign.value == null && list.isNotEmpty()) {
                    _selectedCampaign.value = list.first()
                }
            }
        }
    }

    fun selectCampaign(campaign: Campaign) {
        _selectedCampaign.value = campaign
    }

    fun getAnalysisForCurrentCampaign(): AiMarketAnalysis? {
        val camp = _selectedCampaign.value ?: repository.campaigns.value.firstOrNull() ?: return null
        return AiMarketAnalystService.generateAnalysis(camp)
    }

    fun initContext(context: android.content.Context) {
        repository.setContext(context)
    }

    fun register(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String = "123456",
        age: Int = 25,
        role: UserRole = UserRole.CLIENT,
        gmSecretCode: String = ""
    ): Pair<Boolean, String> {
        return repository.registerUser(fullName, email, phoneNumber, password, age, role, gmSecretCode)
    }

    fun login(identifier: String, password: String = ""): Pair<Boolean, String> {
        return repository.loginUser(identifier, password)
    }

    fun quickSwitchUser(userId: String, gmSecretCode: String = ""): Pair<Boolean, String> {
        return repository.quickSwitchUser(userId, gmSecretCode)
    }

    fun logout() {
        repository.logout()
    }

    fun toggleLanguage() {
        val next = if (repository.currentLanguage.value == "ar") "en" else "ar"
        repository.setLanguage(next)
    }

    fun addCampaign(
        title: String,
        platform: AdPlatform,
        goal: CampaignGoal,
        totalBudgetEgp: Double,
        dailyBudgetEgp: Double,
        days: Int,
        variantA: String,
        variantB: String,
        audience: String
    ) {
        repository.addCampaign(
            title = title,
            platform = platform,
            goal = goal,
            totalBudgetEgp = totalBudgetEgp,
            dailyBudgetEgp = dailyBudgetEgp,
            days = days,
            variantAHeadline = variantA,
            variantBHeadline = variantB,
            targetAudience = audience
        )
    }

    fun toggleCampaignStatus(campaignId: String): Boolean {
        return repository.toggleCampaignStatus(campaignId)
    }

    fun optimizeCampaign(campaignId: String): Campaign? {
        return repository.optimizeCampaignWithAi(campaignId)
    }

    fun addBudgetToCampaign(campaignId: String, amountEgp: Double): Boolean {
        return repository.addBudgetToCampaign(campaignId, amountEgp)
    }

    fun deleteCampaign(campaignId: String): Boolean {
        return repository.deleteCampaign(campaignId)
    }

    fun generateShareableReport(campaign: Campaign): String {
        return repository.generateShareableReportText(campaign)
    }

    fun getClientPortalUrl(): String {
        return MediaBuyerRepository.CLIENT_PORTAL_URL
    }

    fun toggleGlobalAi(user: User): Pair<Boolean, String> {
        return repository.toggleGlobalAiAutonomous(user)
    }

    fun approveSupervisor(supervisorId: String, gmUser: User): Boolean {
        return repository.updateSupervisorApproval(supervisorId, true, gmUser)
    }

    fun rejectSupervisor(supervisorId: String, gmUser: User): Boolean {
        return repository.updateSupervisorApproval(supervisorId, false, gmUser)
    }

    fun updateSupervisorPermissions(supervisorId: String, perms: SupervisorPermissions, gmUser: User): Boolean {
        return repository.updateSupervisorPermissions(supervisorId, perms, gmUser)
    }

    fun addLongTermService(service: LongTermService, gmUser: User): Boolean {
        return repository.addLongTermService(service, gmUser)
    }

    fun updateLongTermService(service: LongTermService, gmUser: User): Boolean {
        return repository.updateLongTermService(service, gmUser)
    }

    fun deleteLongTermService(serviceId: String, gmUser: User): Boolean {
        return repository.deleteLongTermService(serviceId, gmUser)
    }

    fun updateGatewayConfig(config: PaymentGatewayConfig, gmUser: User): Boolean {
        return repository.updateGatewayConfig(config, gmUser)
    }

    fun addCustomPaymentGateway(titleAr: String, titleEn: String, accountInfo: String, gmUser: User): Boolean {
        return repository.addCustomPaymentGateway(titleAr, titleEn, accountInfo, gmUser)
    }

    fun togglePlatform(platformId: String, gmUser: User): Boolean {
        return repository.togglePlatformIntegration(platformId, gmUser)
    }

    fun addPlatformIntegration(key: String, nameAr: String, nameEn: String, gmUser: User): Boolean {
        return repository.addPlatformIntegration(key, nameAr, nameEn, gmUser)
    }

    fun executeGmPrompt(prompt: String, gmUser: User): DeveloperPromptAction {
        return repository.executeGmPromptToModifyApp(prompt, gmUser)
    }

    fun submitPayment(amountEgp: Double, method: PaymentMethodType, ref: String, isAutoRenew: Boolean) {
        repository.submitPayment(amountEgp, method, ref, isAutoRenew)
    }

    fun sendChatMessage(text: String) {
        repository.sendChatMessage(text)
    }

    fun exportBackup(): String {
        return repository.createBackupSnapshot()
    }
}
