package com.redinn.oceanpeace.database.icons

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.getcapacitor.JSArray


@Dao
interface IconDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg icons: Icon?)

    @Update
    fun update(vararg icons: Icon?)

    @Delete
    fun delete(icons: Icon?)

    @MapInfo(keyColumn = "package_name")
    @Query("SELECT * FROM icon ORDER BY label ASC")
    fun getAllIcons(): Map<String, Icon>

    @Query("SELECT * FROM icon WHERE package_name = :packageName")
    fun getIcon(packageName: String): Icon

    @MapInfo(keyColumn = "package_name", valueColumn = "label")
    @Query("SELECT package_name, label FROM icon")
    fun getAppsLabels(): Map<String, String>

    @Ignore
    fun getAllIconsJSON(): JSArray {
        val ret = JSArray()
        val icons = getAllIcons()
        for (icon in icons.values) {
            ret.put(icon.toJSON())
        }
        return ret
    }
}
