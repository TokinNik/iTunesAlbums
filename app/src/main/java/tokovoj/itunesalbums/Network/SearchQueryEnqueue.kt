package tokovoj.itunesalbums.Network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tokovoj.itunesalbums.Data.AlbumData

class SearchQueryEnqueue(val callback: SearchQueryCallback) : Callback<AlbumData>
{
    override fun onResponse(call: Call<AlbumData>, response: Response<AlbumData>)
    {
        if (response.isSuccessful && response.body() != null)
        {
            callback.onComplete(response.body()!!.resultCount, response.body()!!.results)
        }
        else
        {
            callback.onCompleteError(response.code())
        }
    }

    override fun onFailure(call: Call<AlbumData>, t: Throwable)
    {
        callback.onFailture()
    }

}