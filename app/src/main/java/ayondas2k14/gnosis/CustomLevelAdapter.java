package ayondas2k14.gnosis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ayon Das on 22-01-2016.
 */
public class CustomLevelAdapter extends BaseAdapter {


    ArrayList<LevelData> myList=new ArrayList<LevelData>();
    LayoutInflater inflater;
    Context context;


    //Constructor
    public CustomLevelAdapter(Context context, ArrayList<LevelData> myList){
        this.context=context;
        this.myList=myList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public LevelData getItem(int position) {
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
            convertView=inflater.inflate(R.layout.custom_recycler_level,parent,false);
            holder=new MyViewHolder(convertView);
            convertView.setTag(holder);
        }
        else                //If convertView is already initialised then retrieve the instance of holder from tag of convertview
            holder=(MyViewHolder)convertView.getTag();
        LevelData item= getItem(position);

        holder.titleTv.setText(item.getLevel());
        holder.imgView.setImageResource(item.getImgResId());

        return convertView;

    }


    private class MyViewHolder{
        TextView titleTv;
        ImageView imgView;

        public MyViewHolder(View item){
            titleTv=(TextView)item.findViewById(R.id.levelTV);
             imgView=(ImageView)item.findViewById(R.id.lockImage);
        }
    }


}
