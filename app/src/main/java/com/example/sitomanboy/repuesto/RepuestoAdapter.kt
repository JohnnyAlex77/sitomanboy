package com.example.sitomanboy.repuesto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sitomanboy.databinding.ItemRepuestoBinding
import com.example.sitomanboy.model.Repuesto

class RepuestoAdapter(
    private val listener: OnRepuestoClickListener
) : ListAdapter<Repuesto, RepuestoAdapter.RepuestoViewHolder>(RepuestoDiffCallback()) {

    interface OnRepuestoClickListener {
        fun onVerClick(repuesto: Repuesto)
        fun onModificarClick(repuesto: Repuesto)
        fun onEliminarClick(repuesto: Repuesto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepuestoViewHolder {
        val binding = ItemRepuestoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepuestoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepuestoViewHolder, position: Int) {
        val repuesto = getItem(position)
        holder.bind(repuesto)
    }

    inner class RepuestoViewHolder(
        private val binding: ItemRepuestoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnVer.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onVerClick(getItem(position))
                }
            }

            binding.btnModificar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onModificarClick(getItem(position))
                }
            }

            binding.btnEliminar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEliminarClick(getItem(position))
                }
            }
        }

        fun bind(repuesto: Repuesto) {
            binding.tvSerie.text = repuesto.serie
            binding.tvDescripcion.text = repuesto.descripcion
            binding.tvStock.text = "Stock: ${repuesto.stock}"
        }
    }

    class RepuestoDiffCallback : DiffUtil.ItemCallback<Repuesto>() {
        override fun areItemsTheSame(oldItem: Repuesto, newItem: Repuesto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repuesto, newItem: Repuesto): Boolean {
            return oldItem == newItem
        }
    }
}