package tokovoj.itunesalbums.ui.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.R
import java.util.concurrent.TimeUnit

class SongsRecyclerViewAdapter(data: List<Results>) : RecyclerView.Adapter<SongsRecyclerViewAdapter.ViewHolder>()
{
    private var dataList: List<Results> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.songNameTextView.text = dataList[position].trackName
        holder.songArtistNameTextView.text = dataList[position].artistName
        holder.songNumberTextView.text = dataList[position].trackNumber.toString()
        holder.songTimeTextView.text = getNormalTime(dataList[position].trackTimeMillis)
    }

    private fun getNormalTime(trackTimeMillis: Long): CharSequence?
    {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(trackTimeMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis) - minutes*60
        return "$minutes:${if(seconds > 9) "" else "0"}$seconds"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val songNameTextView: TextView = itemView.findViewById(R.id.song_name_textView)
        val songArtistNameTextView: TextView = itemView.findViewById(R.id.song_artist_name_textView)
        val songNumberTextView: TextView = itemView.findViewById(R.id.song_number_textView)
        val songTimeTextView: TextView = itemView.findViewById(R.id.song_time_textView)
    }

}