package com.cupagroup.controlcalidad.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cupagroup.controlcalidad.activities.CanterasListClass;
import com.cupagroup.controlcalidad.db.entity.Canteras;

import java.util.List;

@Dao
public interface CanterasDao {
    @Ignore
    @Query("SELECT id, name FROM canteras")
    List<CanterasListClass> getAllCanteras();

    @Query("SELECT name FROM canteras WHERE id = :id_cantera")
    String getCanteraByid(String id_cantera);

    @Insert
    long insert(Canteras canteras);

    @Update
    void update(Canteras canteras);

    @Delete
    void delete(Canteras canteras);

    @Query("DELETE FROM canteras")
    void deleteAll();
}
