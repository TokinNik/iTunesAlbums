package tokovoj.itunesalbums.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import tokovoj.itunesalbums.Data.Results
import tokovoj.itunesalbums.R

class AlbumsRecyclerViewAdapter(data: List<Results>, var listener: MainActivity.OnAlbumsListIneraxtionListener) : RecyclerView.Adapter<AlbumsRecyclerViewAdapter.ViewHolder>()
{
    var dataList: List<Results> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.albumNameTextView.text = dataList[position].collectionName
        holder.artistNameTextView.text = dataList[position].artistName
        Picasso.with(holder.viev.context)
            .load(dataList[position].artworkUrl100)
            .placeholder(R.drawable.red_box)
            .error(R.drawable.red_box)
            .into(holder.albumImageView)
        holder.viev.setOnClickListener { v -> listener.onItemSelect(position) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val viev: View = itemView
        val albumNameTextView: TextView = itemView.findViewById(R.id.album_textView)
        val artistNameTextView: TextView = itemView.findViewById(R.id.artist_textView)
        val albumImageView: ImageView = itemView.findViewById(R.id.album_imageView)
    }

}