package com.autoai.readnotification.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.autoai.readnotification.models.SaveCustomeMessage

class MessageViewModel : ViewModel() {

    private val _uiState = MutableLiveData<SaveCustomeMessage>()
    val uiState : LiveData<SaveCustomeMessage> get() = _uiState

    init {
        retrieveMessages()
    }

    private fun retrieveMessages() {

    }
}