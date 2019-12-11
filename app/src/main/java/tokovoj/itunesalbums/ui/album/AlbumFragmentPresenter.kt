package tokovoj.itunesalbums.ui.album

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Response
import tokovoj.itunesalbums.data.AlbumData
import tokovoj.itunesalbums.network.Network
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@InjectViewState
class AlbumFragmentPresenter : MvpPresenter<AlbumFragmentView>() {

    private val model = Network()

    fun getSongsForAlbum(id: Long)
    {
        model.getAlbumById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : DisposableSingleObserver<Response<AlbumData>>()
                {
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
                                viewState.setSongs(t.body()!!.resultCount, t.body()!!.results)
                            }
                        }
                        else
                        {
                            parseErrorCode(t.code())
                        }
                    }

                    override fun onError(e: Throwable)
                    {
                        when (e)
                        {
                            is UnknownHostException -> viewState.setConnectionLostMessage()
                            is TimeoutException -> viewState.setConnectionLostMessage()
                        }
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