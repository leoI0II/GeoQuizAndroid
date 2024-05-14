package com.bignerdranch.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val CHEAT_USED_KEY = "CHEAT_USED_ID"
class CheatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var cheatWasUsed : Boolean
        get() = savedStateHandle.get<Boolean>(CHEAT_USED_KEY) ?: false
        set(value) = savedStateHandle.set(CHEAT_USED_KEY, value)

}