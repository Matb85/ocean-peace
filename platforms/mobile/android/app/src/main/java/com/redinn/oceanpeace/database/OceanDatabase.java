package com.redinn.oceanpeace.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.redinn.oceanpeace.database.goals.Goal;
import com.redinn.oceanpeace.database.goals.GoalDAO;
import com.redinn.oceanpeace.database.icons.Icon;
import com.redinn.oceanpeace.database.icons.IconDAO;

@androidx.room.Database(
        entities = {Goal.class, Icon.class},
        version = 1,
        exportSchema = true,
        autoMigrations = {
//                @AutoMigration(
//                        from = 2,
//                        to = 2
//                )
        }
)
public abstract class OceanDatabase extends RoomDatabase {
    private static OceanDatabase instance;

    public static synchronized OceanDatabase getDatabase(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), OceanDatabase.class, "ocean-database").allowMainThreadQueries().build();
        }
        return instance;
    }

    // MIGRATION


    // DAOs
    public abstract GoalDAO goalDAO();

    public abstract IconDAO iconDAO();
}
