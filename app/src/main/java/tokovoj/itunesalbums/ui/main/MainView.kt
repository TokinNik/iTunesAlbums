package tokovoj.itunesalbums.ui.main

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import tokovoj.itunesalbums.data.Results

@StateStrategyType(AddToEndStrategy::class)
interface MainView : MvpView {


    fun setAlbums(count: Int, items: List<Results>)

    fun showProgressBar()

    fun hideKeyboard()

    fun hideProgressBar()

    fun setErrorMessage(code: Int)

    fun setConnectionLostMessage()

    fun setNoResultMessage()

    fun setBadRequestMessage()

    fun setServerErrorMessage()
}