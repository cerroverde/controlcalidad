package com.cupagroup.controlcalidad.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cupagroup.controlcalidad.db.entity.Responsables;

@Dao
public interface ResponsableDao {
    @Query("SELECT resp_name FROM responsable WHERE nave_id == :nave_id")
    String getResponsableNameByNaveID(long nave_id);

    @Query("SELECT resp_email FROM responsable WHERE nave_id == :nave_id")
    String getResponsableEmailByNaveID(long nave_id);

    @Insert
    long insert(Responsables responsables);

    @Update
    void update(Responsables responsables);

    @Delete
    void delete(Responsables responsables);

    @Query("DELETE FROM responsable")
    void deleteAll();
}
