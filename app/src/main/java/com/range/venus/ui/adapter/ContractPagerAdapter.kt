package com.range.venus.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.range.venus.ui.fragment.ContractPagesFragment

class ContractPagerAdapter(private val listTitle: List<String>, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return listTitle.size
    }

    override fun getItem(position: Int): Fragment {
        return ContractPagesFragment.newInstance(listTitle[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }
}