package ayondas2k14.gnosis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Class to create DB Handler to store user profile information
public class UserDBHandler extends SQLiteOpenHelper{
    private static int DATABASE_VERSION=3;
    private static String DATABASE_NAME="users.db";
    private static String TABLE_USERS="users";
    public static String COLUMN_NAME="name";
    public static String COLUMN_IMAGE="image";

    public UserDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
                COLUMN_NAME + " TEXT , " +
                COLUMN_IMAGE + " BLOB);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="DROP TABLE IF EXISTS " + TABLE_USERS + ";";
        db.execSQL(query);
        onCreate(db);
    }

    //Method to save Bitmap Image as byte [] in database
    public void saveImageBitmap(byte [] image){
        SQLiteDatabase db=getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(COLUMN_IMAGE,image);

        db.insert(TABLE_USERS, null, values);

        db.close();
    }

    //Method to return byte [] for image
    public byte[] getImage(){
        SQLiteDatabase db=getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM "+TABLE_USERS+";",null);

        c.moveToLast();

        if(c.getCount()==0)
            return null;
        else
            return c.getBlob(c.getColumnIndex(COLUMN_IMAGE));
    }
}