package com.redinn.oceanpeace.database.icons

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.getcapacitor.JSObject

@Entity
class Icon {
    @PrimaryKey
    @ColumnInfo(name = "package_name")
    var packageName: String

    @ColumnInfo(name = "label")
    lateinit var label: String

    @ColumnInfo(name = "icon_path")
    lateinit var iconPath: String

    @ColumnInfo(name = "version")
    lateinit var version: String

    @Ignore
    fun toJSON(): JSObject {
        val res = JSObject()
        res.put("packageName", packageName)
        res.put("label", label)
        res.put("iconPath", iconPath)
        res.put("version", version)
        return res
    }

    constructor(
        packageName: String,
        label: String,
        iconPath: String,
        version: String
    ) {
        this.packageName = packageName
        this.label = label
        this.iconPath = iconPath
        this.version = version
    }

    @Ignore
    constructor(packageName: String) {
        this.packageName = packageName
    }
}
