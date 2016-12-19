package com.vcapp.virtualcoordinator;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Itemlist {

    private Bitmap iconDrawable;
    private String titleStr;
    private String descStr;

    public void setIcon(Bitmap icon){
        iconDrawable=icon;
    }
    public void setTitle(String title){
        titleStr=title;
    }
    public void setDesc(String desc){
        descStr=desc;
    }
    public Bitmap getIcon(){
        return this.iconDrawable;
    }
    public String getTitle(){
        return this.titleStr;
    }
    public String getDesc(){
        return this.descStr;
    }
}
