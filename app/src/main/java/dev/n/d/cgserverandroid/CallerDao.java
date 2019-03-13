package dev.n.d.cgserverandroid;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CallerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Caller caller);

    @Query("SELECT * from caller_table ORDER BY call_datetime DESC")
    LiveData<List<Caller>> getAllCallers();

    @Query("SELECT * from caller_table ORDER BY call_datetime DESC")
    List<Caller> getAllCallersAsList();

    @Query("UPDATE caller_table SET call_from_CGNet = 1 WHERE (primary_key = :primaryKey)")
    void callFromCGNet(String primaryKey);

    @Query("UPDATE caller_table SET call_from_IMI = 1 WHERE (primary_key = :primaryKey)")
    void callFromIMI(String primaryKey);

    @Query("UPDATE caller_table SET response_CGNet = :response WHERE (primary_key = :primaryKey)")
    void updateResponseCGNet(String primaryKey, String response);

    @Query("UPDATE caller_table SET response_IMI = :response WHERE (primary_key = :primaryKey)")
    void updateResponseIMI(String primaryKey, String response);

    @Query("UPDATE caller_table SET api_datetime = :apiDatetime WHERE (primary_key = :primaryKey)")
    void updateApiDatetime(String primaryKey, String apiDatetime);

    @Query("UPDATE caller_table SET successful_callback_datetime = :successfulCallbackDatetime WHERE (primary_key = :primaryKey)")
    void updateSuccessfulCallbackDatetime(String primaryKey, String successfulCallbackDatetime);

    @Query("DELETE FROM caller_table")
    void deleteAll();

    @Query("DELETE FROM caller_table WHERE (primary_key = :primaryKey)")
    void deleteAnEntry(String primaryKey);

}
