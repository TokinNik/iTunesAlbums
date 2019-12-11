package tokovoj.itunesalbums.ui.main

import moxy.InjectViewState
import moxy.MvpPresenter
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.network.Network
import tokovoj.itunesalbums.network.SearchQueryCallback

@InjectViewState
class MainPresenter : MvpPresenter<MainView>()
{
    private val model = Network()

    fun searchAlbums(query: String)
    {
        model.searchAlbums(query, object: SearchQueryCallback
        {
            private fun selector(it: Results): String = it.collectionName

            override fun onComplete(count: Int, items: List<Results>)
            {
                if(count == 0)
                {
                    viewState.setNoResultMessage()
                }
                else
                {
                    viewState.setAlbums(count, items.sortedBy {selector(it)})
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