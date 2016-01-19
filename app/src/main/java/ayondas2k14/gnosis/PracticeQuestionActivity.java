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

    TextView typeTv,quesTv,qnum;
    Button [] optButton;
    QueDBAdapter db;
    Cursor cursor;
    String category;
    int quesno,qnext;
    int last;


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
        qnum=(TextView)findViewById(R.id.qnoTv);
        setViewsFromDB();
        last= db.totalQuesNumber(category);
    }

    //Method to set textviews and buttons using data from database
    public void setViewsFromDB(){

        db=new QueDBAdapter(this);

        //open the database
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Retrieving category name from extras
        Bundle bundle=getIntent().getExtras();
        category=bundle.getString("Category");
        qnext=bundle.getInt("Qno");

        //Cursor to query returning an entry from the database
        cursor=db.getQueByCategory(category,qnext);

        quesno=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_QNO));

        //retrieving status of question
        int mark=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_MARK));
        //retrieving answer of question
        int answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));

        //setting textviews and buttons text
        String type,que;
        String [] optString=new String[4];
        type=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_CATEGORY));
        que=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_QUES));
        optString[0]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION1));
        optString[1]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION2));
        optString[2]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION3));
        optString[3]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION4));

        String abc=String.valueOf(quesno);
        typeTv.setText(type);
        quesTv.setText(que);
        qnum.setText(abc);
        for(int i=0;i<4;i++)
            optButton[i].setText(optString[i]);

        if(mark!=-1){
            //If answer and makr are not same, then user response is wrong
            if(mark!=answer){
                optButton[mark].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            }
            else        //If answer and mark are same, user answer is correct and already marked
                optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

            for(int i=0;i<4;i++)        //Disabling all buttons
                optButton[i].setClickable(false);
        }
    }

    //Methods to set onclick listeners for option buttons
    public void onOption1Clicked(View view) {
        int answer = cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
        if (answer == 0)
            optButton[0].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        else {
            optButton[0].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }

        //Updating user response in the database for this question
        db.updateQueStatus(category, quesno, 0);

        //disabling button clicks when an option is seletcted
        for (int i = 0; i < 4; i++)
            optButton[i].setClickable(false);

        Thread timer=new Thread(){
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //If this is the last question, go to the stats activity
                    if (quesno == last) {
                        Intent intent = new Intent(PracticeQuestionActivity.this, PracticeStatsActivity.class);
                        intent.putExtra("Category", category);
                        startActivity(intent);
                    } else {        //move to the next question
                        Intent intent = new Intent(PracticeQuestionActivity.this, PracticeQuestionActivity.class);
                        intent.putExtra("Category", category);
                        intent.putExtra("Qno", quesno + 1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }
            }
        };
        timer.start();

    }

    public void onOption2Clicked(View view){
        int answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
        if(answer==1)
            optButton[1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        else {
            optButton[1].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }

        //Updating user response in the database for this question
        db.updateQueStatus(category, quesno, 1);

        //disabling button clicks when an option is seletcted
        for(int i=0;i<4;i++)
            optButton[i].setClickable(false);


        Thread timer=new Thread(){
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //If this is the last question, go to the stats activity
                    if (quesno == last) {
                        Intent intent = new Intent(PracticeQuestionActivity.this, PracticeStatsActivity.class);
                        intent.putExtra("Category", category);
                        startActivity(intent);
                    } else {        //move to the next question
                        Intent intent = new Intent(PracticeQuestionActivity.this, PracticeQuestionActivity.class);
                        intent.putExtra("Category", category);
                        intent.putExtra("Qno", quesno + 1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }
            }
        };
        timer.start();

    }
    public void onOption3Clicked(View view){
        int answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
        if(answer==2)
            optButton[2].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        else {
            optButton[2].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }

        //Updating user response in the database for this question
        db.updateQueStatus(category,quesno,2);

        //disabling button clicks when an option is seletcted
        for(int i=0;i<4;i++)
            optButton[i].setClickable(false);


        Thread timer=new Thread(){
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //If this is the last question, go to the stats activity
                    if (quesno == last) {
                        Intent intent = new Intent(PracticeQuestionActivity.this, PracticeStatsActivity.class);
                        intent.putExtra("Category", category);
                        startActivity(intent);
                    } else {        //move to the next question
                        Intent intent = new Intent(PracticeQuestionActivity.this, PracticeQuestionActivity.class);
                        intent.putExtra("Category", category);
                        intent.putExtra("Qno", quesno + 1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }
            }
        };
        timer.start();

    }


    public void onOption4Clicked(View view){
        int answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
        if(answer==3)
            optButton[3].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        else {
            optButton[3].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            optButton[answer].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }

        //Updating user response in the database for this question
        db.updateQueStatus(category, quesno, 1);

        //disabling button clicks when an option is seletcted
        for(int i=0;i<4;i++)
            optButton[i].setClickable(false);


        Thread timer=new Thread(){
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //If this is the last question, go to the stats activity
                    if (quesno == last) {
                        Intent intent = new Intent(PracticeQuestionActivity.this, PracticeStatsActivity.class);
                        intent.putExtra("Category", category);
                        startActivity(intent);
                    } else {        //move to the next question
                        Intent intent = new Intent(PracticeQuestionActivity.this, PracticeQuestionActivity.class);
                        intent.putExtra("Category", category);
                        intent.putExtra("Qno", quesno + 1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }
            }
        };
        timer.start();
    }


    //Method to set click listener for HOME Button
    public void onHomeButtonClicked(View view){
        Intent intent=new Intent(getApplicationContext(),PracticeActivity.class);
        startActivity(intent);
    }

    //Method to set click listener for next button
    public void onNextButtonClicked(View view) {
        //If this is the last question, go to the stats activity
        if (quesno == last) {
            Intent intent = new Intent(PracticeQuestionActivity.this, PracticeStatsActivity.class);
            intent.putExtra("Category", category);
            startActivity(intent);
        } else {        //move to the next question
            Intent intent = new Intent(PracticeQuestionActivity.this, PracticeQuestionActivity.class);
            intent.putExtra("Category", category);
            intent.putExtra("Qno", quesno + 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    //Method to set click listener for back button
    public void onBackButtonClicked(View view){

        if(quesno==1){      //If this is the first question, return to the category activity
            Intent intent=new Intent(this,PracticeActivity.class);
            startActivity(intent);
        }
        else {              //Move to the previous activity
            Intent intent = new Intent(getApplicationContext(), PracticeQuestionActivity.class);
            intent.putExtra("Category", category);
            intent.putExtra("Qno", quesno - 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    public void onBackPressed () {
        Intent intent = new Intent(getApplicationContext(),PracticeActivity.class);
        //intent.addCategory(Intent.CATEGORY_HOME);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



}