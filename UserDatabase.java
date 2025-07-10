package com.zybooks.projecttwo;
import android.provider.BaseColumns;


// Created a container and inheriting SQLiteOpenHelper to allows SQLite functions
public class UserDatabase{

    //Private constructor. According to the Android documentation it prevents accidental
    //instatiation which is not needed for this class
   private UserDatabase() {}

    //Inner class which defines our first table and implementing BaseColumns allows us to inherit
    //the _id field which are used as primary keys
    public static class UserBase implements BaseColumns {
       public static final String TABLE_NAME ="users";
       public static final String COLUMN_NAME_USERNAME = "username";
       public static final String COLUMN_NAME_PASSWORD = "password";
       public static final String COLUMN_NAME_PHONE_NUMBER = "phoneNumber";
    }
// Second table created for the weight logs. I needed to connect a user to its own weight log table,
    // but was not sure if that was possible. What I did in this instance is create the parentid column.
    // This holds the _id found in the users table. Once the user logs in, their _id is captured and is
    // attached to each weight log created by that user. When doing a query on the database, the parent id/user_id(same id) is used.
    //TODO connect both tables
    public static class Weightlogs implements BaseColumns {
       public static final String TABLE_NAME="weightlogs";
       public static final String COLUMN_NAME_PARENT ="parentid";
       public static final String COLUMN_NAME_DATE = "date";
       public static final String COLUMN_NAME_WEIGHT = "weight";
       public static final String COLUMN_NAME_COMMENT = "comment";
    }


    // The following uses a SQL string function to create the users table
     static final String SQL_CREATE_USERS =
            "CREATE TABLE " + UserBase.TABLE_NAME + " (" +
                    UserBase._ID + " INTEGER PRIMARY KEY, " +
                    UserBase.COLUMN_NAME_USERNAME + " TEXT, " +
                    UserBase.COLUMN_NAME_PASSWORD + " TEXT, " +
                    UserBase.COLUMN_NAME_PHONE_NUMBER + " TEXT)";

   // Another SQL function that deletes the table
    static final String SQL_DELETE_USERS =
           "DROP TABLE IF EXISTS " + UserBase.TABLE_NAME;


         static final String SQL_CREATE_LOGS =
                "CREATE TABLE " + Weightlogs.TABLE_NAME + " (" +
                        Weightlogs._ID + " INTEGER PRIMARY KEY, " +
                        Weightlogs.COLUMN_NAME_PARENT + " TEXT, " +
                        Weightlogs.COLUMN_NAME_DATE + " TEXT, " +
                        Weightlogs.COLUMN_NAME_WEIGHT + " TEXT," +
                        Weightlogs.COLUMN_NAME_COMMENT + " TEXT)";

        static final String SQL_DELETE_LOGS =
                "DROP TABLE IF EXISTS " + Weightlogs.TABLE_NAME;
}
