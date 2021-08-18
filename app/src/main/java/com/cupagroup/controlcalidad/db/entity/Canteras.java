package com.cupagroup.controlcalidad.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cupagroup.controlcalidad.utils.Constants;

import java.io.Serializable;

@Entity(tableName = Constants.TABLE_NAME_CANTERAS)
public class Canteras implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    public Canteras(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Canteras(){
        // Nothing to do
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
