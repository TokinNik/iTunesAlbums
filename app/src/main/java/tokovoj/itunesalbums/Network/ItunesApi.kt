package tokovoj.itunesalbums.Network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tokovoj.itunesalbums.Data.AlbumData

interface ItunesApi
{
    @GET("search")
    fun searchAlbums(@Query("term") term: String, @Query("media") media: String, @Query("entity") entity: String, @Query("limit") limit: Int): Call<AlbumData>

    @GET("lookup")
    fun getAlbumById(@Query("id") id: Long, @Query("entity") entity: String, @Query("limit") limit: Int): Call<AlbumData>

}