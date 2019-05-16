package com.example.win.easy.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.win.easy.repository.db.pojo.IInformation;

import java.util.Collection;

@Dao
public interface IInformationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(IInformation iInformation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Collection<IInformation> iInformations);

    @Query("SELECT * FROM iinformation WHERE id=:iInformationId")
    IInformation findById(long iInformationId);

    @Update
    void update(IInformation iInformation);

    @Update
    void update(Collection<IInformation> iInformations);
}
