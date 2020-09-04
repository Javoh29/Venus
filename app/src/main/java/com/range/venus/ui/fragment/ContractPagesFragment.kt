package com.range.venus.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.db.entities.PaymentModel
import com.range.venus.ui.adapter.ContractItemAdapter
import com.range.venus.utils.lazyDeferred
import kotlinx.android.synthetic.main.fragment_contract_pages.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ContractPagesFragment : Fragment(R.layout.fragment_contract_pages), KodeinAware {

    private val MOTH = "sectionMoth"
    override val kodein by closestKodein()
    private val venusDao: VenusDao by instance()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadData(requireArguments().getString(MOTH)!!)
    }

    private fun loadData(date: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val list = lazyDeferred { venusDao.getPayment("%$date%") }.value.await()
            if (list != null) {
                bindUI(list)
            } else return@launch
        }
    }

    private fun bindUI(list: List<PaymentModel>) {
        recyclerView.adapter =  ContractItemAdapter(list)

    }

}