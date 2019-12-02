package com.example.goostore;

import java.util.*;

public class User {
    //Fields
    private String Email;
    private String Name;
    private String Password;
    private String PhoneNumber;
    private String Address;
    private String BankAccount;

    //Constructor
    public User(String email, String password, String name, String phoneNumber, String address, String bankAccount) {
        Name = name;
        Password = password;
        Email = email;
        PhoneNumber = phoneNumber;
        Address = address;
        BankAccount = bankAccount;
    }

    //Method to access the Email
    public String getEmail() {
        return Email;
    }
    //Method to access the Name
    public String getName() {
        return Name;
    }
    //Method to access the Password
    public String getPassword() {
        return Password;
    }
    //Method to access the PhoneNumber
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    //Method to access the Address
    public String getAddress() {
        return Address;
    }
    //Method to access the Address
    public String getBankAccount() {
        return BankAccount;
    }

    //Registration Method
    public boolean Registration (String email, String password, String name, String phoneNumber, String address, String bankAccount) {
        if (email.equals("") || password.equals("") || name.equals("") || phoneNumber.equals("") || address.equals("") || bankAccount.equals("")) return false;
        Map<String, String> m = new HashMap<String, String>();
        m.put("Password", password);
        m.put("Name", name);
        m.put("PhoneNumber", phoneNumber);
        m.put("Address", address);
        m.put("BankAccount", bankAccount);
        DataBase.Users.put(email, m);
        return true;
    }

    //Login Method
    public boolean Login(String email, String password) {
        if (!DataBase.Users.containsKey(email)) return false;
        return password.equals(DataBase.Users.get(email).get("Password"));
    }
}
