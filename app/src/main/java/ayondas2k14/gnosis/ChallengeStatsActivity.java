package ayondas2k14.gnosis;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;

//Class to create stats activity for each level
public class ChallengeStatsActivity extends AppCompatActivity {
    TextView levelTv,totalQueTv,attQueTv,correctQueTv,resultTv;
    QueDBAdapter db;
    Button retrybtn;
    int total=0,attempted=0,correct=0,mark,answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_challenge_stats);

        //Log.d("Debug","In PracticeStatsActivity");

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


        //Setting textviews
        levelTv=(TextView)findViewById(R.id.levelStatsTv);
        totalQueTv=(TextView)findViewById(R.id.totQuesTv);
        attQueTv=(TextView)findViewById(R.id.attQuesTv);
        correctQueTv=(TextView)findViewById(R.id.correctQuesTv);
        resultTv=(TextView)findViewById(R.id.rsltLevelTv);


        levelTv.setText("Level 1");

        //Cursor to retrieve all questions from database
        Cursor cursor=db.QueFromLevel();

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            total++;

            mark=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_MARKCHL));
            answer=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));

            if(mark==answer)        //If answer marked is same as answer
                correct++;

            if(mark!=-1)        //Answer is marked i.e. question is attempted
                attempted++;

            cursor.moveToNext();
        }

        String str1,str2,str3;
        str1="Total questions: "+String.valueOf(total);
        str2="Attempted: "+String.valueOf(attempted);
        str3="Correct: "+String.valueOf(correct);

        totalQueTv.setText(str1);
        totalQueTv.setTextColor(getResources().getColor(android.R.color.holo_purple));

        attQueTv.setText(str2);
        attQueTv.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));

        correctQueTv.setText(str3);
        correctQueTv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        if(correct>=4){
            resultTv.setText("Level completed\n\nNext Level Unlocked");
            resultTv.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
        }
        else{
            String str="Sorry, You need to complete 4 questions to complete this level";
            resultTv.setText(str);
            resultTv.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            retrybtn=(Button)findViewById(R.id.retry);
        }

    }
    public void onRetryButtonClicked(View view)
    {
        Intent intent=new Intent(getApplicationContext(),ChallengeQuestionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(getApplicationContext(),ChallengeActivity.class);
        startActivity(intent);
    }
}
