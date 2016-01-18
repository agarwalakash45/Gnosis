package ayondas2k14.gnosis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.sql.SQLException;

//Adapter class to use existing DATABASE in the /data/data/packagename/dbname
public class QueDBAdapter {
    private final Context mContext;
    private SQLiteDatabase mDb;
    private QuesDBHandler mDbHandler;

    //Constructor
    public QueDBAdapter(Context context)
    {
        this.mContext = context;
        mDbHandler = new QuesDBHandler(mContext);
    }

    public QueDBAdapter createDatabase() throws SQLException
    {
        try{
            mDbHandler.createDataBase();        //Call createDataBase() method of QuesDBHandler to create DATABASE if it doesn't exist
        }

        catch (IOException mIOException){
//            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    //Method to open existing database
    public QueDBAdapter open() throws SQLException
    {
        try
        {
            mDbHandler.openDataBase();
            mDbHandler.close();
            mDb = mDbHandler.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
     //       Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHandler.close();
    }

    public Cursor getQueByCategory(String category)
    {
        String sql ="SELECT * FROM "  + QuesDBHandler.TABLE_QUES + " WHERE " +
                QuesDBHandler.COLUMN_CATEGORY + "=\"" + category + "\";";

        Cursor mCur = mDb.rawQuery(sql, null);

        mCur.moveToFirst();
        if(mCur.getCount()>0)
            Log.d("Akash","Entries exists!!");

        return mCur;
    }
}
