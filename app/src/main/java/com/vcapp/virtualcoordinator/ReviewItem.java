package com.vcapp.virtualcoordinator;

/**
 * Created by Deok on 2016-07-06.
 */
public class ReviewItem {

    private float rating;
    private String review;
    private boolean check;
    private String idText;
    private String dateText;

    public void setRating(float rating){
        this.rating=rating;
    }
    public void setReview(String review){
        this.review=review;
    }
    public void setIdText(String id){this.idText=id;}
    public void setDateText(String date){this.dateText=date;}
    public void setCheck(boolean check){
        if (check) this.check = true;
        else this.check = false;
    }
    public float getRating(){
        return this.rating;
    }
    public String getReview(){
        return this.review;
    }
    public String getidText(){
        return this.idText;
    }
    public String getDateText(){
        return this.dateText;
    }
}
