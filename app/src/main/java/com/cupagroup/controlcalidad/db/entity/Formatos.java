package com.cupagroup.controlcalidad.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cupagroup.controlcalidad.utils.Constants;

import java.io.Serializable;

@Entity(tableName = Constants.TABLE_NAME_FORMATOS)
public class Formatos implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long formato_id;

    @ColumnInfo(name = "nave_id")
    private long nave_id;

    @ColumnInfo(name = "formato_name")
    private String formato_name;

    public Formatos(long formato_id, long nave_id, String formato_name) {
        this.formato_id = formato_id;
        this.nave_id = nave_id;
        this.formato_name = formato_name;
    }

    @Ignore
    public Formatos(){
        // Emtpy
    }

    public long getFormato_id() {
        return formato_id;
    }

    public void setFormato_id(long formato_id) {
        this.formato_id = formato_id;
    }

    public long getNave_id() {
        return nave_id;
    }

    public void setNave_id(long nave_id) {
        this.nave_id = nave_id;
    }

    public String getFormato_name() {
        return formato_name;
    }

    public void setFormato_name(String formato_name) {
        this.formato_name = formato_name;
    }
}
