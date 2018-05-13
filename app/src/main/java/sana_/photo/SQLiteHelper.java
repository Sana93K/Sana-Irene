package sana_.photo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String Qdatas){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL(Qdatas);
    }

    public void insertData(String description , byte[] image){
        SQLiteDatabase database= getWritableDatabase();
        String Qdatas= "INSERT INTO IMAGES VALUES(NULL, ?, ?)";
        SQLiteStatement statement = database.compileStatement(Qdatas);
        statement.clearBindings();

        statement.bindString(2, description);
        statement.bindBlob(3,image);

        statement.executeInsert();
    }

    public Cursor getData(String Qdatas){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(Qdatas, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
