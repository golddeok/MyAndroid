package com.vcapp.virtualcoordinator;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Deok on 2016-07-03.
 */
public class ItemAdapter extends BaseAdapter{

    public ArrayList<Itemlist> kListViewItemList = new ArrayList<Itemlist>();
    private int lastPosition = -1 ;

    public ItemAdapter(){


    }


    @Override
    public int getCount() {
        return kListViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return kListViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context=parent.getContext();
        System.out.println("getView 실행."+position);
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.itemlist,parent,false);
        }
        ImageView iconImageView=(ImageView)convertView.findViewById(R.id.imageView28);
        TextView titleTextView=(TextView)convertView.findViewById(R.id.nametxt);
        TextView descTextView=(TextView)convertView.findViewById(R.id.infotxt);
        CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);




        Itemlist listViewItem= kListViewItemList.get(position);

        iconImageView.setImageBitmap(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());

        lastPosition = position;



        return convertView;
    }

    public void addItem(Bitmap icon, String title, String desc){
        Itemlist item=new Itemlist();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        kListViewItemList.add(item);
        this.notifyDataSetChanged();
    }
    public void addItem(Bitmap icon, String title, String desc,int position){
        Itemlist  item=new Itemlist ();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        kListViewItemList.add(position,item);
        this.notifyDataSetChanged();
    }
    public void removeItem(int position){
        Itemlist  removeItem= kListViewItemList.get(position);
        kListViewItemList.remove(removeItem);
        this.notifyDataSetChanged();
    }
    public void showCheckBox(){
        for(int i=0;i<getCount();i++)
        {

        }
    }


}
