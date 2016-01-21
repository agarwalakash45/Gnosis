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
    private SQLiteDatabase db;
    private QuesDBHandler mDbHandler;

    //Constructor
    public QueDBAdapter(Context context)
    {
        this.mContext = context;
        mDbHandler = new QuesDBHandler(mContext);
        try {
            createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            db = mDbHandler.getWritableDatabase();
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

    public Cursor getQueByCategory(String category,int quesno)
    {
        String sql ="SELECT * FROM "  + QuesDBHandler.TABLE_QUES + " WHERE " +
                QuesDBHandler.COLUMN_CATEGORY + "=\"" + category + "\" AND " +
                QuesDBHandler.COLUMN_QNO + "=" + quesno + ";";

        Cursor mCur = db.rawQuery(sql, null);

        mCur.moveToFirst();
        if(mCur.getCount()>0)
            Log.d("Akash","Entries exists!!");

        return mCur;
    }

    //Method to update user response in the marked field
    public void updateQueStatus(String category,int queno,int response){
        String query="UPDATE " + QuesDBHandler.TABLE_QUES + " SET " +
                QuesDBHandler.COLUMN_MARK + "=" + response + " WHERE " +
                QuesDBHandler.COLUMN_CATEGORY + "=\"" + category + "\" AND " +
                QuesDBHandler.COLUMN_QNO + "=" + queno + ";";

                 db.execSQL(query);
    }
    //Method to return cursor to the next unmarked question for a particular category
    public Cursor getFirstUnmarkedQuesByCategory(String category){

        String query= "SELECT * FROM " + QuesDBHandler.TABLE_QUES + " WHERE "+
                QuesDBHandler.COLUMN_CATEGORY + " = "+ "\""+category+"\" AND " +
                QuesDBHandler.COLUMN_MARK + "=-1; ";
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor;
    }

    //Method to return cursor to last question in a category
    public int totalQuesNumber(String category){
        String query="SELECT * FROM " + QuesDBHandler.TABLE_QUES + " WHERE " +
                QuesDBHandler.COLUMN_CATEGORY + "=\"" + category + "\";";

        Cursor c=db.rawQuery(query,null);

        c.moveToLast();

        int last=c.getInt(c.getColumnIndex(QuesDBHandler.COLUMN_QNO));

        c.close();

        return last;
    }

    //Method to return cursor to query returning all the entries for a category
    public Cursor getAllFromCategory(String category){
        String query="SELECT * FROM " + QuesDBHandler.TABLE_QUES + " WHERE " +
                QuesDBHandler.COLUMN_CATEGORY + "=\"" + category + "\";";

        return db.rawQuery(query,null);
    }

    //Method to refresh all responses for a category
    public void refreshResponsesForCategory(String category){
        String query="UPDATE " + QuesDBHandler.TABLE_QUES + " SET " +
                QuesDBHandler.COLUMN_MARK + "=-1 WHERE " +
                QuesDBHandler.COLUMN_CATEGORY + "=\"" + category + "\";";

        db.execSQL(query);
    }
}
