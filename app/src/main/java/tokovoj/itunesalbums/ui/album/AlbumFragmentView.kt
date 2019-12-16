package tokovoj.itunesalbums.ui.album

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import tokovoj.itunesalbums.data.Results

@StateStrategyType(AddToEndStrategy::class)
interface AlbumFragmentView : MvpView {

    fun setSongs(count: Int, items: List<Results>)

    fun showProgressBar()

    fun hideProgressBar()

    fun setErrorMessage(code: Int)

    fun setConnectionLostMessage()

    fun setNoResultMessage()

    fun setBadRequestMessage()

    fun setServerErrorMessage()

}