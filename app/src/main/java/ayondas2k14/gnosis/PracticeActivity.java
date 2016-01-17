package ayondas2k14.gnosis;

import android.graphics.drawable.RippleDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class PracticeActivity extends AppCompatActivity {

    ArrayList<CategoryData> myList=new ArrayList<CategoryData>();

    String [] title={"Business","Music","Movies","Literature","General","Sports","India","Science","Computer Science"};

    String [] desc={"A","B","C","C","C","C","C","C","C"};

    int [] imgId={R.drawable.business,R.drawable.music,R.drawable.movies,R.drawable.literature,R.drawable.general, R.drawable.sports,
            R.drawable.india,R.drawable.science,R.drawable.comp_sc};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        //Setting category data
        setCategoryData();

        ListView categoryLv=(ListView)findViewById(R.id.categoryListView);

        CustomCategoryAdapter adapter=new CustomCategoryAdapter(this,myList);

        categoryLv.setAdapter(adapter);
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
