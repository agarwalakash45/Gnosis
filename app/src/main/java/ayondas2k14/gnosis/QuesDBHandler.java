package ayondas2k14.gnosis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLDataException;
import java.sql.SQLException;

//Class to create,open and close an existing database in assets to the device databases directory
public class QuesDBHandler extends SQLiteOpenHelper{
    private static int DATABASE_VERSION=4;
    private static String DB_NAME="questions.db";
    public static String TABLE_QUES="questions";
    private static String DB_PATH="/data/data/ayondas2k14.gnosis/databases/";
    public static String COLUMN_CATEGORY="category";
    public static String COLUMN_QUES="question";
    public static String COLUMN_QNO="qno";          //To keep track of question no.
    public static String COLUMN_OPTION1="option1";
    public static String COLUMN_OPTION2="option2";
    public static String COLUMN_OPTION3="option3";
    public static String COLUMN_OPTION4="option4";
    public static String COLUMN_ANSWER="answer";
    public static String COLUMN_MARK="marked";         //To store user response, intially it si set equal to -1

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    //Constructor to get DATABASE PATH
    public QuesDBHandler(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);

        if(android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";

        else        //For SDK<17, database is stored in data/data/packagename/databases/
            DB_PATH =   "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
    }

    //Method to create database
    public void createDataBase() throws IOException
    {
        //If the database does not exist, copy it from the assets.
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)     //If database doesn't exist
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                //Log.e(TAG, "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Check that the database exists here: /data/data/your package/databases/DBName
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
//        if(dbFile.exists())
  //          dbFile.delete();
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;         //  output filename:- data/data/ayondas2k14.gnosis/questions.db
        OutputStream mOutput = new FileOutputStream(outFileName);

        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
            mOutput.write(mBuffer, 0, mLength);

        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;       //returns true if DATABASES opening is successful
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
