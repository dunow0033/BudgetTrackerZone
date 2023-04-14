package com.example.budgettracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.example.budgettracker.databinding.ActivityDetailedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedBinding

    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var transaction = intent.getSerializableExtra("transaction") as Transaction

        val labelLayout = binding.labelLayout
        val amountLayout = binding.amountLayout

        binding.labelInput.setText(transaction.label)
        binding.amountInput.setText(transaction.amount.toString())
        binding.descriptionInput.setText(transaction.description)

        binding.labelInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
            if(it!!.isNotEmpty()) {
                labelLayout.error = null
            }
        }

        binding.amountInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
            if(it!!.isNotEmpty()) {
                amountLayout.error = null
            }
        }

        binding.descriptionInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
        }

        binding.rootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.updateBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()
            val description = binding.descriptionInput.text.toString()

            if(label.isEmpty())
                labelLayout.error = "Please enter a valid label."
            else if(amount == null)
                amountLayout.error = "Please enter a valid amount."
            else {
                val transaction = Transaction(transaction.id, label, amount, description)
                update(transaction)
            }
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun update(transaction: Transaction) {
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}
