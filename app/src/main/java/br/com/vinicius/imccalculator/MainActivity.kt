package br.com.vinicius.imccalculator

import android.content.Context
import android.os.Bundle
import android.text.*
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val viewWeight by lazy { findViewById<EditText>(R.id.et_weight) }
    private val viewHeight by lazy { findViewById<EditText>(R.id.et_height) }
    private val viewButton by lazy { findViewById<Button>(R.id.btn_calculate) }
    private val viewResult by lazy { findViewById<TextView>(R.id.tv_result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewButton.setOnClickListener {
            if (isWeightValid() && isHeightValid()) {
                val weight = viewWeight.text.toString().toDouble()
                val height = viewHeight.text.toString().toDouble()
                val heightSquared = height * height
                val total = weight / heightSquared
                viewResult.text = stylizeText(
                    "O resultado do IMC é ${total.roundDouble()}",
                    total.roundDouble(),
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple_200) )
                )
                hideKeyboard()
                clearFields()
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        viewHeight.setText("")
        viewWeight.setText("")
        viewHeight.clearFocus()
        viewWeight.clearFocus()
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun isWeightValid() =
        viewWeight.text.isNotBlank() && viewWeight.text.isNotEmpty() && viewWeight.text.toString()
            .toDouble() in MINIMUM_WEIGHT..MAXIMUM_WEIGHT

    private fun isHeightValid() =
        viewHeight.text.isNotBlank() && viewHeight.text.isNotEmpty() && viewHeight.text.toString()
            .toDouble() in MINIMUM_HEIGHT..MAXIMUM_HEIGHT

    private fun Double.roundDouble() = String.format("%.2f", this)

    private fun stylizeText(
        textClear: String,
        textToStylize: String,
        textStyle: CharacterStyle
    ): SpannableString {
        val spannable = SpannableString(textClear)
        val textStyleBegin = textClear.indexOf(textToStylize)
        val textStyleEnd = textStyleBegin + textToStylize.length
        spannable.setSpan(textStyle, textStyleBegin, textStyleEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    companion object {
        private const val MINIMUM_WEIGHT = 0.2
        private const val MAXIMUM_WEIGHT = 500.0
        private const val MINIMUM_HEIGHT = 0.1
        private const val MAXIMUM_HEIGHT = 3.0
    }
}