package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bignerdranch.android.geoquiz.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER_KEY = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding

    private var answerIsTrue : Boolean = false
    private val cheatViewModel : CheatViewModel by viewModels<CheatViewModel>()

    private val backButtonCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val result = Activity.RESULT_CANCELED
            val data = Intent().apply { putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.cheatWasUsed) }
            setResult(result, data)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_KEY, answerIsTrue)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding.showAnswerBtn.setOnClickListener { view : View ->
            val answerText = when {
                answerIsTrue -> R.string.cheat_true_answer
                else ->         R.string.cheat_false_answer
            }
//            val text = findViewById(R.id.answer_text_view)
            binding.answerTextView.setText(answerText)
            setAnswerShownResult(true)
            view.isEnabled = false
        }

        if (cheatViewModel.cheatWasUsed) {
            binding.answerTextView.setText(
                when {
                    answerIsTrue -> R.string.cheat_true_answer
                    else -> R.string.cheat_false_answer
                }
            )
            binding.showAnswerBtn.isEnabled = false
        }

        onBackPressedDispatcher.addCallback(this, backButtonCallback)

    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        if (!cheatViewModel.cheatWasUsed)
            cheatViewModel.cheatWasUsed = isAnswerShown
        val result = if (cheatViewModel.cheatWasUsed) Activity.RESULT_OK else Activity.RESULT_CANCELED
        val data = Intent().apply { putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.cheatWasUsed) }
        setResult(result, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean) : Intent {
            return Intent(packageContext, CheatActivity::class.java).putExtra(EXTRA_ANSWER_KEY, answerIsTrue)
        }
    }
}