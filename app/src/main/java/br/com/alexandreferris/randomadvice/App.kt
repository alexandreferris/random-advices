package br.com.alexandreferris.randomadvice

import android.app.Application
import br.com.alexandreferris.randomadvice.di.AppComponent
import br.com.alexandreferris.randomadvice.di.AppModule
import br.com.alexandreferris.randomadvice.di.DaggerAppComponent
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.MobileAds
import io.fabric.sdk.android.Fabric

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // AdMob
        MobileAds.initialize(this, "ca-app-pub-5204818658708327~1040076478")

        // Fabrioc.IO
        Fabric.with(this, Crashlytics())
        appComponent = initDagger(this)
    }

    private fun initDagger(app: App): AppComponent =
            DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
}