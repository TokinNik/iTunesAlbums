package tokovoj.itunesalbums.di

import dagger.Module
import dagger.Provides
import tokovoj.itunesalbums.network.Network

@Module
class NetworkModule {

    @Provides
    fun provideNetwork(): Network = Network()

}