package com.example.fundamental_android.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundamental_android.model.Hero
import com.example.fundamental_android.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterHero(private val listHero: ArrayList<Hero>) : RecyclerView.Adapter<AdapterHero.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_recycleview, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Picasso.get().load(listHero[position].avatarUrl).into(holder.imgPhoto);
        holder.tvName.text = listHero[position].login
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listHero[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listHero.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: CircleImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Hero)
    }

    fun setData(newList: List<Hero>) {
        listHero.clear()
        listHero.addAll(newList)
    }
}