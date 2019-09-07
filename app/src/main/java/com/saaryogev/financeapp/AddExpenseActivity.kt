package com.saaryogev.financeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private val amountText = findViewById<EditText>(R.id.amountText)
    private val paymentTypeText = findViewById<EditText>(R.id.paymentTypeText)
    private val paymentMethodText = findViewById<EditText>(R.id.paymentMethodText)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
    }

    fun saveExpenseForm(view: View) {
        val expense = Expense(amountText.text.toString().toDouble(), paymentTypeText.text.toString(), paymentMethodText.text.toString(), Date())
    }
}
