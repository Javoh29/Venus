package com.range.venus.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.range.venus.R
import com.range.venus.data.db.entities.AttenModel
import com.range.venus.databinding.ItemAttenContainerBinding

class AttenAdapter(private val listModel: List<AttenModel>) :
    RecyclerView.Adapter<AttenAdapter.AttenViewHolder>() {

    class AttenViewHolder(val binding: ItemAttenContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindUI(model: AttenModel) {
            binding.tvDate.text = "${model.kelganVaqti.substring(8, 10).toInt()}-${
                getMoth(
                    model.kelganVaqti.substring(
                        5,
                        7
                    ).toInt()
                )
            }\n${
                model.kelganVaqti.substring(
                    0,
                    4
                )
            } ${binding.root.context.getString(R.string.text_year)}"
            binding.tvInTime.text = model.kelganVaqti.substring(
                model.kelganVaqti.length - 8,
                model.kelganVaqti.length - 3
            )
            binding.tvOutTime.text = model.ketganVaqti.substring(
                model.ketganVaqti.length - 8,
                model.ketganVaqti.length - 3
            )
        }

        private fun getMoth(moth: Int): String {
            return when (moth) {
                1 -> binding.root.context.getString(R.string.text_yan)
                2 -> binding.root.context.getString(R.string.text_fev)
                3 -> binding.root.context.getString(R.string.text_mar)
                4 -> binding.root.context.getString(R.string.text_apr)
                5 -> binding.root.context.getString(R.string.text_may)
                6 -> binding.root.context.getString(R.string.text_jun)
                7 -> binding.root.context.getString(R.string.text_jul)
                8 -> binding.root.context.getString(R.string.text_avg)
                9 -> binding.root.context.getString(R.string.text_snt)
                10 -> binding.root.context.getString(R.string.text_okt)
                11 -> binding.root.context.getString(R.string.text_nyb)
                12 -> binding.root.context.getString(R.string.text_dek)
                else -> ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttenViewHolder {
        val binding: ItemAttenContainerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_atten_container,
            parent,
            false
        )
        return AttenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttenViewHolder, position: Int) {
        holder.bindUI(listModel[position])
    }

    override fun getItemCount(): Int {
        return listModel.size
    }
}