package tokovoj.itunesalbums.network

import tokovoj.itunesalbums.data.Results


interface SearchQueryCallback
{
    fun onComplete(count: Int, items: List<Results>)

    fun onCompleteError(code: Int)

    fun onFailure()
}