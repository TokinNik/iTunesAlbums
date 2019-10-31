package tokovoj.itunesalbums.Presentor

import android.util.Log
import tokovoj.itunesalbums.AppModel
import tokovoj.itunesalbums.Data.Results
import tokovoj.itunesalbums.Network.SearchQueryCallback

class MainPresentor(var model: AppModel.Model) : AppModel.Presentor
{
    var view: AppModel.View? = null

    override fun attachView(view: AppModel.View)
    {
        this.view = view
    }

    override fun detachView()
    {
        view = null
    }

    override fun searchAlbubs(query: String)
    {
        model.searchAlbums(query, object: SearchQueryCallback
        {
            private fun selector(it: Results): String = it.collectionName

            override fun onComplete(count: Int, items: List<Results>)
            {
                Log.d("PRESENTOR", "onComplete: $count")
                if(count == 0)
                {
                    view?.setNoResultMessage()
                }
                else
                {
                    Log.d("PRESENTOR", "onComplete: $count")
                    view?.setAlbums(count, items.sortedBy {selector(it)})
                }
            }
            override fun onCompleteError(code: Int)
            {
                Log.d("PRESENTOR", "ocCompleteError: $code")
                parseErrorCode(code)
            }

            override fun onFailture()
            {
                Log.d("PRESENTOR", "ocFailture: ")
                view?.setConnectionLostMessage()
            }
        })
    }

    override fun getSongsForAlbum(id: Long)
    {
        model.getAlbumSongs(id, object: SearchQueryCallback
        {
            override fun onComplete(count: Int, items: List<Results>)
            {
                Log.d("PRESENTOR", "onComplete: $count")
                view?.setSongs(count, items.subList(1, items.size))
            }

            override fun onCompleteError(code: Int)
            {
                Log.d("PRESENTOR", "ocCompleteError: $code")
                parseErrorCode(code)
            }

            override fun onFailture()
            {
                Log.d("PRESENTOR", "ocFailture: ")
                view?.setConnectionLostMessage()
            }
        })
    }

    fun parseErrorCode(code: Int)
    {
        when(code)
        {
            204 -> view?.setNoResultMessage()
            400 -> view?.setBadRequestMessage()
            500 -> view?.setServerErrorMessage()
        }
    }

}