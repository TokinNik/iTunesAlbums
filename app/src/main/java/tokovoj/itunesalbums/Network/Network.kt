package tokovoj.itunesalbums.Network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import tokovoj.itunesalbums.Data.AlbumData

class Network()
{
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var itunesApi: ItunesApi = retrofit.create()

    fun searchAlbums(term: String, callback: SearchAlbumsCallback)
    {
        val albumData: Call<AlbumData> = itunesApi.searchAlbums(term, "music", "album", 200)
        albumData.enqueue(SearchAlbumsEnqueue(callback))
    }

    fun getAlbumById(id: Long, callback: SearchAlbumsCallback)
    {
        val albumData: Call<AlbumData> = itunesApi.getAlbumById(id, "album")
        albumData.enqueue(GetAlbumByIdEngueue(callback))
    }

    fun getAlbumSongs(collectionId: Long, callback: SearchAlbumsCallback)
    {
        val albumData: Call<AlbumData> = itunesApi.getAlbumById(collectionId, "song")
        albumData.enqueue(SearchAlbumsEnqueue(callback))
    }


}