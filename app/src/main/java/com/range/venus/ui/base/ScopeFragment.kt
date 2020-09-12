package com.range.venus.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.range.venus.data.pravider.UnitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

abstract class ScopedFragment(layoutId: Int) : Fragment(layoutId), CoroutineScope, KodeinAware {
    private lateinit var job: Job
    override val kodein by closestKodein()
    protected val unitProvider: UnitProvider by instance()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}