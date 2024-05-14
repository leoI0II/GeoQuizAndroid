package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle : SavedStateHandle) : ViewModel() {

    val questionsBank = listOf(
        Question(R.string.question_africa, false),
        Question(R.string.question_mideast, false),
        Question(R.string.question_oceans, true),
        Question(R.string.question_americas, true),
        Question(R.string.question_london, true),
        Question(R.string.question_new_york, false),
        Question(R.string.question_milan, true),
        Question(R.string.question_osaka, false),
        Question(R.string.question_tokyo, true),
        Question(R.string.question_rome, true),
        Question(R.string.question_washington_dc, true)
    )
    private var currentQuestionIndex : Int
        get() = savedStateHandle.get<Int>(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val questionBankSize : Int
        get() = questionsBank.size

    val currentQuestionAnswer : Boolean
        get() = questionsBank[currentQuestionIndex].answer

    val currentQuestion : Question
        get() = questionsBank[currentQuestionIndex]

    val currentQuestionText : Int
        get() = questionsBank[currentQuestionIndex].textResId

    var isCheater : Boolean
        get() = savedStateHandle.get<Boolean>(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    fun moveToNext() {
        currentQuestionIndex = Math.floorMod(currentQuestionIndex + 1, questionsBank.size)
    }

    fun moveToPrev() {
        currentQuestionIndex = Math.floorMod(currentQuestionIndex - 1, questionsBank.size)
    }

}