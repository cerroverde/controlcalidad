package com.cupagroup.controlcalidad.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cupagroup.controlcalidad.activities.CanterasListClass;
import com.cupagroup.controlcalidad.activities.NavesListClass;
import com.cupagroup.controlcalidad.adapters.AdaptadorDirecciones;
import com.cupagroup.controlcalidad.db.entity.Naves;

import java.util.List;

@Dao
public interface NavesDao {
    @Query("SELECT DISTINCT(name) FROM naves ORDER BY name ASC")
    List<String> getAllNavesEntries();

    @Query("SELECT id FROM naves WHERE name LIKE :naveName")
    Long getIdByNaveName(String naveName);

    @Query("SELECT id, name,address FROM naves WHERE id_cantera = :id_cantera ORDER BY LOWER(id)")
    List<NavesListClass> getAll(Long id_cantera);

    @Query("SELECT address FROM naves WHERE id = :id_naves")
    String getAddressFromId(Long id_naves);

    @Query("SELECT id_cantera FROM naves WHERE id = :id_naves")
    String getCanterasByIdNave(Long id_naves);

    @Insert
    long insert(Naves naves);

    @Update
    void update(Naves naves);

    @Delete
    void delete(Naves naves);

    @Query("DELETE FROM naves")
    void deleteAll();
}