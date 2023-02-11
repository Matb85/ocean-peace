package com.redinn.oceanpeace.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.redinn.oceanpeace.database.goals.Goal;
import com.redinn.oceanpeace.database.goals.GoalDAO;

@androidx.room.Database(entities = {Goal.class}, version = 0_1)
public abstract class OceanDatabase extends RoomDatabase {
    private static OceanDatabase instance;

    public static synchronized OceanDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), OceanDatabase.class, "ocean-database").build();
        }
        return instance;
    }


    //DAOs
    public abstract GoalDAO goalDAO();
}
