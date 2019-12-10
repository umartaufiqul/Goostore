package com.example.goostore;

import android.media.Image;

import com.google.firebase.database.Exclude;

import java.util.*;
import java.util.concurrent.Callable;

public class Goods{

    private String mName;
    private String mImageUrl;
    private String mKey;
    private String BasePrice;
    private String Category;
    private String SellerEmail;
    private String BuyerEmail;
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
        BuyerEmail = "";
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

    public void setBasePrice(Integer basePrice){
        if(basePrice < 1){
            basePrice = 1;
        }
        BasePrice = basePrice.toString();
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

    public String getBuyerEmial(){
        return BuyerEmail;
    }

    public void setBuyerEmail(String email){
        BuyerEmail = email;
    }

    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }

   /* private Integer CategoryNumber;
    private double BasePrice;
    private String GoodName;
    private Image[] GoodImages;

    public Goods(String Good_Name, double Base_Price, Integer Category_Number, Image[] Good_Images){
        GoodName = Good_Name;
        BasePrice = Base_Price;
        CategoryNumber = Category_Number;
        GoodImages = new Image[Good_Images.length];
        GoodImages = Good_Images.clone();
    }

    public double getBasePrice() {
        return BasePrice;
    }

    public Image[] getGoodImages() {
        return GoodImages;
    }

    public String getGoodName() {
        return GoodName;
    }

    public Integer getCategoryNumber() {
        return CategoryNumber;
    }

    public void setBasePrice(double basePrice) {
        BasePrice = basePrice;
    }*/

    /* public Goods(String email, String password, String name, String phoneNumber, String address, String bankAccount) {
        super(email, password, name, phoneNumber, address, bankAccount);
    }*/

}

