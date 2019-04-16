package br.com.alexandreferris.randomadvice.di

import br.com.alexandreferris.randomadvice.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
}