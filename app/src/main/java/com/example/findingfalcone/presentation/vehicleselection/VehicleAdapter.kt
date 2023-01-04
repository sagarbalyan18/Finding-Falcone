package com.example.findingfalcone.presentation.vehicleselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findingfalcone.R
import com.example.findingfalcone.domain.model.Vehicle
import kotlinx.android.synthetic.main.item_vehicle.view.*

class VehicleAdapter(private val action: (vehicle: Vehicle) -> Unit) : RecyclerView.Adapter<VehicleAdapter.CustomAdapter>() {
    private var list: ArrayList<Vehicle> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return CustomAdapter(view)
    }

    override fun onBindViewHolder(holder: CustomAdapter, position: Int) {
        holder.bind(list[position], action)
    }

    override fun getItemCount(): Int = list.size

    // Add a list of items
    fun updateList(list: ArrayList<Vehicle>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class CustomAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(vehicle: Vehicle, action: (vehicle: Vehicle) -> Unit) {
            itemView.shuttleImage.setImageResource(vehicle.getImage())
            itemView.shuttleSpeedText.text = vehicle.speed.toString()
            itemView.shuttleContainer.setOnClickListener { action(vehicle) }
        }
    }
}