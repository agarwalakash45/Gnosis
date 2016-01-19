package ayondas2k14.gnosis;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.RippleDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class PracticeActivity extends AppCompatActivity {

    ArrayList<CategoryData> myList=new ArrayList<CategoryData>();

    String [] title={"Business","Music","Movies","Literature","General","Sports","India","Science","Computer Science"};

    String [] desc={"A","B","C","C","C","C","C","C","C"};

    QueDBAdapter db;

    int [] imgId={R.drawable.business,R.drawable.music,R.drawable.movies,R.drawable.literature,R.drawable.general, R.drawable.sports,
            R.drawable.india,R.drawable.science,R.drawable.comp_sc};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        db=new QueDBAdapter(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        //Setting category data
        setCategoryData();

        final ListView categoryLv=(ListView)findViewById(R.id.categoryListView);

        CustomCategoryAdapter adapter=new CustomCategoryAdapter(this,myList);

        categoryLv.setAdapter(adapter);

        categoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(PracticeActivity.this,PracticeQuestionActivity.class);

                String category=((TextView)view.findViewById(R.id.categoryTitleTextView)).getText().toString();
                //sending category name and ques no through Extras
                Cursor cursor= db.getFirstUnmarkedQuesByCategory(category);

                int qno=cursor.getInt(cursor.getColumnIndex(QuesDBHandler.COLUMN_QNO));

                intent.putExtra("Category",category);

                intent.putExtra("Qno",qno);

                startActivity(intent);
            }
        });
    }

    //Method to set category data
    public void setCategoryData(){
        for(int i=0;i<9;i++){
            CategoryData item=new CategoryData();
            item.setTitle(title[i]);
            item.setDescription(desc[i]);
            item.setImgResId(imgId[i]);

            myList.add(item);
        }
    }
}
