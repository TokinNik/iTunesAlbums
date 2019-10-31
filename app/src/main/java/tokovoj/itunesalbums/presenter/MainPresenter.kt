package tokovoj.itunesalbums.presenter

import android.util.Log
import tokovoj.itunesalbums.AppModel
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.network.SearchQueryCallback

class MainPresenter(private var model: AppModel.Model) : AppModel.Presenter
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

    override fun searchAlbums(query: String)
    {
        model.searchAlbums(query, object: SearchQueryCallback
        {
            private fun selector(it: Results): String = it.collectionName

            override fun onComplete(count: Int, items: List<Results>)
            {
                Log.d("PRESENTER", "onComplete: $count")
                if(count == 0)
                {
                    view?.setNoResultMessage()
                }
                else
                {
                    Log.d("PRESENTER", "onComplete: $count")
                    view?.setAlbums(count, items.sortedBy {selector(it)})
                }
            }
            override fun onCompleteError(code: Int)
            {
                Log.d("PRESENTER", "ocCompleteError: $code")
                parseErrorCode(code)
            }

            override fun onFailure()
            {
                Log.d("PRESENTER", "ocFailure: ")
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
                Log.d("PRESENTER", "onComplete: $count")
                view?.setSongs(count, items.subList(1, items.size))
            }

            override fun onCompleteError(code: Int)
            {
                Log.d("PRESENTER", "ocCompleteError: $code")
                parseErrorCode(code)
            }

            override fun onFailure()
            {
                Log.d("PRESENTER", "ocFailure: ")
                view?.setConnectionLostMessage()
            }
        })
    }

    fun parseErrorCode(code: Int)
    {
        when(code)
        {
            400 -> view?.setBadRequestMessage()
            500 -> view?.setServerErrorMessage()
            else -> view?.setErrorMessage(code)
        }
    }

}