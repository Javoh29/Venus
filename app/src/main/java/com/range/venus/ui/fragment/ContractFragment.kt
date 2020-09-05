package com.range.venus.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.databinding.FragmentContractBinding
import com.range.venus.ui.adapter.ContractPagerAdapter
import kotlinx.android.synthetic.main.fragment_contract.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ContractFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val venusDao: VenusDao by instance()
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
        viewModel = ViewModelProvider(this).get(ContractViewModel::class.java)
        viewModel.setView(binding.root, venusDao)
        binding.viewModel = viewModel
        bindUI()
    }

    private fun bindUI() {
        dialogLoading = Dialog(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setContentView(R.layout.dialog_data_download)
        dialogLoading.setCanceledOnTouchOutside(false)
        dialogLoading.setCancelable(false)
        dialogLoading.show()


        viewModel.listDate.observe(viewLifecycleOwner, {
            if (it != null) {
                viewPager.adapter = ContractPagerAdapter(it, childFragmentManager)
                tabLayout.setViewPager(viewPager)
                Handler(Looper.myLooper()!!).postDelayed({viewPager.setCurrentItem(it.size, true)}, 150)
                dialogLoading.dismiss()
            }
        })

        viewModel.dialogTop.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it){
                    dialogTop.visibility = View.VISIBLE
                    dialogTop.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.from_top)
                } else {
                    dialogTop.visibility = View.GONE
                    dialogTop.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom)
                }
            }
        })

        viewModel.message.observe(viewLifecycleOwner, {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
    }

}