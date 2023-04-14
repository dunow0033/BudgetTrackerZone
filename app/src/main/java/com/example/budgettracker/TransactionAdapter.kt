package com.example.budgettracker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.databinding.TransactionLayoutBinding

class TransactionAdapter(private var transactions: List<Transaction>)
    : RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        return TransactionHolder(
            TransactionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.binding.amount.context

        if(transaction.amount >= 0) {
            holder.binding.amount.text = "+ $%.2f".format(transaction.amount)
            holder.binding.amount.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            holder.binding.amount.text = "- $%.2f".format(Math.abs(transaction.amount))
            holder.binding.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        holder.binding.label.text = transaction.label

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("transaction", transaction)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    class TransactionHolder(val binding: TransactionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.label.text = transaction.label
            binding.amount.text = transaction.amount.toString()
        }
    }

    fun setData(transactions: List<Transaction>){
        this.transactions = transactions
    }
}