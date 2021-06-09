package uz.revolution.adiblar.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import uz.revolution.adiblar.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // disable night mode settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        AppDatabase.init(this)
    }
}