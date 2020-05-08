package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    private lateinit var txtInput: TextView
    private var lastNumeric = false
    private var stateError = false
    private var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtInput = findViewById(R.id.txtInput)
    }

    fun onDigit(view: View) {
        if(stateError) {
            txtInput.text = (view as Button).text
            stateError = false
        } else {
            txtInput.append((view as Button).text)
        }
        lastNumeric = true
    }

    fun onDecimalPoint(view: View){
        if(lastNumeric&&!lastDot&&!stateError) {
            txtInput.append(".")
            lastDot = true
            lastNumeric = false
        }
    }

    fun onOperator(view: View) {
        if(lastNumeric&&!stateError) {
            txtInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onClear(view: View) {
        this.txtInput.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    fun onErase(view: View) {
        if(txtInput.text.length==1){
            txtInput.text = ""
        }
        if(txtInput.text.isNotEmpty()) {
            this.txtInput.text = this.txtInput.text.dropLast(1)
            if (this.txtInput.text.last() == '.') {
                lastNumeric = false
                lastDot = true
            }
            if (this.txtInput.text.last().isDigit()) {
                lastNumeric = true
                lastDot = false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun onEqual(view: View) {
        if(lastNumeric&&!stateError) {
            val txt = txtInput.text.toString()
            val expr = ExpressionBuilder(txt).build()
            try {
                val result = expr.evaluate()
                if(result != kotlin.math.floor(result)) {
                    txtInput.text = result.toString()
                    lastDot = true
                } else {
                    txtInput.text = result.toString().dropLast(2)
                    lastNumeric = true
                }
                lastDot = true
            } catch (ex: ArithmeticException) {
                txtInput.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}
