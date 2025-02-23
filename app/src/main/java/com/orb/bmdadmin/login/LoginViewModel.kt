package com.orb.bmdadmin.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.orb.bmdadmin.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel (
    private val repository: AuthRepository = AuthRepository()
): ViewModel() {
    val currentUser = repository.currentUser
    val adminUidRef: CollectionReference = Firebase
        .firestore.collection("admin_control")

    val hasUser: Boolean
        get() = repository.hasUser()

    var loginScreenState by mutableStateOf(LoginScreenState())
        private set

    fun onUserNameChange(userName: String) {
        loginScreenState = loginScreenState.copy(userName = userName)
    }

    fun onPasswordChange(password: String) {
        loginScreenState = loginScreenState.copy(password = password)
    }

    fun onUserNameSignUpChange(userName: String) {
        loginScreenState = loginScreenState.copy(userNameSignUp = userName)
    }

    fun onPasswordSignUpChange(password: String) {
        loginScreenState = loginScreenState.copy(passwordSignUp = password)
    }

    fun onConfirmPasswordSignUpChange(password: String) {
        loginScreenState = loginScreenState.copy(confirmPasswordSignUp = password)
    }

    private fun validateLoginForm() =
        loginScreenState.userName.isNotBlank() &&
                loginScreenState.password.isNotBlank()

    private fun validateSignUpForm() =
        loginScreenState.userNameSignUp.isNotBlank() &&
                loginScreenState.passwordSignUp.isNotBlank() &&
                loginScreenState.confirmPasswordSignUp.isNotBlank()

    fun createUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateSignUpForm()) {
                throw IllegalArgumentException("email and password can not be empty")
            }
            loginScreenState = loginScreenState.copy(isLoading = true)
            if (loginScreenState.passwordSignUp != loginScreenState.confirmPasswordSignUp) {
                throw IllegalArgumentException("Password do not match")
            }
            loginScreenState = loginScreenState.copy(signUpError = null)
            repository.createUser(
                loginScreenState.userNameSignUp,
                loginScreenState.passwordSignUp
            ) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(context, "successful Login", Toast.LENGTH_SHORT).show()
                    loginScreenState = loginScreenState.copy(isSuccessLogIn = true)
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    loginScreenState = loginScreenState.copy(isSuccessLogIn = false)
                }
            }
        } catch (e: Exception) {
            loginScreenState = loginScreenState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginScreenState = loginScreenState.copy(isLoading = false)
        }
    }
    fun loginUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateLoginForm()) {
                throw IllegalArgumentException("email and password can not be empty")
            }
            loginScreenState = loginScreenState.copy(isLoading = true)
            loginScreenState = loginScreenState.copy(loginError = null)
            repository.login(
                loginScreenState.userName,
                loginScreenState.password
            ) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(context, "successful Login", Toast.LENGTH_SHORT).show()
                    loginScreenState = loginScreenState.copy(isSuccessLogIn = true)
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    loginScreenState = loginScreenState.copy(isSuccessLogIn = false)
                }
            }
        } catch (e: Exception) {
            loginScreenState = loginScreenState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginScreenState = loginScreenState.copy(isLoading = false)
        }
    }
    fun checkIfUserIsAdmin(
        repository: AuthRepository = AuthRepository(),
        onResult: (Boolean) -> Unit
    ) {
        val currentUser = repository.currentUser ?: return onResult(false)

        adminUidRef.document("code_generator")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val adminUid = document.getString("uid")
                    onResult(adminUid == currentUser.uid)
                } else {
                    onResult(false)
                }
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
    fun verifyOTP(
        repository: AuthRepository = AuthRepository(),
        context: Context,
        inputCode: String,
        onNavigateToHomePage: () -> Unit
    ) {
        adminUidRef.document("latest_code")
            .get()
            .addOnSuccessListener { document ->
                val correctCode = document.getString("code")
                if (correctCode == inputCode) {
                    // Delete the used code from FireStore
                    adminUidRef.document("latest_code").delete()
                        .addOnSuccessListener {
                            onNavigateToHomePage.invoke() // Grant access
                        }
                } else {
                    Toast.makeText(context, "Invalid 8-digit code", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching code", Toast.LENGTH_SHORT).show()
            }
    }
}

data class LoginScreenState(
    val userName: String = "",
    val password: String = "",
    val userNameSignUp: String = "",
    val passwordSignUp: String = "",
    val confirmPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogIn: Boolean = false,
    val signUpError: String? = null,
    val loginError: String? = null
)