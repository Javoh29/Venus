package com.range.venus.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.range.venus.R
import com.range.venus.data.model.ItemModel
import com.range.venus.databinding.ItemContractContainerBinding

class ContractItemAdapter(private val listModel: List<ItemModel>) : RecyclerView.Adapter<ContractItemAdapter.ContractViewHolder>() {

    class ContractViewHolder(val binding: ItemContractContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUI(model: ItemModel) {
            binding.tvBank.text = model.bankName
            binding.tvInfo.text = model.info
            binding.tvDate.text = model.date
            binding.tvSom.text = model.sum
            if (model.sum.substring(0,1) == "-"){
                binding.frameLine.setBackgroundResource(R.drawable.view_frame_line_dark)
                binding.layout.setBackgroundResource(R.drawable.img_item_debit)
                binding.tvBank.setTextColor(binding.root.resources.getColor(R.color.colorDark))
                binding.tvInfo.setTextColor(binding.root.resources.getColor(R.color.colorDark))
                binding.tvDate.setTextColor(binding.root.resources.getColor(R.color.colorDark))
                binding.tvSom.setTextColor(binding.root.resources.getColor(R.color.colorDark))
                binding.som.setTextColor(binding.root.resources.getColor(R.color.colorDark))
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