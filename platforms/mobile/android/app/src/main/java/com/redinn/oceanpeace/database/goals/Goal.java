package com.redinn.oceanpeace.database.goals;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.redinn.oceanpeace.goals.Goals;

import org.json.JSONObject;

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

    public String name;
    public String apps;
    @ColumnInfo(name = "websites")
    public String websites;
    @ColumnInfo(name = "limit")
    public String limit;
    @ColumnInfo(name = "active_days")
    public String activeDays;
    public String type;
    public String sessionUpdate;
    public String sessionTime;
    public String sessionHistory;



    @Ignore
    public JSONObject toJSON() throws Exception {
        JSONObject ret = new JSONObject();

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
}
