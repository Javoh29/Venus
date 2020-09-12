package com.range.venus.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.db.entities.TableModel
import com.range.venus.ui.adapter.LessonsAdapter
import com.range.venus.ui.base.ScopedFragment
import com.range.venus.utils.lazyDeferred
import kotlinx.android.synthetic.main.fragment_table_pages.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class TablePagesFragment : ScopedFragment(R.layout.fragment_table_pages) {

    private val INDEX: String = "index"
    private val venusDao: VenusDao by instance()

    companion object {
        @JvmStatic
        fun newInstance(param: Int) =
            TablePagesFragment().apply {
                arguments = Bundle().apply {
                    putString(INDEX, param.toString())
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerTable.layoutManager = LinearLayoutManager(context)
        loadData()
    }

    private fun loadData() = launch {
        lazyDeferred { venusDao.getLessons(requireArguments().getString(INDEX)!!) }.value.await()
            .observeForever {
                if (it != null) {
                    bindUI(it)
                } else return@observeForever
            }
    }

    private fun bindUI(list: List<TableModel>) = launch(Dispatchers.Main) {
        recyclerTable.adapter = LessonsAdapter(list)
    }
}