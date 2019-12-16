package tokovoj.itunesalbums.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Response
import tokovoj.itunesalbums.App
import tokovoj.itunesalbums.data.AlbumData
import tokovoj.itunesalbums.data.Results
import tokovoj.itunesalbums.network.Network
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject


@InjectViewState
class MainPresenter : MvpPresenter<MainView>()
{
    @Inject
    lateinit var model : Network

    init {
        App.networkComponent.inject(this)
    }

    fun onSearchClick(query: String)
    {
        viewState.hideKeyboard()
        viewState.showProgressBar()
        searchAlbums(query)
    }

    private fun searchAlbums(query: String)
    {
        model.searchAlbums(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : DisposableSingleObserver<Response<AlbumData>>()
                {
                    private fun selector(it: Results): String = it.collectionName

                    override fun onSuccess(t: Response<AlbumData>)
                    {
                        if (t.isSuccessful && t.body() != null)
                        {
                            if(t.body()!!.resultCount == 0)
                            {
                                viewState.setNoResultMessage()
                            }
                            else
                            {
                                viewState.setAlbums(t.body()!!.resultCount, t.body()!!.results.sortedBy {selector(it)})
                            }
                        }
                        else
                        {
                            parseErrorCode(t.code())
                        }
                        viewState.hideProgressBar()
                    }

                    override fun onError(e: Throwable)
                    {
                       when (e)
                       {
                            is UnknownHostException -> viewState.setConnectionLostMessage()
                            is TimeoutException -> viewState.setConnectionLostMessage()
                        }
                        viewState.hideProgressBar()
                    }
                }
            )
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