package com.cupagroup.controlcalidad.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cupagroup.controlcalidad.db.entity.Formatos;

import java.util.List;

@Dao
public interface FormatosDao {
    @Query("SELECT COUNT(*) FROM formatos")
    Integer getAllCount();

    @Query("SELECT COUNT(*) FROM formatos WHERE nave_id = :nave_id")
    Integer getAllCountByNaveId(Long nave_id);

    @Query("SELECT formato_name FROM formatos WHERE nave_id = :nave_id ")
    List<String> getAllByNaveId(Long nave_id);

    @Insert
    long insert(Formatos formatos);

    @Update
    void update(Formatos formatos);

    @Delete
    void delete(Formatos formatos);

    @Query("DELETE FROM formatos")
    void deleteAll();
}
