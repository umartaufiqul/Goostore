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
    private String ProfilePic;

    //Constructor
    public User(String email, String password, String name, String phoneNumber, String address, String bankAccount) {
        Name = name;
        Password = password;
        Email = email;
        PhoneNumber = phoneNumber;
        Address = address;
        BankAccount = bankAccount;
        ProfilePic = "";
    }

    //DO NOT DELETE THIS
    public User() {}

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

    public String getProfilePic() { return ProfilePic; }

    public void setEmail(String email) {
        Email = email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setBankAccount(String bankAccount) {
        BankAccount = bankAccount;
    }

    public void setProfilePic(String profilePic) { ProfilePic = profilePic; }

    //Registration Method
    public boolean Registration (String email, String password, String name, String phoneNumber, String address, String bankAccount) {
        if (email.equals("") || password.equals("") || name.equals("") || phoneNumber.equals("") || address.equals("") || bankAccount.equals("")) return false;
        Map<String, String> m = new HashMap<String, String>();
        m.put("Password", password);
        m.put("Name", name);
        m.put("PhoneNumber", phoneNumber);
        m.put("Address", address);
        m.put("BankAccount", bankAccount);
        return true;
    }

}
