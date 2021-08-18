package com.cupagroup.controlcalidad.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cupagroup.controlcalidad.db.entity.Calidad;

import java.util.List;

@Dao
public interface CalidadDao {

    @Query("SELECT calidad_name FROM calidad ORDER BY LOWER(calidad_id)")
    List<String> getAll();

    @Query("SELECT COUNT(*) FROM calidad")
    Integer getAllCount();

    @Insert
    long insert(Calidad calidad);

    @Update
    void update(Calidad calidad);

    @Delete
    void delete(Calidad calidad);

    @Query("DELETE FROM calidad")
    void deleteAll();
}
