package tokovoj.itunesalbums.network

import io.reactivex.Single
import retrofit2.Response
import tokovoj.itunesalbums.data.AlbumData

class Network
{
    private var itunesApi = ItunesApi.Builder("https://itunes.apple.com/").build()

    fun searchAlbums(term: String): Single<Response<AlbumData>> = itunesApi.searchAlbums(term, "music", "album", 200)

    fun getAlbumById(collectionId: Long): Single<Response<AlbumData>> = itunesApi.getAlbumById(collectionId, "song", 200)
}