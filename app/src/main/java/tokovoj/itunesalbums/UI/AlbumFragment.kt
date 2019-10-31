package tokovoj.itunesalbums.UI

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.R
import java.text.SimpleDateFormat
import java.util.*

class AlbumFragment(var results: Results) : Fragment()
{
    companion object
    {
        const val TAG = "ALBUM_FRAGMENT"
    }
    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,svedInstanceState: Bundle?): View?
    {
        root = inflater.inflate(R.layout.fragment_album, container, false)
        setUI()
        return root
    }

    private fun setUI()
    {
        root.findViewById<TextView>(R.id.album_textView).text = results.collectionName
        root.findViewById<TextView>(R.id.artist_textView).text = results.artistName
        root.findViewById<TextView>(R.id.genre_textView).text = results.primaryGenreName
        root.findViewById<TextView>(R.id.size_textView).text = StringBuilder((results.trackCount).toString()).append(" ").append(resources.getString(R.string.n_tracks))
        root.findViewById<TextView>(R.id.release_date_textView).text = StringBuilder(resources.getString(R.string.release)).append(" ").append(convertToNormalDate(results.releaseDate))
        root.findViewById<TextView>(R.id.copyright_textView).text = results.copyright
        Picasso.with(root.context)
            .load(results.artworkUrl100)
            .placeholder(R.drawable.red_box)
            .error(R.drawable.red_box)
            .into(root.findViewById<ImageView>(R.id.album_imageView))
        root.findViewById<RecyclerView>(R.id.songs_recyclerView).layoutManager = GridLayoutManager(root.context, 1)
    }

    fun setSongsList(songs: List<Results>)
    {
        root.findViewById<RecyclerView>(R.id.songs_recyclerView).adapter = SongsRecyclerViewAdapter(songs)
        println(songs.size)
    }

    private fun convertToNormalDate(oldFormat: String): String?
    {
        val format: SimpleDateFormat? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        format?.timeZone = TimeZone.getTimeZone("UTC")
        val formatNormal: SimpleDateFormat? = SimpleDateFormat("dd.MM.yyyy")
        val date: Date = format?.parse(oldFormat) ?: Date()
        return formatNormal?.format(date)
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
    }

    override fun onDetach()
    {
        super.onDetach()
    }
}
