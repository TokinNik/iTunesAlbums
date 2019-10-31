package tokovoj.itunesalbums

import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.network.SearchQueryCallback

interface AppModel
{
    interface View
    {
        fun setAlbums(count: Int, items: List<Results>)

        fun setSongs(count: Int, items: List<Results>)

        fun setErrorMessage(code: Int)

        fun setConnectionLostMessage()

        fun setNoResultMessage()

        fun setBadRequestMessage()

        fun setServerErrorMessage()

    }

    interface Presenter
    {
        fun searchAlbums(query: String)

        fun getSongsForAlbum(id: Long)

        fun attachView(view: View)

        fun detachView()
    }

    interface Model
    {
        fun searchAlbums(term: String, callback: SearchQueryCallback)

        fun getAlbumSongs(collectionId: Long, callback: SearchQueryCallback)
    }
}