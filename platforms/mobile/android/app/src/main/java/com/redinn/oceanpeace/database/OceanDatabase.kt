package com.redinn.oceanpeace.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.redinn.oceanpeace.database.goals.Goal
import com.redinn.oceanpeace.database.goals.GoalDAO
import com.redinn.oceanpeace.database.icons.Icon
import com.redinn.oceanpeace.database.icons.IconDAO

@Database(
    entities = [Goal::class, Icon::class],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class OceanDatabase : RoomDatabase() {
    // MIGRATION
    // DAOs
    abstract fun goalDAO(): GoalDAO
    abstract fun iconDAO(): IconDAO

    companion object {
        private var instance: OceanDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): OceanDatabase {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    OceanDatabase::class.java,
                    "ocean-database"
                ).allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}
