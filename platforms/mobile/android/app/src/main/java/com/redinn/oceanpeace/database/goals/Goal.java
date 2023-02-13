package com.redinn.oceanpeace.database.goals;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.getcapacitor.JSObject;
import com.redinn.oceanpeace.goals.Goals;

/**
 * Class providing saving, loading and deleting functions for goals. <br>
 *  <br>
 * Goal basic structure:<br>
 *
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
    @NonNull
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
    public String sessionTime;
    @ColumnInfo(name = "session_history")
    public String sessionHistory;



    @Ignore
    public JSObject toJSON() {
        JSObject ret = new JSObject();

        ret.put(Goals.ID, id);
        ret.put(Goals.NAME, name);
        ret.put(Goals.APPS, apps);
        ret.put(Goals.WEBSITES, websites);
        ret.put(Goals.LIMIT, limit);
        ret.put(Goals.LIMITACTIONTYPE, type);
        ret.put(Goals.ACTIVEDAYS, activeDays);
        ret.put(Goals.SESSIONUPDATE, sessionUpdate);
        ret.put(Goals.SESSIONTIME, sessionTime);
        ret.put(Goals.SESSIONSHISTORY, sessionHistory);

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
