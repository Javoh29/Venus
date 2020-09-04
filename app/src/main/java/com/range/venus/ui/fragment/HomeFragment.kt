package com.range.venus.ui.fragment

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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.network.ApiService
import com.range.venus.data.pravider.UnitProvider
import com.range.venus.databinding.FragmentHomeBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val venusDao: VenusDao by instance()
    private val apiService: ApiService by instance()
    private val unitProvider: UnitProvider by instance()
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialogLoading: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.setView(binding.root, venusDao, apiService, unitProvider)
        bindUI()
    }

    private fun bindUI() {
        dialogLoading = Dialog(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setContentView(R.layout.dialog_download)
        dialogLoading.setCanceledOnTouchOutside(false)
        dialogLoading.setCancelable(false)

        viewModel.showDialog.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it) {
                    dialogLoading.show()
                } else {
                    dialogLoading.dismiss()
                }
            }
        })

        viewModel.message.observe(viewLifecycleOwner, {
            if (it != null && it != 0){
                Toast.makeText(context, getString(it), Toast.LENGTH_LONG).show()
            }
        })
    }

}