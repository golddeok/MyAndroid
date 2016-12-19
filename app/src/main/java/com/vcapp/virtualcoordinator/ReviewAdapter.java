package com.vcapp.virtualcoordinator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Deok on 2016-07-06.
 */
public class ReviewAdapter extends BaseAdapter {

    public ArrayList<ReviewItem> reviewItemList = new ArrayList<>();





    @Override
    public int getCount() {
        return reviewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context=parent.getContext();
        System.out.println("getview 실행된다."+position);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.review_item,parent,false);
        }
        RatingBar rt= (RatingBar) convertView.findViewById(R.id.reviewItemRating);
        TextView textView=(TextView) convertView.findViewById(R.id.reviewItemText);
        CheckBox cb= (CheckBox) convertView.findViewById(R.id.reviewItemCheck);

        TextView idText= (TextView) convertView.findViewById(R.id.reviewIdText);
        TextView dateText= (TextView) convertView.findViewById(R.id.reviewDateText);

        ReviewItem reviewItem=reviewItemList.get(position);

        rt.setRating(reviewItem.getRating());
        textView.setText(reviewItem.getReview());
        idText.setText(reviewItem.getidText());
        dateText.setText(reviewItem.getDateText());

        reviewItem.setCheck(cb.isChecked());


        return convertView;
    }

    public void addReviewItem(float rating, String review, String id, String date){
        ReviewItem item=new ReviewItem();


        item.setRating(rating);
        item.setReview(review);
        item.setIdText(id);
        item.setDateText(date);
        if(reviewItemList.size()==0){
            reviewItemList.add(item);
        }
        else
        {
            for(int i=(reviewItemList.size())-1; i < 0 ;i--)
            {
                ReviewItem refItem=reviewItemList.get(i);
                reviewItemList.add(i+1,refItem);
                reviewItemList.remove(i);
            }
            reviewItemList.add(0,item);
        }

        this.notifyDataSetChanged();
    }
    public void initAddReviewItem(float rating, String review, String id, String date){
        ReviewItem item=new ReviewItem();


        item.setRating(rating);
        item.setReview(review);
        item.setIdText(id);
        item.setDateText(date);
        reviewItemList.add(item);
        this.notifyDataSetChanged();

    }
    public void removeReviewItem(int position){
        ReviewItem reviewItem= reviewItemList.get(position);
        reviewItemList.remove(reviewItem);
        this.notifyDataSetChanged();
    }
    public void clearReviewList(){
        reviewItemList.clear();
    }
}
