package com.redinn.oceanpeace.database.goals

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface GoalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg goals: Goal?)

    @Update
    fun update(vararg goals: Goal?)

    @Delete
    fun delete(goal: Goal?)

    @get:Query("SELECT * FROM goal ORDER BY name ASC")
    val allGoals: List<Goal?>?

    @get:Query("SELECT name FROM goal")
    val allGoalsName: List<String?>?

    @Query("SELECT * FROM goal WHERE id = :id")
    fun getGoalByName(id: String?): Goal?

    @Query("UPDATE goal SET session_time = session_time + :time WHERE apps LIKE '%'||:appName||'%' ")
    fun updateGoalSessionTime(appName: String?, time: Long): Completable?

    @Query("SELECT (`limit`*60*1000 - (session_time)) AS time_left FROM goal WHERE apps LIKE '%'||:appName||'%' ORDER BY time_left ASC LIMIT 1")
    fun getTimeLeftForApp(appName: String?): Single<Any>?
}
