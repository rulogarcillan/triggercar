package com.tuppersoft.trigger.core.platform

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

/**
 * Created by Raúl Rodríguez Concepción on 2019-06-16.
 * raulrcs@gmail.com
 */
abstract class CoreActivity : AppCompatActivity() {

    private val globalViewModel: CoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        observerShowToolbar()
        observerChangeTitleToolbar()
    }

    private fun observerShowToolbar() {
        globalViewModel.showToolbar.observe(this, Observer {
            when (it) {
                true -> supportActionBar?.show()
                false -> supportActionBar?.hide()

            }
        })
    }

    private fun observerChangeTitleToolbar() {
        globalViewModel.toolbarTitle.observe(this, Observer { title ->
            supportActionBar?.title = title
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> return true
        }
        return true
    }
}

