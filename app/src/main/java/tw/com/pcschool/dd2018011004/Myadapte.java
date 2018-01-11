package tw.com.pcschool.dd2018011004;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tw.com.pcschool.dd2018011004.Mobile01news;
import tw.com.pcschool.dd2018011004.R;

/**
 * Created by Student on 2018/1/11.
 */
//自訂畫面來放抓回的 圖文, ListView自定圖文
public class Myadapte extends BaseAdapter {
    Context context;
    ArrayList<Mobile01news> mylist;

    //建構式
    public Myadapte(Context context, ArrayList<Mobile01news> mylist)
    {
        this.context = context;
        this.mylist = mylist;
    }
    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder viewholder;
        if (view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.mylayout,null);
            viewholder = new Viewholder();
            viewholder.tv1 = view.findViewById(R.id.textView);
            viewholder.tv2 = view.findViewById(R.id.textView2);
            viewholder.img = view.findViewById(R.id.imageView);
            view.setTag(viewholder);
        }
        else
        {
            viewholder = (Viewholder) view.getTag();
        }

        viewholder.tv1.setText(mylist.get(i).title);
        viewholder.tv2.setText(mylist.get(i).description);
        Picasso.with(context).load(mylist.get(i).imgurl).into(viewholder.img);
        viewholder.tv1.setText(mylist.get(i).title);
        viewholder.tv2.setText(mylist.get(i).description);
        return view;
    }

    static class Viewholder
    {
        TextView tv1;
        TextView tv2;
        ImageView img;
    }
}
