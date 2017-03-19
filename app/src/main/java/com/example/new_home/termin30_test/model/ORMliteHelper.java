package com.example.new_home.termin30_test.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by New_home on 12.3.2017.
 */

public class ORMliteHelper extends OrmLiteSqliteOpenHelper{


    private static final String DATABASE_NAME    = "priprema.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Filmovi, Integer> mFilmoviDao = null;
    private Dao<Glumci, Integer> mGlumciDao = null;

    public ORMliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Filmovi.class);
            TableUtils.createTable(connectionSource, Glumci.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Filmovi.class, true);
            TableUtils.dropTable(connectionSource, Glumci.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Filmovi, Integer> getFilmoviDao() throws SQLException {
        if (mFilmoviDao == null) {
            mFilmoviDao = getDao(Filmovi.class);
        }

        return mFilmoviDao;
    }

    public Dao<Glumci, Integer> getGlumciDao() throws SQLException {
        if (mGlumciDao == null) {
            mGlumciDao = getDao(Glumci.class);
        }

        return mGlumciDao;
    }

    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        mFilmoviDao = null;
        mGlumciDao = null;

        super.close();
    }
}
