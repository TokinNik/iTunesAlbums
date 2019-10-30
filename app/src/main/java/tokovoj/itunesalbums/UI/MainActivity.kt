package tokovoj.itunesalbums.UI

import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tokovoj.itunesalbums.Data.Results
import tokovoj.itunesalbums.Network.Network
import tokovoj.itunesalbums.Network.SearchAlbumsCallback
import tokovoj.itunesalbums.R

class MainActivity : AppCompatActivity(), AlbumFragment.OnFragmentInteractionListener
{
    private lateinit var albumsRecycler: RecyclerView
    lateinit var net: Network
    lateinit var list: List<Results>
    lateinit var albumListListener: OnAlbumsListIneraxtionListener
    lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        net = Network()
        searchButton = findViewById(R.id.search_button)
        searchButton.setOnClickListener{
            findViewById<ProgressBar>(R.id.search_progressBar).visibility = View.VISIBLE
            it.visibility = View.GONE
            net.searchAlbums(findViewById<EditText>(R.id.search_editText).text.toString(),
            object: SearchAlbumsCallback
            {
                private fun selector(it: Results): String = it.collectionName

                override fun onComplete(count: Int, items: List<Results>)
                {
                    Log.d("MAIN", "onComplete: $count")
                    list = items.sortedBy {selector(it)}
                    findViewById<ProgressBar>(R.id.search_progressBar).visibility = View.GONE
                    it.visibility = View.VISIBLE
                    albumsRecycler.adapter = AlbumsRecyclerViewAdapter(list, albumListListener)
                }
                override fun onCompleteError(code: Int)
                {
                    Log.d("MAIN", "ocCompleteError: $code")
                }

                override fun onFailture()
                {
                    Log.d("MAIN", "ocFailture: ")
                }

            })}

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
        net.getAlbumSongs(results.collectionId, object: SearchAlbumsCallback
        {
            override fun onComplete(count: Int, items: List<Results>)
            {
                Log.d("MAIN", "onComplete: $count")
                val songs: MutableList<Results> = items.toMutableList()
                songs.removeAt(0)//todo drugoe reshenie???
                findViewById<ProgressBar>(R.id.songs_progressBar).visibility = View.GONE
                (supportFragmentManager.findFragmentByTag(AlbumFragment.TAG) as AlbumFragment).setSongsList(songs)//todo privedenie???
            }

            override fun onCompleteError(code: Int)
            {
                Log.d("MAIN", "ocCompleteError: $code")
            }

            override fun onFailture()
            {
                Log.d("MAIN", "ocFailture: ")
            }

        })
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

    override fun onFragmentBackPressed()
    {

    }

    interface OnAlbumsListIneraxtionListener
    {
        fun onItemSelect(position: Int)
    }
}
