package tokovoj.itunesalbums.ui.album

import moxy.InjectViewState
import moxy.MvpPresenter
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.network.Network
import tokovoj.itunesalbums.network.SearchQueryCallback

@InjectViewState
class AlbumFragmentPresenter : MvpPresenter<AlbumFragmentView>() {

    private val model = Network()

    fun getSongsForAlbum(id: Long)
    {
        model.getAlbumSongs(id, object: SearchQueryCallback
        {
            override fun onComplete(count: Int, items: List<Results>)
            {
                if(count == 0)
                {
                    viewState.setNoResultMessage()
                }
                else
                {
                    viewState.setSongs(count, items.subList(1, items.size))
                }
            }

            override fun onCompleteError(code: Int)
            {
                parseErrorCode(code)
            }

            override fun onFailure()
            {
                viewState.setConnectionLostMessage()
            }
        })
    }

    fun parseErrorCode(code: Int)
    {
        when(code)
        {
            400 -> viewState.setBadRequestMessage()
            500 -> viewState.setServerErrorMessage()
            else -> viewState.setErrorMessage(code)
        }
    }
}