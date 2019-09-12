package mubstimor.android.quickorder.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import mubstimor.android.quickorder.dao.TableDao;
import mubstimor.android.quickorder.models.Table;

public class TableRepository {

    private TableDao mTableDao;
    private LiveData<List<Table>> mAllTables;

    TableRepository(Application application){
        QuickRoomDatabase db = QuickRoomDatabase.getQuickRoomDatabase(application);
        mTableDao = db.tableDao();
        mAllTables = mTableDao.getAllTables();
    }

    LiveData<List<Table>> getAllTables(){
        return mAllTables;
    }

    private void insert(Table table) {
        new insertAsyncTask(mTableDao).execute(table);
    }

    private static class insertAsyncTask extends AsyncTask<Table, Void, Void> {

        private TableDao mAsyncTaskDao;

        insertAsyncTask(TableDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Table... tables) {
            mAsyncTaskDao.insert(tables[0]);
            return null;
        }
    }

}
