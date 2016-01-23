package ayondas2k14.gnosis;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.logging.Level;

public class ChallengeActivity extends AppCompatActivity {

    ArrayList<LevelData> myList = new ArrayList<LevelData>();

    String[] title = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9","Level 10"};

       int[] imgId = {R.drawable.ic_lock_open_black_24dp, R.drawable.ic_lock_black_24dp, R.drawable.ic_lock_black_24dp, R.drawable.ic_lock_black_24dp, R.drawable.ic_lock_black_24dp, R.drawable.ic_lock_black_24dp,
            R.drawable.ic_lock_black_24dp, R.drawable.ic_lock_black_24dp, R.drawable.ic_lock_black_24dp,R.drawable.ic_lock_black_24dp};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        setLevelData();
        final ListView challengeLv = (ListView) findViewById(R.id.challengeListView);

        CustomLevelAdapter adapter = new CustomLevelAdapter(getApplicationContext(), myList);

        challengeLv.setAdapter(adapter);

        challengeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),ChallengeQuestionActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setLevelData() {
        for (int i = 0; i < 10; i++) {
            LevelData item = new LevelData();
            item.setLevel(title[i]);
            item.setImgResId(imgId[i]);


            myList.add(item);
        }

    }

    //Overriding onBackPressed() method

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,MainActivity.class);
        finish();
        startActivity(intent);
    }
}
