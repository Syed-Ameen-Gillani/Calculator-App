package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.Ans
import kotlinx.android.synthetic.main.activity_main.backspace
import kotlinx.android.synthetic.main.activity_main.errorBox
import kotlinx.android.synthetic.main.activity_main.inputDisplay
import kotlinx.android.synthetic.main.activity_main.keyDot
import kotlinx.android.synthetic.main.activity_main.resultDisplay
import kotlinx.android.synthetic.main.activity_main.keyEqualTo
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {
    private var validDot = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyDot.setOnClickListener {
            if(inputDisplay.text.isEmpty() || validDot)
            {
                inputDisplay.append(".")
            }
            validDot = false
        }
        Ans.setOnClickListener {
            inputDisplay.text = "${inputDisplay.text}${resultDisplay.text}"
            validDot = !resultDisplay.text.contains('.')
        }
        backspace.setOnClickListener {
            if(inputDisplay.text.isEmpty() || inputDisplay.text.length == 1){inputDisplay.text=""}
            else{
                var secondLastChar = inputDisplay.text[inputDisplay.text.length - 2]
                when( inputDisplay.text.last()) {
                    '.' -> {
                        validDot = true
                        inputDisplay.text = inputDisplay.text.dropLast(1)
                    }
                    '+', '-', '*', '/' -> {
                        if (secondLastChar.isDigit()) {
                           validDot = false
                            inputDisplay.text = inputDisplay.text.dropLast(1)}
                    }
                    else -> {if(inputDisplay.text.last().isDigit()){inputDisplay.text = inputDisplay.text.dropLast(1)}
                    }

                }
            }

        }
        fun reset() { inputDisplay.text = ""
            errorBox.text="" }
        keyEqualTo.setOnClickListener {
            try {
                val expression = ExpressionBuilder(inputDisplay.text.toString()).build()
                val result = expression.evaluate()

                // Check if the result is not null before further processing
                if (result != null) {
                    val longResult = result.toLong()

                    if (result == longResult.toDouble()) {
                        resultDisplay.text = longResult.toString()
                        reset()
                    } else {
                        resultDisplay.text = result.toString()
                        reset()
                    }
                } else {
                    // Handle the case where the expression evaluation resulted in a null value
                    resultDisplay.text = ""
                }
            } catch (e: Exception) {
                errorBox.text = e.message
            }
        }
    }

    fun clearAll(view: View) {
        inputDisplay.text = ""
        resultDisplay.text = ""
        errorBox.text = ""
        validDot = true
    }
    fun operatorAction(view: View) {
        if (inputDisplay.text.isEmpty() && view is Button)
        {
            when(view.text)
            {
                "+"->inputDisplay.append(view.text)
                "-"->inputDisplay.append(view.text)
            }
        }
        else if(inputDisplay.text.isNotEmpty() && view is Button)
        {
            when(inputDisplay.text.last()){
                '+','-','/','*','.'-> inputDisplay.text = inputDisplay.text
                else -> {
                    inputDisplay.append(view.text)
                    validDot = true
                }
            }

        }
    }
    fun keyAction(view: View) {
        if(view is Button){inputDisplay.append(view.text)}
    }
}
