package ayondas2k14.gnosis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//Class to create custom adapter for category list view items
public class CustomCategoryAdapter extends BaseAdapter{

    ArrayList<CategoryData> myList=new ArrayList<CategoryData>();
    LayoutInflater inflater;
    Context context;

    //Constructor
    public CustomCategoryAdapter(Context context, ArrayList<CategoryData> myList){
        this.context=context;
        this.myList=myList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public CategoryData getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;

        if(convertView==null){          //If convertView is not initialised, inflate the layout, create a viewHolder and save it in a tag of covertView
            convertView=inflater.inflate(R.layout.custom_category_list,parent,false);
            holder=new MyViewHolder(convertView);
            convertView.setTag(holder);
        }
        else                //If convertView is already initialised then retrieve the instance of holder from tag of convertview
            holder=(MyViewHolder)convertView.getTag();
        CategoryData item= getItem(position);

        holder.titleTV.setText(item.getTitle());
        holder.descTV.setText(item.getDescription());
        holder.imgView.setImageResource(item.getImgResId());

        return convertView;
    }

    private class MyViewHolder{
        TextView titleTV,descTV;
        ImageView imgView;

        public MyViewHolder(View item){
            titleTV=(TextView)item.findViewById(R.id.categoryTitleTextView);
            descTV=(TextView)item.findViewById(R.id.categoryDescriptionTextView);
            imgView=(ImageView)item.findViewById(R.id.categoryImageView);
        }
    }
}