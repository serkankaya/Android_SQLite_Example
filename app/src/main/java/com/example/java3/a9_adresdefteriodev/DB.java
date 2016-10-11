package com.example.java3.a9_adresdefteriodev;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    private static String dbName = "proje";
    private static Integer versiyon = 1;

    public SQLiteDatabase oku() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase yaz() {
        return this.getWritableDatabase();
    }

    public DB(Context context) {
        super(context, dbName + ".db", null, versiyon);
    }

    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName + ".db", null, versiyon);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `adresler` (\n" +
                "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`adresbaslik`\tTEXT,\n" +
                "\t`il`\tTEXT,\n" +
                "\t`ilce`\tTEXT,\n" +
                "\t`sokak`\tTEXT,\n" +
                "\t`adres`\tTEXT\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists adresler");
        onCreate(db);
    }

    // data getirme
    public Cursor dataGetir(String tableName,String query){
        return oku().rawQuery("select * from "+tableName+" "+query,null);
    }

    // data silme
    public int sil(String tableName,String columnName,String val){
        return yaz().delete(tableName,columnName+"=?",new String[]{val});
    }
}
