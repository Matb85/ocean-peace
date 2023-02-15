package com.redinn.oceanpeace.database.icons;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.getcapacitor.JSArray;

import java.util.Map;

@Dao
public interface IconDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Icon... icons);

    @Update
    void update(Icon... icons);

    @Delete
    void delete(Icon icons);

    @MapInfo(keyColumn = "package_name")
    @Query("SELECT * FROM icon ORDER BY label ASC")
    Map<String, Icon> getAllIcons();

    @Query("SELECT * FROM icon WHERE package_name = :packageName")
    Icon getIcon(String packageName);

    @MapInfo(keyColumn = "package_name", valueColumn = "label")
    @Query("SELECT package_name, label FROM icon")
    Map<String, String> getAppsLabels();

    default JSArray getAllIcons_JSON() {
        JSArray ret = new JSArray();

        Map<String, Icon> icons = getAllIcons();

        for (Icon icon : icons.values()) {
            ret.put(icon.toJSON());
        }

        return ret;
    }
}
