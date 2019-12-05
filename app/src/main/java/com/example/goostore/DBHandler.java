package com.example.goostore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    //Database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    public static final String TABLE_NAME = "UserData";

    public static final String COLUMN_EMAIL = "userEmail";
    public static final String COLUMN_NAME = "userName";
    public static final String COLUMN_PWD = "userPWD";
    public static final String COLUMN_PHONE = "userPhone";
    public static final String COLUMN_ADDRESS = "userAddress";
    public static final String COLUMN_BANKACC = "userBankAcc";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        Log.d("test", "DB handler finally worked");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_EMAIL + " TEXT, " + COLUMN_NAME + " TEXT, " +
                COLUMN_PWD + " TEXT, " + COLUMN_PHONE + " TEXT, " + COLUMN_ADDRESS + " TEXT, " + COLUMN_BANKACC + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(User user) {

        User existUser = findUser(user.getEmail());

        if (existUser != null) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_PWD, user.getPassword());
        values.put(COLUMN_PHONE, user.getPhoneNumber());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_BANKACC, user.getBankAccount());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
        return true;
    }

    public User findUser(String userEmail) {
        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = '" +
                userEmail + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(QUERY, null);

        User user = new User("", "", "", "", "", "");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setEmail(cursor.getString(0));
            user.setName(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setPhoneNumber(cursor.getString(3));
            user.setAddress(cursor.getString(4));
            user.setBankAccount(cursor.getString(5));
            cursor.close();
        }
        else {
            user = null;
        }
        db.close();
        return user;
    }
}
