package com.example.fitnesscenterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesscenterapp.databinding.PageEquipmentListBinding
import com.example.fitnesscenterapp.databinding.PageMainMenuBinding
import com.example.lib.Equipment

class MainPagerAdapter(
    private val mainActivity: MainActivity,
    private val app: MyApplication,
    private val onMenuButtonClick: (String) -> Unit,
    private val onEquipmentClick: (Equipment) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MENU = 0
        private const val VIEW_TYPE_LIST = 1
    }

    class MenuViewHolder(val binding: PageMainMenuBinding) : RecyclerView.ViewHolder(binding.root)
    class ListViewHolder(val binding: PageEquipmentListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_MENU
            1 -> VIEW_TYPE_LIST
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MENU -> {
                val binding = PageMainMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MenuViewHolder(binding)
            }
            VIEW_TYPE_LIST -> {
                val binding = PageEquipmentListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ListViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MenuViewHolder -> setupMenuPage(holder)
            is ListViewHolder -> setupListPage(holder)
        }
    }

    override fun getItemCount(): Int = 2

    private fun setupMenuPage(holder: MenuViewHolder) {
        with(holder.binding) {
            buttonAdd.setOnClickListener { onMenuButtonClick("ADD") }
            buttonQR.setOnClickListener { onMenuButtonClick("QR") }
            buttonInfo.setOnClickListener { onMenuButtonClick("INFO") }
            buttonAbout.setOnClickListener { onMenuButtonClick("ABOUT") }
            buttonExit.setOnClickListener { onMenuButtonClick("EXIT") }
        }
    }

    private fun setupListPage(holder: ListViewHolder) {
        with(holder.binding) {
            recyclerView.layoutManager = LinearLayoutManager(mainActivity)
            val adapter = EquipmentAdapter(app, onEquipmentClick)
            recyclerView.adapter = adapter
        }
    }

    fun refreshList(position: Int) {
        if (position == 1) {
            notifyItemChanged(1)
        }
    }
}
