package com.redinn.oceanpeace.database.icons;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.getcapacitor.JSObject;

@Entity
public class Icon {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "package_name")
    public String packageName;

    @ColumnInfo(name = "label")
    public String label;
    @ColumnInfo(name = "icon_path")
    public String iconPath;
    @ColumnInfo(name = "version")
    public String version;

    @Ignore
    public JSObject toJSON() {
        JSObject ret = new JSObject();
        ret.put("packageName", packageName);
        ret.put("label", label);
        ret.put("iconPath", iconPath);
        ret.put("version", version);

        return ret;
    }

    @Ignore
    public Icon(@NonNull String packageName,
                String label,
                String iconPath,
                String version) {
        this.packageName = packageName;
        this.label = label;
        this.iconPath = iconPath;
        this.version = version;
    }
    @Ignore
    public Icon(@NonNull String packageName) {
        this.packageName = packageName;
    }
    public Icon(){}
}
