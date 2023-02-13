package com.redinn.oceanpeace.database.goals;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Goal... goals);

    @Update
    void update(Goal... goals);

    @Delete
    void delete(Goal goal);

    @Query("SELECT * FROM goal ORDER BY name ASC")
    List<Goal> getAllGoals();

    @Query("SELECT name FROM goal")
    List<String> getAllGoalsName();

    @Query("SELECT * FROM goal WHERE id = :id")
    Goal getGoalByName(String id);


}
