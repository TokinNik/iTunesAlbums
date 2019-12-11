package tokovoj.itunesalbums.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import tokovoj.itunesalbums.R
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.ui.album.AlbumFragment


class MainActivity : MvpAppCompatActivity(), MainView
{
    lateinit var list: List<Results>

    private lateinit var albumsRecycler: RecyclerView
    private lateinit var albumListListener: OnAlbumsListInteractionListener
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var searchProgressBar: ProgressBar

    private val presenter by moxyPresenter { MainPresenter() }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            hideKeyboard()
            presenter.searchAlbums(searchEditText.text.toString())
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                showProgressBar()
                hideKeyboard()
                presenter.searchAlbums(searchEditText.text.toString())
                true
            }
            else
            {
                false
            }
        }

        albumListListener = object :
            OnAlbumsListInteractionListener
        {
            override fun onItemSelect(position: Int)
            {
                hideKeyboard()
                setAlbumFragment(list[position])
            }
        }

        albumsRecycler = findViewById(R.id.albums_recyclerView)
        albumsRecycler.layoutManager = GridLayoutManager(this, 1)
    }

    private fun setAlbumFragment(results: Results)
    {
        albumsRecycler.visibility = View.GONE
        search_linearLayout.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .add(R.id.container,
                AlbumFragment(results), AlbumFragment.TAG)
            .commit()
    }

    override fun onBackPressed()
    {
        if(albumsRecycler.visibility == View.GONE)
        {
            albumsRecycler.visibility = View.VISIBLE
            search_linearLayout.visibility = View.VISIBLE
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
        albumsRecycler.adapter =
            AlbumsRecyclerViewAdapter(
                list,
                albumListListener
            )
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
            .setPositiveButton(R.string.ok) { dialog: DialogInterface, _ -> dialog.cancel()}
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
        searchProgressBar.visibility = View.GONE
        searchButton.visibility = View.VISIBLE
    }

    private fun hideKeyboard()
    {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(searchButton.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    interface OnAlbumsListInteractionListener
    {
        fun onItemSelect(position: Int)
    }

    override fun onDestroy()
    {
        super.onDestroy()
    }
}
