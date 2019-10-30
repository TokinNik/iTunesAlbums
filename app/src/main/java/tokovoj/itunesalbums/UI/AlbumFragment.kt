package tokovoj.itunesalbums.UI

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album.view.*
import tokovoj.itunesalbums.Data.Results

import tokovoj.itunesalbums.R
import java.util.*

class AlbumFragment(var results: Results) : Fragment()
{
    companion object
    {
        const val TAG = "ALBUM_FRAGMENT"
    }
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

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
        root.findViewById<TextView>(R.id.size_textView).text = "${results.trackCount} трэков"
        root.findViewById<TextView>(R.id.release_date_textView).text = "Релиз: ${results.releaseDate.substring(0,results.releaseDate.length-10)}"//todo convert to normal date
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
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
    }

    override fun onDetach()
    {
        super.onDetach()
    }

    interface OnFragmentInteractionListener
    {
        fun onFragmentBackPressed()
    }


}
