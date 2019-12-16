package tokovoj.itunesalbums.ui.album

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album.*
import moxy.MvpAppCompatFragment
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.R
import moxy.ktx.moxyPresenter
import java.text.SimpleDateFormat
import java.util.*

class AlbumFragment(var results: Results) : MvpAppCompatFragment(), AlbumFragmentView
{
    companion object
    {
        const val TAG = "ALBUM_FRAGMENT"
    }
    private lateinit var root: View

    private val presenter by moxyPresenter { AlbumFragmentPresenter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,svedInstanceState: Bundle?): View?
    {
        root = inflater.inflate(R.layout.fragment_album, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUI()
    }

    private fun setUI()
    {
        album_textView.text = results.collectionName
        artist_textView.text = results.artistName
        genre_textView.text = results.primaryGenreName
        size_textView.text = StringBuilder((results.trackCount).toString()).append(" ").append(resources.getString(R.string.n_tracks))
        release_date_textView.text = StringBuilder(resources.getString(R.string.release)).append(" ").append(convertToNormalDate(results.releaseDate))
        copyright_textView.text = results.copyright
        Picasso.with(root.context)
            .load(results.artworkUrl100)
            .placeholder(R.drawable.red_box)
            .error(R.drawable.red_box)
            .into(album_imageView)
        songs_recyclerView.layoutManager = GridLayoutManager(root.context, 1)
        presenter.getSongsForAlbum(results.collectionId)
    }

    private fun convertToNormalDate(oldFormat: String): String?
    {
        val format: SimpleDateFormat? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        format?.timeZone = TimeZone.getTimeZone("UTC")
        val formatNormal: SimpleDateFormat? = SimpleDateFormat("dd.MM.yyyy")
        val date: Date = format?.parse(oldFormat) ?: Date()
        return formatNormal?.format(date)
    }

    override fun setSongs(count: Int, items: List<Results>)
    {
        songs_recyclerView.adapter =
            SongsRecyclerViewAdapter(items)
        println(items.size)
    }

    override fun setErrorMessage(code: Int)
    {
        Toast.makeText(activity, "${R.string.download_error }: $code", Toast.LENGTH_SHORT).show()
    }

    override fun setBadRequestMessage()
    {
        Toast.makeText(activity, R.string.bad_request_message, Toast.LENGTH_SHORT).show()
    }

    override fun setServerErrorMessage()
    {
        Toast.makeText(activity, R.string.server_error_message, Toast.LENGTH_SHORT).show()
    }

    override fun setNoResultMessage()
    {
        Toast.makeText(activity, R.string.no_result_found, Toast.LENGTH_SHORT).show()
    }

    override fun setConnectionLostMessage()
    {

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.connection_lost_error)
            .setMessage(R.string.connection_lost_error_message)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { dialog: DialogInterface, _ -> dialog.cancel()}
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun showProgressBar()
    {
        songs_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar()
    {
        songs_progressBar.visibility = View.GONE
    }
}
