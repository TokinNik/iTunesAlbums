package tokovoj.itunesalbums.data

class Results(val wrapperType: String,
              val collectionType: String,
              val kind: String,
              val artistId: Long,
              val collectionId: Long,
              val amgArtistId: Long,
              val artistName: String,
              val trackName: String,
              val collectionName: String,
              val collectionCensoredName: String,
              val artistViewUrl: String,
              val previewUrl: String,
              val collectionViewUrl: String,
              val artworkUrl60: String?,
              val artworkUrl100: String?,
              val collectionPrice: Float,
              val collectionExplicitness: String,
              val trackCount: Int,
              val trackNumber: Int,
              val trackTimeMillis: Long,
              val copyright: String,
              val country: String,
              val currency : String,
              val releaseDate: String,
              val primaryGenreName: String)

{

}
