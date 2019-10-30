package tokovoj.itunesalbums.Network

import tokovoj.itunesalbums.Data.Results


interface SearchAlbumsCallback
{
    fun onComplete(count: Int, items: List<Results>)

    fun onCompleteError(code: Int)

    fun onFailture()
}