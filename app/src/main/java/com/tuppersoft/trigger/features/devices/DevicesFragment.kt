package com.tuppersoft.trigger.features.devices

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.trigger.core.platform.GlobalFragment
import com.tuppersoft.trigger.databinding.DevicesFragmentBinding
import com.tuppersoft.trigger.features.main.MainActivityViewModel
import com.tuppersoft.trigger.features.service.Actions
import com.tuppersoft.trigger.features.service.MonitorService
import com.tuppersoft.trigger.features.service.ServiceState
import com.tuppersoft.trigger.features.service.getServiceConfigState
import com.tuppersoft.trigger.features.service.setServiceConfigState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DevicesFragment : GlobalFragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private val devicesViewModel: DevicesViewModel by viewModels()
    private val deviceListAdapter = DeviceListAdapter()

    private var _binding: DevicesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DevicesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun init() {
        handleDeviceList()
        handlePermission()
        devicesViewModel.getDevices()
        deviceListAdapter.setOnclickItemListener {
            if (it.isSelected) {
                devicesViewModel.saveDevice(it.mac)
            } else {
                devicesViewModel.deleteDevice(it.mac)
            }

        }

        binding.swStart.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                startService()
            } else {
                stopService()
            }
        }
    }

    private fun handleDeviceList() {

        lifecycleScope.launch {
            devicesViewModel.devices.collect { deviceList ->
                binding.rvDevices.adapter = deviceListAdapter.apply {
                    submitList(deviceList)
                }

            }
        }
    }

    private fun handlePermission() {
        lifecycleScope.launch {
            viewModel.permissionAccept.collect { flag ->
                if (flag) {
                    context?.let {
                        binding.swStart.isChecked = getServiceConfigState(it) == ServiceState.STARTED
                        if (binding.swStart.isChecked) {
                            startService()
                        } else {
                            stopService()
                        }

                    }
                }
            }
        }
    }

    private fun stopService() {
        activity?.let { mActivity ->
            binding.swStart.isChecked = false
            if (getServiceConfigState(mActivity) != ServiceState.STOPPED) {
                Intent(mActivity, MonitorService::class.java).also {
                    it.action = Actions.STOP.name
                    "${Actions.STOP.name} the service from a Fragment".logd()
                    startForegroundService(requireContext(), it)
                }
                setServiceConfigState(mActivity, ServiceState.STOPPED)
            }
        }
    }

    private fun startService() {

        activity?.let { mActivity ->
            if (!viewModel.permissionAccept.value) {
                binding.swStart.isChecked = false
            } else {
                setServiceConfigState(mActivity, ServiceState.STARTED)
                Intent(mActivity, MonitorService::class.java).also {
                    it.action = Actions.START.name
                    "${Actions.START.name} the service from a Fragment".logd()
                    startForegroundService(requireContext(), it)
                }
            }

        }
    }
}
