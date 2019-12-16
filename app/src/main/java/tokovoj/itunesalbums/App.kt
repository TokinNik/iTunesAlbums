package tokovoj.itunesalbums

import android.app.Application
import tokovoj.itunesalbums.di.DaggerNetworkComponent
import tokovoj.itunesalbums.di.NetworkComponent
import tokovoj.itunesalbums.di.NetworkModule

class App: Application() {

   companion object{
       lateinit var networkComponent: NetworkComponent
   }

    override fun onCreate() {
        super.onCreate()
        networkComponent = buildComponent()
    }

    private fun buildComponent(): NetworkComponent = DaggerNetworkComponent
        .builder()
        .networkModule( NetworkModule() )
        .build()
}
