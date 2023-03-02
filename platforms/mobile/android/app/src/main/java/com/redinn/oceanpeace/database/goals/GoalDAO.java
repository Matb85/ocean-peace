package com.redinn.oceanpeace.database.goals;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

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

    @Query("UPDATE goal SET session_time = session_time + :time WHERE apps LIKE '%'||:appName||'%' ")
    Completable updateGoalSessionTime(String appName, long time);

    @Query("SELECT (`limit`*60*1000 - (session_time)) AS time_left FROM goal WHERE apps LIKE '%'||:appName||'%' ORDER BY time_left ASC LIMIT 1")
    Single<Long> getTimeLeftForApp(String appName);
}
