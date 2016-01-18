package ayondas2k14.gnosis;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;

public class PracticeQuestionActivity extends AppCompatActivity {

    TextView typeTv,quesTv;
    Button opt1Button,opt2Button,opt3Button,opt4Button;
    QueDBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_question);

        typeTv=(TextView)findViewById(R.id.quizTypeTextView);
        quesTv=(TextView)findViewById(R.id.queTextView);
        opt1Button=(Button)findViewById(R.id.option1Button);
        opt2Button=(Button)findViewById(R.id.option2Button);
        opt3Button=(Button)findViewById(R.id.option3Button);
        opt4Button=(Button)findViewById(R.id.option4Button);

        setViewsFromDB();
    }

    //Method to set textviews and buttons using data from database
    public void setViewsFromDB(){

        db=new QueDBAdapter(this);
        //create database
        try {
            db.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //open the database
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Cursor to query returning an entry from the database
        Cursor cursor=db.getTestData();

        //setting textviews and buttons text
        String type,que,opt1,opt2,opt3,opt4;
        type=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_CATEGORY));
        que=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_QUES));
        opt1=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION1));
        opt2=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION2));
        opt3=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION3));
        opt4=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION4));

        typeTv.setText(type);
        quesTv.setText(que);
        opt1Button.setText(opt1);
        opt2Button.setText(opt2);
        opt3Button.setText(opt3);
        opt4Button.setText(opt4);

    }
}