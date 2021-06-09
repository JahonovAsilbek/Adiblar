package uz.revolution.adiblar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.revolution.adiblar.database.daos.LiteratureDao
import uz.revolution.adiblar.database.entities.Literature

@Database(entities = [Literature::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): LiteratureDao

    companion object {

        @Volatile
        private var database: AppDatabase? = null

        fun init(context: Context) {
            synchronized(this) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "saved.db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
        }
    }

    object get {
        fun getDatabase(): AppDatabase {
            return database!!
        }
    }
}