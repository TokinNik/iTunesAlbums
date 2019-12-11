package tokovoj.itunesalbums.network

import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import tokovoj.itunesalbums.data.AlbumData

interface ItunesApi
{
    @GET("search")
    fun searchAlbums(
        @Query("term") term: String,
        @Query("media") media: String,
        @Query("entity") entity: String,
        @Query("limit") limit: Int
    ): Single<Response<AlbumData>>

    @GET("lookup")
    fun getAlbumById(
        @Query("id") id: Long,
        @Query("entity") entity: String,
        @Query("limit") limit: Int
    ): Single<Response<AlbumData>>

    class Builder(
       val baseUrl: String
    ){
        fun build(): ItunesApi
        {
            return (Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()).create()
        }
    }

}