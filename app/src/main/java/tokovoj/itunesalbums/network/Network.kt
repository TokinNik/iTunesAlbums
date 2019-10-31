package tokovoj.itunesalbums.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import tokovoj.itunesalbums.AppModel
import tokovoj.itunesalbums.data.AlbumData

class Network : AppModel.Model
{
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var itunesApi: ItunesApi = retrofit.create()

    override fun searchAlbums(term: String, callback: SearchQueryCallback)
    {
        val albumData: Call<AlbumData> = itunesApi.searchAlbums(term, "music", "album", 200)
        albumData.enqueue(SearchQueryEnqueue(callback))
    }

    override fun getAlbumSongs(collectionId: Long, callback: SearchQueryCallback)
    {
        val albumData: Call<AlbumData> = itunesApi.getAlbumById(collectionId, "song", 200)
        albumData.enqueue(SearchQueryEnqueue(callback))
    }


}