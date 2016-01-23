package ayondas2k14.gnosis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class ChallengeQuestionActivity extends AppCompatActivity {

    Button[] optButton;
    TextView quesTv,quizTv,timerTv;
    QueDBAdapter db;
    Cursor cursor;
    int [] quesResponse;        //This array stores the responses for each question in a level
    int current;                //Stores the current questoin number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_question);

//        Log.d("Debug", "I am here!!");

        //Initialising all responses to -1 initially
        quesResponse=new int[10];
        for(int i=1;i<=9;i++)
            quesResponse[i]=-1;

        //set current to 1 initially
        current=1;

        optButton=new Button[4];

        //setting buttons ans textviews
        quizTv=(TextView)findViewById(R.id.quizTypeTextView);
        quizTv.setText("Level 1");
        timerTv=(TextView)findViewById(R.id.qnoTv);
        quesTv=(TextView)findViewById(R.id.queTextView);
        optButton[0]=(Button)findViewById(R.id.option1Button);
        optButton[1]=(Button)findViewById(R.id.option2Button);
        optButton[2]=(Button)findViewById(R.id.option3Button);
        optButton[3]=(Button)findViewById(R.id.option4Button);
        Button home=(Button) findViewById(R.id.homeButton);
        home.setVisibility(View.GONE);
        //Creating timer
        new CountDownTimer(181000,1000){

            long min,sec;
            @Override
            public void onTick(long millisUntilFinished) {
                min=(millisUntilFinished/1000)/60;
                sec=(millisUntilFinished/1000)%60;

                String minute,second;
                if(min<=9)
                    minute="0"+String.valueOf(min);
                else
                    minute=String.valueOf(min);

                if(sec<=9)
                    second="0"+String.valueOf(sec);
                else
                    second=String.valueOf(sec);

                //Getting time as MM:SS format
                String time=minute+":"+second;

                //Setting time to text view
                timerTv.setText(time);
            }

            @Override
            public void onFinish() {
                Toast.makeText(ChallengeQuestionActivity.this, "Time Up!!!", Toast.LENGTH_LONG).show();
                Intent intent =new Intent(getApplicationContext(),ChallengeStatsActivity.class);
                startActivity(intent);
            }
        }.start();


        db=new QueDBAdapter(this);
        try {
            db.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Refresh all questions in that level


        db.refreshLevel(1);
        cursor=db.QueFromLevel();

        onQuestionChange();

    }

    public void onQuestionChange() {
        String ques=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_QUES));

        quesTv.setText(ques);

        String [] options=new String[4];
        options[0]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION1));
        options[1]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION2));
        options[2]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION3));
        options[3]=cursor.getString(cursor.getColumnIndex(QuesDBHandler.COLUMN_OPTION4));

        for(int i=0;i<4;i++)
            optButton[i].setText(options[i]);

        if(quesResponse[current]!=-1) {     //If response is marked
           for(int i=0;i<4;i++)
           {
                if(i==quesResponse[current])
                    optButton[i].setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                else
                    optButton[i].setBackgroundResource(android.R.drawable.btn_default);

               optButton[i].setClickable(false);
           }
        }
        else{                       //response is not marked
                for(int i=0;i<4;i++)
                {
                     optButton[i].setClickable(true);
                     optButton[i].setBackgroundResource(android.R.drawable.btn_default);
                }
        }
    }

    //Methods to set onclick listeners for option buttons
    public void onOption1Clicked(View view) {

        String category=cursor.getString(cursor.getColumnIndex((QuesDBHandler.COLUMN_CATEGORY)));

        int qno=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_QNO));
        db.updatechallengeQues(category,qno,0);

        quesResponse[current]=0;

        optButton[0].setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        for(int i=0;i<4;i++)
            optButton[i].setClickable(false);

        cursor.moveToNext();

        //Increment current for next question
        current++;

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!cursor.isAfterLast())
                    onQuestionChange();
                else{
                    Intent intent=new Intent(getApplicationContext(),ChallengeStatsActivity.class);
                    startActivity(intent);
                }

            }
        },3000);
    }

    public void onOption2Clicked(View view){
        optButton[1].setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        String category=cursor.getString(cursor.getColumnIndex((QuesDBHandler.COLUMN_CATEGORY)));

        int qno=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_QNO));
        db.updatechallengeQues(category, qno, 1);

        quesResponse[current]=1;

        for(int i=0;i<4;i++)
            optButton[i].setClickable(false);

        cursor.moveToNext();

        //Increment current for next question
        current++;

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!cursor.isAfterLast())
                    onQuestionChange();
                else{
                    Intent intent=new Intent(getApplicationContext(),ChallengeStatsActivity.class);
                    startActivity(intent);
                }

            }
        }, 3000);

    }
    public void onOption3Clicked(View view){

        optButton[2].setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        String category=cursor.getString(cursor.getColumnIndex((QuesDBHandler.COLUMN_CATEGORY)));

        int qno=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_QNO));
        db.updatechallengeQues(category, qno, 2);

        quesResponse[current]=2;


        for(int i=0;i<4;i++)
            optButton[i].setClickable(false);

        cursor.moveToNext();

        //Increment current for next question
        current++;

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!cursor.isAfterLast())
                    onQuestionChange();
                else {
                    Intent intent = new Intent(getApplicationContext(), ChallengeStatsActivity.class);
                    startActivity(intent);
                }

            }
        }, 3000);

    }


    public void onOption4Clicked(View view) {
        optButton[3].setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        String category=cursor.getString(cursor.getColumnIndex((QuesDBHandler.COLUMN_CATEGORY)));

        int qno=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_QNO));

        db.updatechallengeQues(category, qno, 3);

        quesResponse[current]=3;

        for(int i=0;i<4;i++)
            optButton[i].setClickable(false);

        cursor.moveToNext();

        //Increment current for next question
        current++;

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!cursor.isAfterLast())
                    onQuestionChange();
                else{
                    Intent intent=new Intent(getApplicationContext(),ChallengeStatsActivity.class);
                    startActivity(intent);
                }
            }
        }, 3000);
    }

    public void onNextButtonClicked(View  view){
        cursor.moveToNext();
        if(cursor.isAfterLast())
        {
            Intent intent=new Intent(getApplicationContext(),ChallengeStatsActivity.class);
            Toast.makeText(getApplicationContext(),"Sorry, no more questions!!",Toast.LENGTH_SHORT).show();
            //cursor.close();
            //Log.d("Debug","Before starting intent");
            startActivity(intent);
        }
        else {
            //Increment current for next question
            current++;
            onQuestionChange();
        }

    }
    public void onBackButtonClicked(View  view){

        cursor.moveToPrevious();
        if(cursor.isBeforeFirst())
        {

            Toast.makeText(getApplicationContext(),"Sorry,this is the first question!!",Toast.LENGTH_SHORT).show();
        }
        else {
            //Decrement current for previous question
            current--;
            onQuestionChange();
        }
    }

    //Overriding obBackPresses() method


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("Do you want to finish?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), ChallengeStatsActivity.class);
                finish();
                startActivity(intent);
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }
}