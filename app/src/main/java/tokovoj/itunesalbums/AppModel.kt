package tokovoj.itunesalbums

import tokovoj.itunesalbums.Data.Results
import tokovoj.itunesalbums.Network.SearchQueryCallback

interface AppModel
{
    interface View
    {
        fun setAlbums(count: Int, items: List<Results>)

        fun setSongs(count: Int, items: List<Results>)

        fun setErrorMessage()

        fun setConnectionLostMessage()

        fun setNoResultMessage()

        fun setBadRequestMessage()

        fun setServerErrorMessage()

    }

    interface Presentor
    {
        fun searchAlbubs(query: String)

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