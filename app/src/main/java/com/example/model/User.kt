package com.example.model

enum class UserRole {
    CLIENT,
    SUPERVISOR,
    GENERAL_MANAGER
}

data class SupervisorPermissions(
    val canViewAnalytics: Boolean = true,
    val canEditCampaigns: Boolean = false,
    val canAnswerSupport: Boolean = true,
    val canManageBudgets: Boolean = false
)

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val age: Int,
    val role: UserRole,
    val password: String = "123456",
    val isApproved: Boolean = true, // Supervisors require GM approval
    val balanceEgp: Double = 0.0,
    val permissions: SupervisorPermissions = SupervisorPermissions(),
    val registeredAt: Long = System.currentTimeMillis()
)
