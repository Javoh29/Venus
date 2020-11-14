package com.range.venus.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.range.venus.R
import com.range.venus.data.db.entities.PaymentModel
import com.range.venus.databinding.ItemContractContainerBinding
import java.text.DecimalFormat

class ContractItemAdapter(private val listModel: List<PaymentModel>) : RecyclerView.Adapter<ContractItemAdapter.ContractViewHolder>() {

    class ContractViewHolder(val binding: ItemContractContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val numFormat = DecimalFormat("#,###.##")
        @SuppressLint("SetTextI18n")
        fun bindUI(model: PaymentModel) {
            binding.tvDate.text = model.sana
            if (model.turi == "0"){
                binding.tvSom.text = numFormat.format(model.summa.toInt())
                binding.frameLine.setBackgroundResource(R.drawable.view_frame_line_dark)
                binding.layout.setBackgroundResource(R.drawable.img_item_debit)
                binding.tvDate.setTextColor(binding.root.resources.getColor(R.color.colorDark))
                binding.tvSom.setTextColor(binding.root.resources.getColor(R.color.colorDark))
                binding.som.setTextColor(binding.root.resources.getColor(R.color.colorDark))
                binding.tvType.setTextColor(binding.root.resources.getColor(R.color.colorDark))
                binding.tvType.text = binding.root.context.getString(R.string.text_debitor)
            } else {
                binding.tvSom.text = "+" + numFormat.format(model.summa.toInt())
                binding.tvType.text = binding.root.context.getString(R.string.text_kreditor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractViewHolder {
        val binding: ItemContractContainerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_contract_container,
            parent,
            false
        )
        return ContractViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
        holder.bindUI(listModel[position])
    }

    override fun getItemCount(): Int {
        return listModel.size
    }
}