package com.example.goostore;

import android.media.Image;

import com.google.firebase.database.Exclude;

import java.util.*;
import java.util.concurrent.Callable;

public class Goods{

    private String mName;
    private String mImageUrl;  // Image's ID
    private String mKey;       // Goods' ID
    private String BasePrice;
    private String Category;
    private String SellerEmail;
    private String DeadLine;

    public Goods(){

    }

    public Goods(String name, String imageUrl,String basePrice, String category, String selleremail, String deadLine) {
        if (name.trim().equals("")) {
            name = "No name";
        }
        if (Double.parseDouble(basePrice) < 1.0) {
            Double price = 1.0;
            basePrice = price.toString();
        }

        mName = name;
        mImageUrl = imageUrl;
        BasePrice = basePrice;
        Category = category;
        SellerEmail = selleremail;
        DeadLine = deadLine;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }

    public String getBasePrice(){
        return BasePrice;
    }

    public void setBasePrice(String basePrice){
        if(Integer.parseInt(basePrice) < 1){
            BasePrice = "1";
        }
        BasePrice = basePrice;
    }

    public String getCategory(){
        return Category;
    }

    public void setCategory(String category){
        Category = category;
    }

    public String getSellerEmail(){
        return SellerEmail;
    }

    public void setSellerEmail(String email){
        SellerEmail = email;
    }

    public String getDeadLine(){
        return DeadLine;
    }

    public void setDeadLine(String deadline){
        DeadLine = deadline;
    }

    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }


}

