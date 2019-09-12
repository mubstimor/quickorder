package mubstimor.android.quickorder.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import mubstimor.android.quickorder.dao.TableDao;
import mubstimor.android.quickorder.models.Table;

@Database(entities = {Table.class}, version = 1)
public abstract class QuickRoomDatabase extends RoomDatabase {

    public abstract TableDao tableDao();

    public static volatile QuickRoomDatabase INSTANCE;

    static QuickRoomDatabase getQuickRoomDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (QuickRoomDatabase.class) {
                if (INSTANCE == null) {
                    // create db
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            QuickRoomDatabase.class, "quick_database")
//                            .addCallback(sRoomDatabaseCallBack)
//                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
