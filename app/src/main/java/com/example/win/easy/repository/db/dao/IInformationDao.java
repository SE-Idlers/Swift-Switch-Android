package com.example.win.easy.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.win.easy.repository.db.pojo.IInformation;

@Dao
public interface IInformationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(IInformation iInformation);

    @Query("SELECT * FROM iinformation WHERE id=:iInformationId")
    IInformation findById(long iInformationId);
}
