package com.range.venus.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
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
        val list: ArrayList<String> = ArrayList()
        list.add("Yanvar 2019")
        list.add("Fevral 2019")
        list.add("Mart 2019")
        list.add("Aprel 2019")
        list.add("May 2019")
        list.add("Iyun 2019")
        list.add("Iyul 2019")
        list.add("Avgust 2019")
        list.add("Sentyabr 2019")
        list.add("Oktyabr 2019")
        list.add("Noyabr 2019")
        list.add("Dekabr 2019")
        list.add("Yanvar")
        list.add("Fevral")
        list.add("Mart")
        list.add("Aprel")
        list.add("Iyun")
        list.add("Avgust")
        list.add("Sentyabr")
        viewPager.adapter = ContractPagerAdapter(list, childFragmentManager)
        viewPager.currentItem = list.size
        tabLayout.setViewPager(viewPager)
    }

}