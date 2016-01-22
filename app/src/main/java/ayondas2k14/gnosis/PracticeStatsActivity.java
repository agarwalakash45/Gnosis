package ayondas2k14.gnosis;

import android.content.Intent;
import android.database.Cursor;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.security.KeyStore;
import java.sql.SQLException;
import java.util.ArrayList;

public class PracticeStatsActivity extends AppCompatActivity {

    QueDBAdapter db;
    Cursor cursor;
    String category;
    TextView categoryTV,totalQueTV,attemptedQueTV,correctQueTV;
    int total,attempted,correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_stats);

        Log.d("Debug","In PracticeStatsActivity");

        //setting text views
        categoryTV=(TextView)findViewById(R.id.categoryStatsTV);
        totalQueTV=(TextView)findViewById(R.id.totalQuesTV);
        attemptedQueTV=(TextView)findViewById(R.id.attemptedQueTV);
        correctQueTV=(TextView)findViewById(R.id.correctQueTV);

        //receiving category name through intent
        Bundle bundle=getIntent().getExtras();
        category=bundle.getString("Category");

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

        //retrieve cursor to all entries for a particular category
        cursor=db.getAllFromCategory(category);

        //Retrieving total questions
        total=cursor.getCount();

        Log.d("Debug",""+cursor.getCount());

        cursor.moveToFirst();

        //Retreiving attempted and correct questions
        while(!cursor.isAfterLast()){
            int ans,mark;
            ans=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_ANSWER));
            mark=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_MARKPRAC));

            Log.d("Debug",""+cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_QNO))+" "+ mark+ " "+ans);

            //Answer marked is correct
            if(ans==mark)
                correct++;
            //Question is attempted
            if(mark!=-1)
                attempted++;

            cursor.moveToNext();
        }

        String str1,str2,str3;
        str1="Total questions: "+String.valueOf(total);
        str2="Attempted: "+String.valueOf(attempted);
        str3="Correct: "+String.valueOf(correct);

        //Setting text to textviews
        categoryTV.setText(category);
        totalQueTV.setText(str1);
        totalQueTV.setTextColor(getResources().getColor(android.R.color.holo_purple));

        attemptedQueTV.setText(str2);
        attemptedQueTV.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));

        correctQueTV.setText(str3);
        correctQueTV.setTextColor(getResources().getColor(android.R.color.holo_green_dark));


        //To add a pie chart in this activity
        PieChart chart=(PieChart)findViewById(R.id.pieChart);

        ArrayList<Entry> yvalues=new ArrayList<Entry>();
        Entry pieIncorrect=new Entry(attempted-correct,0);
        Entry pieNotAttempted=new Entry(total-attempted,1);
        Entry pieCorrect=new Entry(correct,2);

        yvalues.add(pieIncorrect);
        yvalues.add(pieNotAttempted);
        yvalues.add(pieCorrect);

        PieDataSet DataSet=new PieDataSet(yvalues,null);

        ArrayList<Integer> colors=new ArrayList<Integer>();
        colors.add(ColorTemplate.rgb("#ff5050"));
        colors.add(ColorTemplate.rgb("#12a7d1"));
        colors.add(ColorTemplate.rgb("#b3d334"));

        DataSet.setColors(colors);

        String [] xvalues={"Incorrect","Not Attempted","Correct"};

        PieData data=new PieData(xvalues,DataSet);

        data.setValueTextSize(15f);

        chart.setData(data);
//        chart.setHoleColorTransparent(true);
  //      chart.setHoleRadius(7);

        chart.setUsePercentValues(true);

        chart.setDescription(null);

        chart.invalidate();
    }

    //Method to set onClick for HOME button
    public void onHomeButtonClicked(View view){
        Intent intent=new Intent(this,PracticeActivity.class);
        startActivity(intent);
    }
}
