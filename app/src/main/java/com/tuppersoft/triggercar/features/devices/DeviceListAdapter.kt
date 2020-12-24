package com.tuppersoft.triggercar.features.devices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tuppersoft.domain.models.DeviceModel
import com.tuppersoft.triggercar.databinding.ItemDeviceBinding
import com.tuppersoft.triggercar.features.devices.CharactersListAdapter.DeviceViewHolder

class CharactersListAdapter : ListAdapter<DeviceModel, DeviceViewHolder>(DeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class DeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DeviceModel) {
            binding.tvName.text = item.name
            binding.tvMac.text = item.mac
        }
    }

    class DeviceDiffCallback : DiffUtil.ItemCallback<DeviceModel>() {

        override fun areItemsTheSame(oldItem: DeviceModel, newItem: DeviceModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DeviceModel, newItem: DeviceModel): Boolean {
            return oldItem.mac == newItem.mac
        }
    }
}
