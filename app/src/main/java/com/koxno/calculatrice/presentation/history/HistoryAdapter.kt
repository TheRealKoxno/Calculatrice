package com.koxno.calculatrice.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koxno.calculatrice.data.db.typeConverters.history
import com.koxno.calculatrice.databinding.HistoryItemBinding
import com.koxno.calculatrice.domain.entity.HistoryItem

class HistoryAdapter(
    private val onItemClicked: (HistoryItem) -> Unit,
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var data: List<HistoryItem> = emptyList()

    fun setData(data: List<HistoryItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = data[position]
        with(holder.bindings) {
            expression.text = item.expression
            result.text = item.result
            createdAt.text = history(item.createdAt)
            root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    class HistoryViewHolder(val bindings: HistoryItemBinding) :
        RecyclerView.ViewHolder(bindings.root)

}


