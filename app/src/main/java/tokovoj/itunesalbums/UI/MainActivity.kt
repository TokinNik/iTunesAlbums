package tokovoj.itunesalbums.UI

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tokovoj.itunesalbums.AppModel
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.network.Network
import tokovoj.itunesalbums.presenter.MainPresenter
import tokovoj.itunesalbums.R

class MainActivity : AppCompatActivity(), AppModel.View
{
    private lateinit var albumsRecycler: RecyclerView
    lateinit var list: List<Results>
    private lateinit var albumListListener: OnAlbumsListInteractionListener
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var presenter: AppModel.Presenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(Network())
        presenter.attachView(this)

        searchProgressBar= findViewById(R.id.search_progressBar)
        searchEditText = findViewById(R.id.search_editText)
        searchButton = findViewById(R.id.search_button)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            searchButton.background.colorFilter = BlendModeColorFilter(0xD32F2F, BlendMode.SRC_ATOP)
        }
        else
        {
            searchButton.background.setColorFilter(0xD32F2F, PorterDuff.Mode.MULTIPLY)
        }
        searchButton.setOnClickListener{
            showProgressBar()
            presenter.searchAlbums(searchEditText.text.toString())}

        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                showProgressBar()
                presenter.searchAlbums(searchEditText.text.toString())
                true
            }
            else
            {
                false
            }
        }

        albumListListener = object : OnAlbumsListInteractionListener
        {
            override fun onItemSelect(position: Int)
            {
                setAlbumFragment(list[position])
            }
        }

        albumsRecycler = findViewById(R.id.albums_recyclerView)
        albumsRecycler.layoutManager = GridLayoutManager(this, 1)
    }

    private fun setAlbumFragment(results: Results)
    {
        albumsRecycler.visibility = View.GONE
        findViewById<LinearLayout>(R.id.search_linearLayout).visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .add(R.id.container, AlbumFragment(results), AlbumFragment.TAG)
            .commit()
        presenter.getSongsForAlbum(results.collectionId)
    }

    override fun onBackPressed()
    {
        if(albumsRecycler.visibility == View.GONE)
        {
            albumsRecycler.visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.search_linearLayout).visibility = View.VISIBLE
            supportFragmentManager.findFragmentByTag(AlbumFragment.TAG)?.let{
                supportFragmentManager.beginTransaction()
                    .remove(it)
                    .commit()
            }
        }
        else
        {
            super.onBackPressed()
        }
    }

    override fun setAlbums(count: Int, items: List<Results>)
    {
        hideProgressBar()
        list = items
        albumsRecycler.adapter = AlbumsRecyclerViewAdapter(list, albumListListener)
    }

    override fun setSongs(count: Int, items: List<Results>)
    {
        findViewById<ProgressBar>(R.id.songs_progressBar).visibility = View.GONE
        (supportFragmentManager.findFragmentByTag(AlbumFragment.TAG) as AlbumFragment).setSongsList(items)
    }

    override fun setErrorMessage(code: Int)
    {
        Toast.makeText(this, "${R.string.download_error }: $code", Toast.LENGTH_SHORT).show()
        hideProgressBar()
    }

    override fun setBadRequestMessage()
    {
        Toast.makeText(this, R.string.bad_request_message, Toast.LENGTH_SHORT).show()
        hideProgressBar()
    }

    override fun setServerErrorMessage()
    {
        Toast.makeText(this, R.string.server_error_message, Toast.LENGTH_SHORT).show()
        hideProgressBar()
    }

    override fun setConnectionLostMessage()
    {
        hideProgressBar()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.connection_lost_error)
            .setMessage(R.string.connection_lost_error_message)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { dialog: DialogInterface, which: Int -> dialog.cancel()}
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun setNoResultMessage()
    {
        Toast.makeText(this, R.string.no_result_found, Toast.LENGTH_SHORT).show()
        hideProgressBar()
    }

    private fun showProgressBar()
    {
        searchProgressBar.visibility = View.VISIBLE
        searchButton.visibility = View.GONE
    }

    private fun hideProgressBar()
    {
        if(albumsRecycler.visibility == View.VISIBLE)
        {
            searchProgressBar.visibility = View.GONE
            searchButton.visibility = View.VISIBLE
        }
        else
        {
            findViewById<ProgressBar>(R.id.songs_progressBar).visibility = View.GONE
        }
    }

    interface OnAlbumsListInteractionListener
    {
        fun onItemSelect(position: Int)
    }

    override fun onDestroy()
    {
        presenter.detachView()
        super.onDestroy()
    }
}
