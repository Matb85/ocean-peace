package com.redinn.oceanpeace.database.goals

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.getcapacitor.JSObject

/**
 * Class providing saving, loading and deleting functions for  <br></br>
 * <br></br>
 * Goal basic structure:<br></br>
 *
 *
 * {<br></br>
 * "id": "goal1676156268732",<br></br>
 * "name": "Hdhs",<br></br>
 * "apps": "[\"pl.allegro\"]",<br></br>
 * "websites": "[]",<br></br>
 * "limit": "75",<br></br>
 * "activeDays": "[\"Wed\"]",<br></br>
 * "limitActionType": "Notification",<br></br>
 * "sessionUpdate": "",<br></br>
 * "sessionTime": "0",<br></br>
 * "sessionHistory": "[]"<br></br>
 * }
 */
@Entity
class Goal {

    @PrimaryKey
    lateinit var id: String

    @ColumnInfo(name = "name")
    lateinit var name: String

    @ColumnInfo(name = "apps")
    lateinit var apps: String

    @ColumnInfo(name = "websites")
    lateinit var websites: String

    @ColumnInfo(name = "limit")
    lateinit var limit: String

    @ColumnInfo(name = "active_days")
    lateinit var activeDays: String

    @ColumnInfo(name = "type")
    lateinit var type: String

    @ColumnInfo(name = "session_update")
    lateinit var sessionUpdate: String

    @ColumnInfo(name = "session_time")
    var sessionTime: Long = 0

    @ColumnInfo(name = "session_history")
    lateinit var sessionHistory: String

    /**
     * Returns JSON of goal, without *sessionTime* and *sessionUpdate*
     *
     * @return JSObject with goal
     */
    @Ignore
    fun toJSON_forFE(): JSObject {
        val ret = JSObject()
        ret.put(ID, id)
        ret.put(NAME, name)
        ret.put(APPS, apps)
        ret.put(WEBSITES, websites)
        ret.put(LIMIT, limit)
        ret.put(LIMIT_ACTION_TYPE, type)
        ret.put(ACTIVE_DAYS, activeDays)
        ret.put(SESSION_TIME, sessionTime)
        ret.put(SESSION_HISTORY, sessionHistory)
        Log.i("GOAL", ret.toString())
        return ret
    }

    companion object {
        @Ignore
        var ID = "id"

        @Ignore
        var NAME = "name"

        @Ignore
        var APPS = "apps"

        @Ignore
        var WEBSITES = "websites"

        @Ignore
        var LIMIT = "limit"

        @Ignore
        var ACTIVE_DAYS = "activeDays"

        @Ignore
        var LIMIT_ACTION_TYPE = "limitActionType"

        @Ignore
        var SESSION_UPDATE = "sessionUpdate"

        @Ignore
        var SESSION_TIME = "sessionTime"

        @Ignore
        var SESSION_HISTORY = "sessionHistory"
    }
}
