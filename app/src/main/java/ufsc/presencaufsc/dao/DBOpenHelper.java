package ufsc.presencaufsc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context) {
        super(context, "DBLOCAL", null, 1);
    }

    /* Criação das tabelas */
    @Override
    public void onCreate(SQLiteDatabase db) {

        ArrayList<String> listaSqls = ScriptDLL.getCreateTableTarefa();
        db.execSQL(listaSqls.get(0));
        db.execSQL(listaSqls.get(1));
        db.execSQL(listaSqls.get(2));
        db.execSQL(listaSqls.get(3));
        db.execSQL(listaSqls.get(4));
    }

    /* Para atualização do banco de dados */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}