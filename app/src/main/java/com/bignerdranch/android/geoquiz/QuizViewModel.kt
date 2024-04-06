package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class QuizViewModel(private val savedStateHandle : SavedStateHandle) : ViewModel() {

    val questionsBank = listOf(
        Question(R.string.question_africa, false, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_london, true, false),
        Question(R.string.question_new_york, false, false),
        Question(R.string.question_milan, true, false),
        Question(R.string.question_osaka, false, false),
        Question(R.string.question_tokyo, true, false),
        Question(R.string.question_rome, true, false),
        Question(R.string.question_washington_dc, true, false)
    )
    private var currentQuestionIndex : Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val questionBankSize : Int
        get() = questionsBank.size

    val currentQuestionAnswer : Boolean
        get() = questionsBank[currentQuestionIndex].answer

    val currentQuestion : Question
        get() = questionsBank[currentQuestionIndex]

    val currentQuestionText : Int
        get() = questionsBank[currentQuestionIndex].textResId

    fun moveToNext() {
        currentQuestionIndex = Math.floorMod(currentQuestionIndex + 1, questionsBank.size)
    }

    fun moveToPrev() {
        currentQuestionIndex = Math.floorMod(currentQuestionIndex - 1, questionsBank.size)
    }

}