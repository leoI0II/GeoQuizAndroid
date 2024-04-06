package com.bignerdranch.android.geoquiz

import org.junit.jupiter.api.Assertions.*
import androidx.lifecycle.SavedStateHandle
import org.junit.Test

class QuizViewModelTest
{
    @Test
    fun providesExpectedQuestionText()
    {
        val savedStateHandle = SavedStateHandle()
        val quizVewModel = QuizViewModel(savedStateHandle)
        assertEquals(R.string.question_africa, quizVewModel.currentQuestionText)
    }

    @Test
    fun wrapsAroundQuestionBank()
    {
        val savedStateHandle = SavedStateHandle(mapOf(CURRENT_INDEX_KEY to 10))
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertEquals(R.string.question_washington_dc, quizViewModel.currentQuestionText)
        quizViewModel.moveToNext()
        assertEquals(R.string.question_africa, quizViewModel.currentQuestionText)
    }

    @Test
    fun checkFirstQuestionAnswer() {
        val savedStateHandle = SavedStateHandle()
        val quizVewModel = QuizViewModel(savedStateHandle)
        assertFalse(quizVewModel.currentQuestionAnswer)
    }
}