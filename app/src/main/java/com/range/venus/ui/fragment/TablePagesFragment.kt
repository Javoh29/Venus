package com.range.venus.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import kotlinx.android.synthetic.main.fragment_table_pages.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TablePagesFragment : Fragment(R.layout.fragment_table_pages), KodeinAware {

    private val INDEX: String = "index"
    override val kodein by closestKodein()
    private val venusDao: VenusDao by instance()

    companion object {
        @JvmStatic
        fun newInstance(param: Int) =
            TablePagesFragment().apply {
                arguments = Bundle().apply {
                    putInt(INDEX, param)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerTable.layoutManager = LinearLayoutManager(context)
    }

    private fun loadData() {
//        String sd = "SELECT * FROM table_name WHERE hafta_id = '1' ORDER BY hafta_id, para_id, dars_turi ASC"
    }
}