package com.cupagroup.controlcalidad.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cupagroup.controlcalidad.utils.Constants;

import java.io.Serializable;

@Entity(tableName = Constants.TABLE_NAME_NAVES)
public class Naves implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "id_cantera")
    private String id_cantera;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "phone")
    private String phone;


    public Naves(long id, String id_cantera, String name, String address, String phone) {
        this.id = id;
        this.id_cantera = id_cantera;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    @Ignore
    public Naves(){
        //Empty
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getId_cantera() {
        return id_cantera;
    }

    public void setId_cantera(String id_cantera) {
        this.id_cantera = id_cantera;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
