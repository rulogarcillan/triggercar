package com.tuppersoft.trigger.core.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Raúl Rodríguez Concepción on 2019-06-16.
 * raulrcs@gmail.com
 */

class CoreViewModel : ViewModel() {

    private val _toolbarTitle: MutableLiveData<String> = MutableLiveData("")
    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle
    private val _showToolbar: MutableLiveData<Boolean> = MutableLiveData()
    val showToolbar: LiveData<Boolean>
        get() = _showToolbar

    fun setToolbarTitle(title: String) {
        _toolbarTitle.value = title
    }

    fun showToolbar(flag: Boolean) {
        _showToolbar.value = flag
    }
}
