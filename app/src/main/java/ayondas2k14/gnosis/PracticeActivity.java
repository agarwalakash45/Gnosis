package ayondas2k14.gnosis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.drm.DrmStore;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class PracticeActivity extends AppCompatActivity {

    ArrayList<CategoryData> myList = new ArrayList<CategoryData>();

    String[] title = {"Business", "Music", "Movies", "Literature", "General", "Sports", "India", "Science", "Computer Science"};

    String[] desc = {"A", "B", "C", "C", "C", "C", "C", "C", "C"};

    QueDBAdapter db;

    int[] imgId = {R.drawable.business, R.drawable.music, R.drawable.movies, R.drawable.literature, R.drawable.general, R.drawable.sports,
            R.drawable.india, R.drawable.science, R.drawable.comp_sc};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        db = new QueDBAdapter(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Setting category data
        setCategoryData();

        final ListView categoryLv = (ListView) findViewById(R.id.categoryListView);

        CustomCategoryAdapter adapter = new CustomCategoryAdapter(this, myList);

        categoryLv.setAdapter(adapter);

        categoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PracticeActivity.this, PracticeQuestionActivity.class);

                String category = ((TextView) view.findViewById(R.id.categoryTitleTextView)).getText().toString();
                //sending category name and ques no through Extras
                Cursor cursor = db.getFirstUnmarkedQuesByCategory(category);

                int qno;

                if (cursor.getCount() == 0)        //If all questions are marked, then go to the first question
                    qno = 1;
                else
                    qno = cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_QNO));   //else go to the first unmarked question

                intent.putExtra("Category", category);

                intent.putExtra("Qno", qno);

                startActivity(intent);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    //Method to set category data
    public void setCategoryData() {
        for (int i = 0; i < 9; i++) {
            CategoryData item = new CategoryData();
            item.setTitle(title[i]);
            item.setDescription(desc[i]);
            item.setImgResId(imgId[i]);

            myList.add(item);
        }


    }

    public void onBackPressed () {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
