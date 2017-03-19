package com.example.new_home.termin30_test.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by New_home on 12.3.2017.
 */
@DatabaseTable(tableName = Filmovi.TABLE_NAME_USERS)
public class Filmovi {
    public static final String TABLE_NAME_USERS  = "filmovi";
    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_NAME   = "ime filma";
    public static final String FIELD_NAME_TYPE   = "zanr";
    public static final String FIELD_NAME_YEAR   = "godina izlaska filma";
    public static final String FIELD_NAME_ACTOR  = "glumci";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int idFilm;
    @DatabaseField(columnName = FIELD_NAME_TYPE)
    private String zanrFilma;
    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String imeFilma;
    @DatabaseField(columnName = FIELD_NAME_YEAR)
    private String godinaFilma;
    @DatabaseField(columnName = FIELD_NAME_ACTOR, foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Glumci glumci;

    public Filmovi (){}

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public String getZanrFilma() {
        return zanrFilma;
    }

    public void setZanrFilma(String zanrFilma) {
        this.zanrFilma = zanrFilma;
    }

    public String getImeFilma() {
        return imeFilma;
    }

    public void setImeFilma(String imeFilma) {
        this.imeFilma = imeFilma;
    }

    public String getGodinaFilma() {
        return godinaFilma;
    }

    public void setGodinaFilma(String godinaFilma) {
        this.godinaFilma = godinaFilma;
    }

    public Glumci getGlumci() {
        return glumci;
    }

    public void setGlumci(Glumci glumci) {
        this.glumci = glumci;
    }

    @Override
    public String toString() {
        return imeFilma;
    }
}
