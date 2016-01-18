package ayondas2k14.gnosis;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;

public class PracticeQuestionActivity extends AppCompatActivity {

    TextView typeTv,quesTv;
    Button [] optButton;
    QueDBAdapter db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_question);

        typeTv=(TextView)findViewById(R.id.quizTypeTextView);
        quesTv=(TextView)findViewById(R.id.queTextView);
        optButton=new Button[4];
        optButton[0]=(Button)findViewById(R.id.option1Button);
        optButton[1]=(Button)findViewById(R.id.option2Button);
        optButton[2]=(Button)findViewById(R.id.option3Button);
        optButton[3]=(Button)findViewById(R.id.option4Button);

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

        //Retrieving category name from extras
        Bundle bundle=getIntent().getExtras();
        String category=bundle.getString("Category");

        //Cursor to query returning an entry from the database
        cursor=db.getQueByCategory(category);

        //setting textviews and buttons text
        String type,que;
        String [] optString=new String[4];
        type=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_CATEGORY));
        que=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_QUES));
        optString[0]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION1));
        optString[1]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION2));
        optString[2]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION3));
        optString[3]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION4));

        typeTv.setText(type);
        quesTv.setText(que);
        for(int i=0;i<4;i++)
                optButton[i].setText(optString[i]);
    }

    //Methods to set onclick listeners for option buttons
    public void onOption1Clicked(View view){
        int answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
        if(answer==0)
            optButton[0].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        else {
            optButton[0].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }

    public void onOption2Clicked(View view){
        int answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
        if(answer==1)
            optButton[1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        else {
            optButton[1].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }
    public void onOption3Clicked(View view){
        int answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
        if(answer==2)
            optButton[2].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        else {
            optButton[2].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }
    public void onOption4Clicked(View view){
        int answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
        if(answer==3)
            optButton[3].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        else {
            optButton[3].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }


    //Method to set click listener for HOME Button
    public void onHomeButtonClicked(View view){
        Intent intent=new Intent(this,PracticeActivity.class);
        startActivity(intent);
    }

    //Overriding onBackPressed() method to return PracticeActivity when back is pressed

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,PracticeActivity.class);
        startActivity(intent);
    }
}