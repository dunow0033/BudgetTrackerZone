package com.example.budgettracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.example.budgettracker.databinding.ActivityAddTransactionBinding
import com.example.budgettracker.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val labelLayout = binding.labelLayout
        val amountLayout = binding.amountLayout

        binding.labelInput.addTextChangedListener {
            if(it!!.isNotEmpty()) {
                labelLayout.error = null
            }
        }

        binding.amountInput.addTextChangedListener {
            if(it!!.isNotEmpty()) {
                amountLayout.error = null
            }
        }

        binding.addTransactionBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()
            val description = binding.descriptionInput.text.toString()

            if(label.isEmpty())
                labelLayout.error = "Please enter a valid label."
            else if(amount == null)
                amountLayout.error = "Please enter a valid amount."
            else {
                val transaction = Transaction(0, label, amount, description)
                insert(transaction)
            }
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun insert(transaction: Transaction) {
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}