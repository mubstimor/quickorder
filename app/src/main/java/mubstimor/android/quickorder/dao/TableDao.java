package mubstimor.android.quickorder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import mubstimor.android.quickorder.models.Table;

@Dao
public interface TableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Table table);

    @Query("DELETE FROM `table`")
    void deleteAll();

    @Query("SELECT * FROM `table` ORDER BY id ASC")
    LiveData<List<Table>> getAllTables();

}
