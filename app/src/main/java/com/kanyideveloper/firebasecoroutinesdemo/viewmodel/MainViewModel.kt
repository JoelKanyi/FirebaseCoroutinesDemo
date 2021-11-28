package com.kanyideveloper.firebasecoroutinesdemo.viewmodel

import android.util.Patterns
import androidx.lifecycle.*
import com.google.firebase.auth.AuthResult
import com.kanyideveloper.firebasecoroutinesdemo.repository.MainRepository
import com.kanyideveloper.firebasecoroutinesdemo.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _registerStatus = MutableLiveData<Resource<AuthResult>>()
    val registerStatus: LiveData<Resource<AuthResult>> = _registerStatus

    private val _loginStatus = MutableLiveData<Resource<AuthResult>>()
    val loginStatus: LiveData<Resource<AuthResult>> = _loginStatus

    private val repository = MainRepository()


    fun registerUser(
        name: String,
        email: String,
        phone: String,
        password: String
    ) {
        var error =
            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                "Empty Strings"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "Not a valid Email"
            } else null

        error?.let {
            _registerStatus.postValue(Resource.Error(it))
            return
        }
        _registerStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.register(name, email, phone, password)
            _registerStatus.postValue(result)
        }
    }

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginStatus.postValue(Resource.Error("Empty Strings"))
        } else {
            _loginStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val result = repository.login(email, password)
                _loginStatus.postValue(result)
            }
        }
    }
}
