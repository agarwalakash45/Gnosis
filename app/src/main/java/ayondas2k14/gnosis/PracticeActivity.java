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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    String[] title = {"Business", "Computer Science","General","India","Literature", "Movies","Music","Science","Sports"};

    String[] desc = {"For the corporate geeks", "Hackers and Crackers", "Test your general knowledge", "Mera Bharat Mahaan", "For the avid readers", "Movie buffs,eh?", "'Music is my life',anyone?", "For the nerds out there", "For the sport fanatics"};

    QueDBAdapter db;

    String category;

    int[] imgId = {R.drawable.business, R.drawable.comp_sc, R.drawable.general, R.drawable.india, R.drawable.literature, R.drawable.movies,
            R.drawable.music, R.drawable.science, R.drawable.sports};
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

                category = ((TextView) view.findViewById(R.id.categoryTitleTextView)).getText().toString();
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

        registerForContextMenu(categoryLv);
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

    //Method to create context menu to give options to show stats and give refresh options
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.context_category_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        final String categoryName=((TextView)info.targetView.findViewById(R.id.categoryTitleTextView)).getText().toString();


        switch(item.getItemId()){
            case R.id.stats_context_menu:
                //Starting the stats activity for this category
                Intent intent=new Intent(this,PracticeStatsActivity.class);

                intent.putExtra("Category",categoryName);
                startActivity(intent);

                break;
            case R.id.refresh_context_menu:

                AlertDialog.Builder alert=new AlertDialog.Builder(this);
                alert.setMessage("All progress will be refreshed\nAre you sure?");

                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.refreshResponsesForCategory(categoryName);
                        Toast.makeText(getApplicationContext(), "Data Refreshed!!", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.show();

                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed () {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
