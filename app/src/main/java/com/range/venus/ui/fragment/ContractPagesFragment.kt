package com.range.venus.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.range.venus.R
import com.range.venus.ui.adapter.ContractItemAdapter
import kotlinx.android.synthetic.main.fragment_contract_pages.*

class ContractPagesFragment : Fragment() {

    private val MOTH = "sectionMoth"

    companion object {
        @JvmStatic
        fun newInstance(sectionNumber: String): ContractPagesFragment {
            return ContractPagesFragment().apply {
                arguments = Bundle().apply {
                    putString(MOTH, sectionNumber)
                }
            }
        }
    }

    private lateinit var viewModel: ContractPagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contract_pages, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContractPagesViewModel::class.java)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =  ContractItemAdapter(viewModel.list)
    }

}