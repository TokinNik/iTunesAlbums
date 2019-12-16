package tokovoj.itunesalbums.di

import dagger.Component
import tokovoj.itunesalbums.ui.album.AlbumFragmentPresenter
import tokovoj.itunesalbums.ui.main.MainPresenter

@Component(modules = [NetworkModule::class])

interface NetworkComponent {

    fun inject(mainPresenter: MainPresenter)

    fun inject(albumFragmentPresenter: AlbumFragmentPresenter)

}