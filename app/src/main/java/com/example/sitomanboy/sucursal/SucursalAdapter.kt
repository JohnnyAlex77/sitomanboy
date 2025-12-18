package com.example.sitomanboy.sucursal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sitomanboy.databinding.ItemSucursalBinding
import com.example.sitomanboy.model.Sucursal

class SucursalAdapter(
    private val listener: OnSucursalClickListener
) : ListAdapter<Sucursal, SucursalAdapter.SucursalViewHolder>(SucursalDiffCallback()) {

    interface OnSucursalClickListener {
        fun onVerClick(sucursal: Sucursal)
        fun onModificarClick(sucursal: Sucursal)
        fun onEliminarClick(sucursal: Sucursal)
        fun onInventarioClick(sucursal: Sucursal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SucursalViewHolder {
        val binding = ItemSucursalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SucursalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SucursalViewHolder, position: Int) {
        val sucursal = getItem(position)
        holder.bind(sucursal)
    }

    inner class SucursalViewHolder(
        private val binding: ItemSucursalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onVerClick(getItem(position))
                }
            }
        }

        fun bind(sucursal: Sucursal) {
            binding.tvNombreSucursal.text = sucursal.nombre
            binding.tvCodigoSucursal.text = "Código: ${sucursal.codigo}"
            binding.tvDireccionSucursal.text = sucursal.direccion

            // Mostrar estadísticas del inventario
            val repuestos = sucursal.obtenerRepuestos()
            val totalRepuestos = repuestos.size
            val totalStock = repuestos.sumOf { it.stock }

            binding.tvTotalProductos.text = "Productos: $totalRepuestos • Stock: $totalStock"
        }
    }

    class SucursalDiffCallback : DiffUtil.ItemCallback<Sucursal>() {
        override fun areItemsTheSame(oldItem: Sucursal, newItem: Sucursal): Boolean {
            return oldItem.codigo == newItem.codigo
        }

        override fun areContentsTheSame(oldItem: Sucursal, newItem: Sucursal): Boolean {
            return oldItem == newItem
        }
    }
}