package com.orb.bmdadmin.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.ktx.Firebase
import com.orb.bmdadmin.repository.AuthRepository
import com.orb.bmdadmin.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val storageRepository: StorageRepository
): ViewModel() {
    val currentUser = repository.currentUser
    private val database = Firebase.database.reference
    val adminControlRef = database.child("admin_control")

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
        onResult: (Boolean) -> Unit
    ) {
        val currentUser = Firebase.auth.currentUser ?: return onResult(false)

        adminControlRef.child("code_generator").get()
            .addOnSuccessListener { snapshot ->
                val adminUid = snapshot.child("uid").value.toString()
                onResult(adminUid == currentUser.uid)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun verifyOTP(
        context: Context,
        inputCode: Int,
        onNavigateToHomePage: () -> Unit
    ) {
        adminControlRef.child("latest_code").get()
            .addOnSuccessListener { snapshot ->
                val correctCode = snapshot.child("code").value

                if (correctCode == inputCode) {
                    // Remove code using transaction
                    adminControlRef.child("latest_code").runTransaction(object : Transaction.Handler {
                        override fun doTransaction(currentData: MutableData): Transaction.Result {
                            currentData.value = null
                            return Transaction.success(currentData)
                        }

                        override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                            if (committed) {
                                onNavigateToHomePage()
                            }
                        }
                    })
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