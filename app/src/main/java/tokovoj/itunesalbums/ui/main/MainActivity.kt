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
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import tokovoj.itunesalbums.R
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.ui.album.AlbumFragment


class MainActivity : MvpAppCompatActivity(), MainView
{
    lateinit var list: List<Results>

    private val presenter by moxyPresenter { MainPresenter() }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            search_button.background.colorFilter = BlendModeColorFilter(0xD32F2F, BlendMode.SRC_ATOP)
        }
        else
        {
            search_button.background.setColorFilter(0xD32F2F, PorterDuff.Mode.MULTIPLY)
        }
        
        initOnClick()
        initRecyclerView()
    }
    
    private fun initOnClick()
    {
        search_button.setOnClickListener{
            showProgressBar()
            hideKeyboard()
            presenter.searchAlbums(search_editText.text.toString())
        }

        search_editText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                showProgressBar()
                hideKeyboard()
                presenter.searchAlbums(search_editText.text.toString())
                true
            }
            else
            {
                false
            }
        }
    }

    private fun initRecyclerView()
    {
        albums_recyclerView.layoutManager = GridLayoutManager(this, 1)
    }

    private fun setAlbumFragment(results: Results)
    {
        albums_recyclerView.visibility = View.GONE
        search_linearLayout.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .add(R.id.container,
                AlbumFragment(results), AlbumFragment.TAG)
            .commit()
    }

    override fun onBackPressed()
    {
        if(albums_recyclerView.visibility == View.GONE)
        {
            albums_recyclerView.visibility = View.VISIBLE
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
        albums_recyclerView.adapter =
            AlbumsRecyclerViewAdapter(
                data = list,
                listener = object : OnAlbumsListInteractionListener
                {
                    override fun onItemSelect(position: Int)
                    {
                        hideKeyboard()
                        setAlbumFragment(list[position])
                    }
                }
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
        search_progressBar.visibility = View.VISIBLE
        search_button.visibility = View.GONE
    }

    private fun hideProgressBar()
    {
        search_progressBar.visibility = View.GONE
        search_button.visibility = View.VISIBLE
    }

    private fun hideKeyboard()
    {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(search_button.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
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
