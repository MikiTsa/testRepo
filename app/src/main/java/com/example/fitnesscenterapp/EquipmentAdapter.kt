package com.example.fitnesscenterapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesscenterapp.databinding.ItemEquipmentBinding
import com.example.lib.Equipment

class EquipmentAdapter(
    private val app: MyApplication,
    private val onItemClick: (Equipment) -> Unit
) : RecyclerView.Adapter<EquipmentAdapter.ViewHolder>() {

    private val fitnessCenters = listOf(
        "CleverFit Maribor",
        "PowerGym Ljubljana",
        "FitZone Celje",
        "IronTemple Kranj",
        "FlexFit Nova Gorica"
    )

    class ViewHolder(val binding: ItemEquipmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEquipmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipment = app.equipmentList[position]
        
        with(holder.binding) {
            tvEquipmentName.text = equipment.name
            tvWeightAndMuscle.text = "${equipment.weightLimit}kg | ${equipment.muscleGroup}"

            val fitnessCenter = fitnessCenters[position % fitnessCenters.size]
            tvLocation.text = fitnessCenter
            tvBrand.text = equipment.brand.name

            // Click to edit
            cvEquipmentItem.setOnClickListener {
                onItemClick(equipment)
            }

            // Long click to delete
            cvEquipmentItem.setOnLongClickListener {
                showDeleteConfirmation(holder.itemView, equipment, holder.bindingAdapterPosition)
                true
            }
        }
    }

    override fun getItemCount(): Int = app.equipmentList.size

    private fun showDeleteConfirmation(view: View, equipment: Equipment, position: Int) {
        AlertDialog.Builder(view.context)
            .setTitle("Delete Equipment")
            .setMessage("Are you sure you want to delete ${equipment.name}?")
            .setPositiveButton("Delete") { dialog, _ ->
                app.deleteEquipment(equipment.id)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, app.equipmentList.size)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}
