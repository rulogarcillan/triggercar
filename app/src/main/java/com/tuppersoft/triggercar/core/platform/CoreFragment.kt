package com.tuppersoft.triggercar.core.platform

/**
 * Created by Raúl Rodríguez Concepción on 2019-06-16.
 * raulrcs@gmail.com
 */

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tuppersoft.triggercar.R

abstract class CoreFragment : Fragment() {

    private val globalViewModel: CoreViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
    }

    private fun setToolbar() {
        showActionBar()
        setTitleToolbar(getString(R.string.app_name))
    }

    fun setTitleToolbar(title: String) {
        globalViewModel.setToolbarTitle(title)
    }

    fun hideActionBar() {
        globalViewModel.showToolbar(false)
    }

    fun showActionBar() {
        globalViewModel.showToolbar(true)
    }
}


