package com.redinn.oceanpeace.database.goals;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.getcapacitor.JSObject;

/**
 * Class providing saving, loading and deleting functions for  <br>
 * <br>
 * Goal basic structure:<br>
 * <p>
 * {<br>
 * &emsp;"id": "goal1676156268732",<br>
 * &emsp;"name": "Hdhs",<br>
 * &emsp;"apps": "[\"pl.allegro\"]",<br>
 * &emsp;"websites": "[]",<br>
 * &emsp;"limit": "75",<br>
 * &emsp;"activeDays": "[\"Wed\"]",<br>
 * &emsp;"limitActionType": "Notification",<br>
 * &emsp;"sessionUpdate": "",<br>
 * &emsp;"sessionTime": "0",<br>
 * &emsp;"sessionHistory": "[]"<br>
 * }
 */

@Entity
public class Goal {
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "apps")
    public String apps;
    @ColumnInfo(name = "websites")
    public String websites;
    @ColumnInfo(name = "limit")
    public String limit;
    @ColumnInfo(name = "active_days")
    public String activeDays;
    @ColumnInfo(name = "type")
    public String type;
    @ColumnInfo(name = "session_update")
    public String sessionUpdate;
    @ColumnInfo(name = "session_time")
    public long sessionTime;
    @ColumnInfo(name = "session_history")
    public String sessionHistory;


    /**
     * Returns JSON of goal, without <i>sessionTime</i> and <i>sessionUpdate</i>
     *
     * @return JSObject with goal
     */
    @Ignore
    public JSObject toJSON_forFE() {
        JSObject ret = new JSObject();

        ret.put(ID, id);
        ret.put(NAME, name);
        ret.put(APPS, apps);
        ret.put(WEBSITES, websites);
        ret.put(LIMIT, limit);
        ret.put(LIMIT_ACTION_TYPE, type);
        ret.put(ACTIVE_DAYS, activeDays);
        ret.put(SESSION_TIME, sessionTime);
        ret.put(SESSION_HISTORY, sessionHistory);

        Log.i("GOAL", ret.toString());

        return ret;
    }

    @Ignore
    public static String ID = "id";
    @Ignore
    public static String NAME = "name";
    @Ignore
    public static String APPS = "apps";
    @Ignore
    public static String WEBSITES = "websites";
    @Ignore
    public static String LIMIT = "limit";
    @Ignore
    public static String ACTIVE_DAYS = "activeDays";
    @Ignore
    public static String LIMIT_ACTION_TYPE = "limitActionType";
    @Ignore
    public static String SESSION_UPDATE = "sessionUpdate";
    @Ignore
    public static String SESSION_TIME = "sessionTime";
    @Ignore
    public static String SESSION_HISTORY = "sessionHistory";
}
