package com.range.venus.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.range.venus.R
import com.range.venus.databinding.FragmentContractBinding
import com.range.venus.ui.activity.LoginActivity
import com.range.venus.ui.activity.MainActivity
import com.range.venus.ui.adapter.ContractPagerAdapter
import kotlinx.android.synthetic.main.fragment_contract.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ContractFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: ContractViewModelFactory by instance()
    private lateinit var viewModel: ContractViewModel
    private lateinit var binding: FragmentContractBinding
    private lateinit var dialogLoading: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contract, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ContractViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.loadData()
        bindUI()
    }

    private fun bindUI() {
        dialogLoading = Dialog(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setContentView(R.layout.dialog_download)
        dialogLoading.setCanceledOnTouchOutside(false)
        dialogLoading.setCancelable(false)
        dialogLoading.show()

        viewModel.update.observe(viewLifecycleOwner, {
            if (it != null && it) {
                viewPager.adapter = ContractPagerAdapter(viewModel.listDate, childFragmentManager)
                tabLayout.setViewPager(viewPager)
                Handler(Looper.myLooper()!!).postDelayed({viewPager.setCurrentItem(viewModel.listDate.size, true)}, 200)
                dialogLoading.dismiss()
            }
        })

        viewModel.message.observe(viewLifecycleOwner, {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.blockUser.observe(viewLifecycleOwner, {
            if (it != null && it) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        })

        viewModel.changeLang.observe(viewLifecycleOwner, {
            if (it != null && it) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
        })
    }

}