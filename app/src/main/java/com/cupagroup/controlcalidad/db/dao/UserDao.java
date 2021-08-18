package com.cupagroup.controlcalidad.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cupagroup.controlcalidad.db.entity.User;
import com.cupagroup.controlcalidad.modelo.Credentials;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT username FROM user ORDER BY LOWER(user_id)")
    List<String> getAllEntries();

    @Ignore
    @Query("SELECT user_id FROM user ORDER BY LOWER(user_id)")
    List<Long> getAllEntryValues();

    @Query("SELECT user_id FROM user WHERE username LIKE :nombre LIMIT 1")
    Long getIdByName(String nombre);

    @Ignore
    @Query("SELECT email, password FROM user WHERE email LIKE :email")
    Credentials getCredentials(String email);

    @Query("SELECT email FROM user WHERE user_id = :user_id")
    String getEmailById(Long user_id);

    @Insert
    long insert(User user);

    @Ignore
    @Update
    void update(User user);

    @Ignore
    @Delete
    void delete(User users);

    @Ignore
    @Query("DELETE FROM user")
    void deleteAll();
}