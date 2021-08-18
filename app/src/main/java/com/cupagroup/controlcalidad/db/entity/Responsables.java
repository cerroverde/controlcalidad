package com.cupagroup.controlcalidad.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.cupagroup.controlcalidad.utils.Constants;

import java.io.Serializable;

@Entity(tableName = Constants.TABLE_NAME_RESPONSABLES)
public class Responsables implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long contact_id;

    @ColumnInfo(name = "resp_id")
    private long resp_id;

    @ColumnInfo(name = "nave_id")
    private long nave_id;

    @ColumnInfo(name = "resp_name")
    private String resp_name;

    @ColumnInfo(name = "resp_email")
    private String resp_email;

    public Responsables(long resp_id, long nave_id, String resp_name, String resp_email) {
        this.resp_id = resp_id;
        this.nave_id = nave_id;
        this.resp_name = resp_name;
        this.resp_email = resp_email;
    }

    public long getContact_id() {
        return contact_id;
    }

    public void setContact_id(long contact_id) {
        this.contact_id = contact_id;
    }

    public long getResp_id() {
        return resp_id;
    }

    public void setResp_id(long resp_id) {
        this.resp_id = resp_id;
    }

    public long getNave_id() {
        return nave_id;
    }

    public void setNave_id(long nave_id) {
        this.nave_id = nave_id;
    }

    public String getResp_name() {
        return resp_name;
    }

    public void setResp_name(String resp_name) {
        this.resp_name = resp_name;
    }

    public String getResp_email() {
        return resp_email;
    }

    public void setResp_email(String resp_email) {
        this.resp_email = resp_email;
    }
}
