package com.tuppersoft.trigger.features.devices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tuppersoft.skizo.kotlin.core.customtypealias.OnClickItem
import com.tuppersoft.trigger.databinding.ItemDeviceBinding
import com.tuppersoft.trigger.features.devices.DeviceListAdapter.DeviceViewHolder

class DeviceListAdapter : ListAdapter<DeviceNav, DeviceViewHolder>(DeviceDiffCallback()) {

    var onClickItem: OnClickItem<DeviceNav> = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(currentList[position], onClickItem)
    }

    fun setOnclickItemListener(onClickItem: OnClickItem<DeviceNav>) {
        this.onClickItem = onClickItem
    }

    inner class DeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DeviceNav, onClickItem: OnClickItem<DeviceNav>) {
            binding.tvName.text = item.name
            binding.tvMac.text = item.mac
            binding.cbCheck.isChecked = item.isSelected
            binding.root.setOnClickListener {
                binding.cbCheck.isChecked = !binding.cbCheck.isChecked
            }

            binding.cbCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isSelected = isChecked
                onClickItem(item)
            }
        }
    }

    class DeviceDiffCallback : DiffUtil.ItemCallback<DeviceNav>() {

        override fun areItemsTheSame(oldItem: DeviceNav, newItem: DeviceNav): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DeviceNav, newItem: DeviceNav): Boolean {
            return oldItem.mac == newItem.mac
        }
    }
}
