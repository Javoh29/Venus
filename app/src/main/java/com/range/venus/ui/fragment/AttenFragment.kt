package com.range.venus.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.db.entities.AttenModel
import com.range.venus.databinding.FragmentAttenBinding
import com.range.venus.ui.adapter.AttenAdapter
import com.range.venus.utils.lazyDeferred
import kotlinx.android.synthetic.main.fragment_atten.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class AttenFragment : Fragment(), KodeinAware, CoroutineScope {

    override val kodein by closestKodein()
    private val venusDao: VenusDao by instance()
    private lateinit var viewModel: TableViewModel
    private lateinit var binding: FragmentAttenBinding
    private lateinit var dialogDate: Dialog
    private val dateAndTime = Calendar.getInstance()
    private lateinit var job: Job

    @SuppressLint("SimpleDateFormat")
    private val simpleFormat: SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd")

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        job = Job()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_atten, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TableViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.setView(binding.root, venusDao)
        bindUI()
    }

    private fun bindUI() {
        recyclerAtten.layoutManager = LinearLayoutManager(context)
        dialogDate = Dialog(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
        dialogDate.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogDate.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogDate.setContentView(R.layout.dialog_date)

        val tvCalendar1: AppCompatTextView = dialogDate.findViewById(R.id.tvCalendar1)
        val tvCalendar2: AppCompatTextView = dialogDate.findViewById(R.id.tvCalendar2)
        dateAndTime.time = Date()
        dateAndTime.add(Calendar.MONTH, -1)
        tvCalendar1.text = simpleFormat.format(dateAndTime.time)
        tvCalendar2.text = simpleFormat.format(Date())
        loadDate(simpleFormat.format(dateAndTime.time), simpleFormat.format(Date()))
        viewModel.tvDate.value = "${
            tvCalendar1.text!!.subSequence(
                8,
                tvCalendar1.text.length
            )
        }.${tvCalendar1.text!!.subSequence(
            5,
            tvCalendar1.text.length-3
        )} - ${tvCalendar2.text!!.subSequence(8, tvCalendar2.text.length)}.${tvCalendar2.text!!.subSequence(5, tvCalendar2.text.length-3)}"

        tvCalendar1.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                pickerStart,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        tvCalendar2.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                pickerEnd,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        dialogDate.findViewById<AppCompatButton>(R.id.btnCancel)
            .setOnClickListener { dialogDate.dismiss() }
        dialogDate.findViewById<AppCompatButton>(R.id.btnOk).setOnClickListener {
            dialogDate.dismiss()
            viewModel.tvDate.value = "${
                tvCalendar1.text!!.subSequence(
                    8,
                    tvCalendar1.text.length
                )
            }.${tvCalendar1.text!!.subSequence(
                5,
                tvCalendar1.text.length-3
            )} - ${tvCalendar2.text!!.subSequence(8, tvCalendar2.text.length)}.${tvCalendar2.text!!.subSequence(5, tvCalendar2.text.length-3)}"
            loadDate(tvCalendar1.text.toString(), tvCalendar2.text.toString())
        }

        viewModel.message.observe(viewLifecycleOwner, {
            if (it != null) {
                Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.isOpen.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it) {
                    dialogDate.show()
                } else dialogDate.dismiss()
            }
        })
    }

    private fun loadDate(date1: String, date2: String) = launch {
        val d1 = "$date1 00:00:01"
        val d2 = "$date2 23:59:59"
        lazyDeferred { venusDao.getAtten(d1, d2) }.value.await().observe(viewLifecycleOwner, {
            if (it != null) {
                bindList(it)
            } else return@observe
        })
    }

    private fun bindList(list: List<AttenModel>) {
        recyclerAtten.adapter = AttenAdapter(list)

    }

    @SuppressLint("SetTextI18n")
    private val pickerStart: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
            var moth = ""
            var day = ""
            try {
                moth = if (i2 < 9) {
                    "0${i2 + 1}."
                } else {
                    "${i2 + 1}."
                }
                day = if (i3 < 10) {
                    "0${i3}"
                } else {
                    "$i3"
                }
            } catch (e: Exception) {
            }
            dialogDate.findViewById<AppCompatTextView>(R.id.tvCalendar1).text = "${i}.$moth$day"
        }

    @SuppressLint("SetTextI18n")
    private val pickerEnd: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
            var moth = ""
            var day = ""
            try {
                moth = if (i2 < 9) {
                    "0${i2 + 1}."
                } else {
                    "${i2 + 1}."
                }
                day = if (i3 < 10) {
                    "0${i3}"
                } else {
                    "$i3"
                }
            } catch (e: Exception) {
            }
            dialogDate.findViewById<AppCompatTextView>(R.id.tvCalendar2).text = "${i}.$moth$day"
        }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}