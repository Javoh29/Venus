package com.range.venus.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.databinding.FragmentTableBinding
import com.range.venus.ui.adapter.TablePagerAdapter
import kotlinx.android.synthetic.main.fragment_table.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TableFragment: Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val venusDao: VenusDao by instance()
    private lateinit var viewModel: TableViewModel
    private lateinit var binding: FragmentTableBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_table, container, false)
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
        val list = ArrayList<String>()
        list.addAll(resources.getStringArray(R.array.Weeks))
        viewPagerTable.adapter = TablePagerAdapter(list, childFragmentManager)
        tabLayout.setViewPager(viewPagerTable)

        viewModel.message.observe(viewLifecycleOwner, {
            if (it != null) {
                Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
            }
        })
    }

}