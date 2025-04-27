package com.orb.bmdadmin.data

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.orb.bmdadmin.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val repository: StorageRepository
) : ViewModel() {
    private val _otp = MutableStateFlow(0)
    val otp: StateFlow<Int> = _otp.asStateFlow()

    private val _currentUsername = MutableStateFlow("Username")
    val currentUsername: StateFlow<String> = _currentUsername

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized

    init {
        checkAdminAccess()
        getOTP()
        repository.user()?.let { user ->
            val userRef =
                repository.database.child("users").child(repository.getUserId()).child("username")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            _currentUsername.value =
                                snapshot.getValue(String::class.java) ?: "username"
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                        }
                    })
        }
    }

    fun updateUsername(newUsername: String) {
        repository.user()?.let { user ->
            val userRef = repository.database.child("users").child(user.uid).child("username")
            userRef.setValue(newUsername)
        }
    }

    fun verifyOTP(inputCode: Int, onResult: (Boolean) -> Unit) {
        repository.verifyOTP(inputCode, onResult)
    }

    fun generateCode(): Int = repository.generateRandomCode()

    fun checkAdminAccess() {
        repository.checkAdminAccess(
            onResult = {
                _isAuthorized.value = it
            }
        )
    }

    fun storeCodeInDB(code: Int) {
        repository.storeCodeInDB(code = code)
    }

    fun getOTP() {
        repository.getOTP(
            otp = {
                _otp.value = it
            }
        )
    }
}