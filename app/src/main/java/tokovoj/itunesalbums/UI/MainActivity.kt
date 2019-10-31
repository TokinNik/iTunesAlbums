package tokovoj.itunesalbums.UI

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tokovoj.itunesalbums.AppModel
import tokovoj.itunesalbums.Data.Results
import tokovoj.itunesalbums.Network.Network
import tokovoj.itunesalbums.Presentor.MainPresentor
import tokovoj.itunesalbums.R

class MainActivity : AppCompatActivity(), AppModel.View
{
    private lateinit var albumsRecycler: RecyclerView
    lateinit var list: List<Results>
    private lateinit var albumListListener: OnAlbumsListIneraxtionListener
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var searchProgreeBar: ProgressBar
    private lateinit var presentor: AppModel.Presentor

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presentor = MainPresentor(Network())
        presentor.attachView(this)

        searchProgreeBar= findViewById(R.id.search_progressBar)
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
            presentor.searchAlbubs(searchEditText.text.toString())}

        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                showProgressBar()
                presentor.searchAlbubs(searchEditText.text.toString())
                true
            }
            else
            {
                false
            }
        }

        albumListListener = object : OnAlbumsListIneraxtionListener
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
        presentor.getSongsForAlbum(results.collectionId)
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

    override fun setErrorMessage()
    {
        Toast.makeText(this, R.string.download_error, Toast.LENGTH_SHORT).show()
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

    fun showProgressBar()
    {
        searchProgreeBar.visibility = View.VISIBLE
        searchButton.visibility = View.GONE
    }

    fun hideProgressBar()
    {
        if(albumsRecycler.visibility == View.VISIBLE)
        {
            searchProgreeBar.visibility = View.GONE
            searchButton.visibility = View.VISIBLE
        }
        else
        {
            findViewById<ProgressBar>(R.id.songs_progressBar).visibility = View.GONE
        }
    }

    interface OnAlbumsListIneraxtionListener
    {
        fun onItemSelect(position: Int)
    }

    override fun onDestroy()
    {
        presentor.detachView()
        super.onDestroy()
    }
}
