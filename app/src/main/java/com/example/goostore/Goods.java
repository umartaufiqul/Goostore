package com.example.goostore;

import android.media.Image;

import java.util.*;

public class Goods{

    private Integer CategoryNumber;
    public double BasePrice;
    public String GoodName;
    public Image[] GoodImages;

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
    }

    /* public Goods(String email, String password, String name, String phoneNumber, String address, String bankAccount) {
        super(email, password, name, phoneNumber, address, bankAccount);
    }*/
}

