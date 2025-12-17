package com.example.sitomanboy.sucursal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sitomanboy.databinding.ItemInventarioBinding
import com.example.sitomanboy.model.Repuesto

class InventarioAdapter(
    private val listener: OnInventarioClickListener
) : ListAdapter<Repuesto, InventarioAdapter.InventarioViewHolder>(InventarioDiffCallback()) {

    interface OnInventarioClickListener {
        fun onVerDetalleClick(repuesto: Repuesto)
        fun onModificarStockClick(repuesto: Repuesto)
        fun onEliminarProductoClick(repuesto: Repuesto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventarioViewHolder {
        val binding = ItemInventarioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InventarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventarioViewHolder, position: Int) {
        val repuesto = getItem(position)
        holder.bind(repuesto)
    }

    inner class InventarioViewHolder(
        private val binding: ItemInventarioBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnVer.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onVerDetalleClick(getItem(position))
                }
            }

            binding.btnModificarStock.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onModificarStockClick(getItem(position))
                }
            }

            binding.btnEliminar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEliminarProductoClick(getItem(position))
                }
            }
        }

        fun bind(repuesto: Repuesto) {
            binding.tvSerie.text = repuesto.serie
            binding.tvDescripcion.text = repuesto.descripcion
            binding.tvStock.text = "Stock: ${repuesto.stock} unidades"
        }
    }

    class InventarioDiffCallback : DiffUtil.ItemCallback<Repuesto>() {
        override fun areItemsTheSame(oldItem: Repuesto, newItem: Repuesto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repuesto, newItem: Repuesto): Boolean {
            return oldItem == newItem
        }
    }
}