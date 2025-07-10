package com.zybooks.projecttwo;


import static com.zybooks.projecttwo.R.layout.create_account_popup;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


// This file mainly handles the functionality for the login screen. Will add more functionality

public class MainActivity extends AppCompatActivity {
    // Declared variables for the login screen
    Button submitButton;
    EditText userNameInput;
    EditText passwordInput;
    String userNameString;
    String passwordString;
    String username;
    String password;
    Button createButton;
    EditText newUsernameField;
    EditText newPasswordField;
    EditText newPasswordConfirmationField;
    String newUsernameString;
    String newPasswordString;
    String newPasswordConfirmationString;
    Button createAccountButton;
    Boolean popUpBool = false;
    View createAccountPopUp;

    public static int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        //Inflater for various xml files
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        // Variables and instances to access the SQLite database
        UserDatabaseDbHelper dbHelper = new UserDatabaseDbHelper(MainActivity.this);
        SQLiteDatabase dbWritable = dbHelper.getWritableDatabase();
        SQLiteDatabase dbReadable = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();


        // Buttons with listeners depending if the user logins with credentials or wants to make a
        // new user account
        submitButton = findViewById(R.id.submitButton);
        createButton = findViewById(R.id.createLoginButton);


        // A listener for the submit button that changes screen when the user presses the button
        // through intent
        submitButton.setOnClickListener(view -> {
            userNameInput = findViewById(R.id.usernameText);
            passwordInput = findViewById(R.id.passwordText);
            userNameString = userNameInput.getText().toString();
            passwordString = passwordInput.getText().toString();
            // newRowId is not used, but it has the rowId of the new row
            long newRowId = dbWritable.insert("users", null, values);


            //Checking to see if user already has an account
            String[] projection = {
                    "_id",
                    "username",
                    "password"
            };

            String selection = " username LIKE ?";
            String[] selectionArgs = {userNameString};
            String sortOrder = "username DESC";

            Cursor cursor = dbReadable.query(
                    "users",
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            // Makes sure cursor is not empty
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    // If statements that checks whether entered username matches one in the SQLite database
                    if (username.equals(userNameString)) {
                        password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                        //If username matches one in the database then does the same with the password
                        if (password.equals(passwordString)) {
                            // If both username and password are found then user will move on to the
                            // next screen. Also the row _id will be saved to be used on the second table (weight logs)
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_LONG).show();
                            user_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                            Intent intent = new Intent(MainActivity.this, displayDatabaseInfo.class);
                            startActivity(intent);
                        }
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } else {
                Toast.makeText(this, "Username not found, please create an account by pressing create account button", Toast.LENGTH_LONG).show();
            }

        });


        // Create listener will trigger a alertdialog prompting the user to enter a new username and
        // the password two times to confirm.
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccountPopUp = inflater.inflate(create_account_popup,null,false);
                createAccountButton = createAccountPopUp.findViewById(R.id.createAccountButton);
                newUsernameField = createAccountPopUp.findViewById(R.id.newUsernameField);
                newPasswordField = createAccountPopUp.findViewById(R.id.newPasswordField);
                newPasswordConfirmationField = createAccountPopUp.findViewById(R.id.newPasswordConfirmationField);

                AlertDialog createNewAccountDialog = new AlertDialog.Builder(MainActivity.this)
                        .setView(createAccountPopUp)
                        .create();
                    createNewAccountDialog.show();

                    // Once the user enters a new username and password then the if statement will
                    // check if the two password fields match
                    createAccountButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newUsernameString = newUsernameField.getText().toString();
                            newPasswordString = newPasswordField.getText().toString();
                            newPasswordConfirmationString = newPasswordConfirmationField.getText().toString();

                            if (newPasswordString.equals(newPasswordConfirmationString)) {
                                values.put("username", newUsernameString);
                                values.put("password", newPasswordString);
                                Toast.makeText(MainActivity.this, "Account Created, please login", Toast.LENGTH_LONG).show();
                                createNewAccountDialog.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Both password fields must match try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




            }
        });
    }
}