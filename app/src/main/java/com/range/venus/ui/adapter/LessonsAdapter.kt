package com.range.venus.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.range.venus.R
import com.range.venus.data.db.entities.TableModel
import com.range.venus.databinding.ItemTableContainerBinding

class LessonsAdapter(private val listModel: List<TableModel>) :
    RecyclerView.Adapter<LessonsAdapter.LessonsViewHolder>() {

    class LessonsViewHolder(private val binding: ItemTableContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindUI(model: TableModel) {
            binding.tvLessonIndex.text = model.paraId
            binding.tvLessonName.text = model.darsNomi
            binding.tvHours.text = model.darsVaqti
            binding.tvClassroom.text = model.auditoriya + " " + binding.root.context.getString(R.string.text_room)
            binding.tvTeacher.text = model.oqituvchi

            when (model.darsTuri) {
                "1" -> {
                    binding.tvType.text = binding.root.context.getString(R.string.text_main_table)
                    binding.viewTableColor.setBackgroundResource(R.color.colorPrimaryDark)
                }
                "2" -> {
                    binding.tvType.text = binding.root.context.getString(R.string.text_deno_table)
                    binding.viewTableColor.setBackgroundResource(R.color.colorGreen)
                }
                "3" -> {
                    binding.tvType.text = binding.root.context.getString(R.string.text_numer_table)
                    binding.viewTableColor.setBackgroundResource(R.color.colorAccent)
                }
            }
            when (model.mashTuri) {
                "1" -> {
                    binding.tvLessonType.text =
                        binding.root.context.getString(R.string.text_lecture)
                }
                "2" -> {
                    binding.tvLessonType.text =
                        binding.root.context.getString(R.string.text_pracrice)
                }
                "3" -> {
                    binding.tvLessonType.text = binding.root.context.getString(R.string.text_lab)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        val binding: ItemTableContainerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_table_container,
            parent,
            false
        )
        return LessonsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        holder.bindUI(listModel[position])
    }

    override fun getItemCount(): Int {
        return listModel.size
    }
}