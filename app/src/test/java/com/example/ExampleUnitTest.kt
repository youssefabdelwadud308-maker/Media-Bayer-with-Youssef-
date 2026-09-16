package com.example

import com.example.data.MediaBuyerRepository
import com.example.model.UserRole
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: MediaBuyerRepository

    @Before
    fun setUp() {
        repository = MediaBuyerRepository()
    }

    @Test
    fun testClientRegistrationWithAllFields() {
        val result = repository.registerUser(
            fullName = "أحمد مصطفى",
            email = "ahmed.client@test.com",
            phoneNumber = "01012345678",
            password = "password123",
            age = 29,
            role = UserRole.CLIENT
        )

        assertTrue(result.first)
        val currentUser = repository.currentUser.value
        assertNotNull(currentUser)
        assertEquals("أحمد مصطفى", currentUser?.fullName)
        assertEquals(29, currentUser?.age)
        assertEquals(UserRole.CLIENT, currentUser?.role)
        assertTrue(currentUser?.isApproved == true)
    }

    @Test
    fun testSupervisorRegistrationEntersPendingApprovalState() {
        val result = repository.registerUser(
            fullName = "كريم المشرف",
            email = "karim.supervisor@test.com",
            phoneNumber = "01188776655",
            password = "pass123456",
            age = 32,
            role = UserRole.SUPERVISOR
        )

        assertTrue(result.first)
        assertTrue(result.second.contains("قيد المراجعة والاعتماد") || result.second.contains("Pending Approval"))

        // Unapproved supervisor cannot log in
        val loginResult = repository.loginUser("karim.supervisor@test.com", "pass123456")
        assertFalse(loginResult.first)
        assertTrue(loginResult.second.contains("قيد المراجعة والاعتماد") || loginResult.second.contains("Pending Approval"))
    }

    @Test
    fun testGeneralManagerAccessRestrictedWithoutSecretCode() {
        // Attempting to register or access GM with invalid code must fail
        val failedResult = repository.registerUser(
            fullName = "مخترق غير مصرح",
            email = "hacker@test.com",
            phoneNumber = "01000000000",
            password = "wrongpassword",
            age = 30,
            role = UserRole.GENERAL_MANAGER,
            gmSecretCode = "999999"
        )

        assertFalse(failedResult.first)
        assertTrue(failedResult.second.contains("رمز الأمان السري غير صحيح") || failedResult.second.contains("308380"))
    }

    @Test
    fun testGeneralManagerSecretKeyValidationForYoussefJohnny() {
        // Validating with Western digits 308380
        val loginResult = repository.loginUser("308380", "")
        assertTrue(loginResult.first)
        assertEquals(UserRole.GENERAL_MANAGER, repository.currentUser.value?.role)
        assertEquals("Youssef Johnny", repository.currentUser.value?.fullName)

        // Validating with Arabic digits ٣٠٨٣٨٠
        repository.logout()
        val loginArabicResult = repository.loginUser("٣٠٨٣٨٠", "")
        assertTrue(loginArabicResult.first)
        assertEquals(UserRole.GENERAL_MANAGER, repository.currentUser.value?.role)
    }
}
