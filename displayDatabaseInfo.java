package com.zybooks.projecttwo;

// Import of various xml files
import static com.zybooks.projecttwo.MainActivity.user_id;
import static  com.zybooks.projecttwo.R.layout.add_new_log_pop_up;
import static com.zybooks.projecttwo.R.layout.deny_confirmation_dialog;
import static com.zybooks.projecttwo.R.layout.educational_ui_layout;
import static com.zybooks.projecttwo.R.layout.receive_sms_notifications_page;
import static com.zybooks.projecttwo.R.layout.update_log_popup;

// import of various libraries,etc.
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

// This file mainly handles the logic of the screen after login where we list the weight logs, and
// have a couple of buttons for adding a new weight goal and setting the weight goal with the option
// of receiving notifications. Also implements the RecyclerViewInterface in order to manipulate each item in the view
public class displayDatabaseInfo extends AppCompatActivity implements RecyclerViewInterface {
// Variable declarations
    List<WeightLog> log = new ArrayList<WeightLog>();
    // Variable declarations for adding a new log
    FloatingActionButton fab;
    EditText dateInput;
    EditText weightInput;
    EditText commentInput;
    Button saveButton;


    // Variable declarations for setting the weight goal and related functionalities(text messaging
    // and permission control
    EditText weightGoalInput;
    Button setWeightGoalButton;
    Button saveButtonForNotifications;
    String textSentMessage;
    String weightGoalInputString;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    // Adapter instance created for the recyclerView
    MyAdapter adapter;

    // The following two variables are used when using CRUD functions on the database
    SQLiteDatabase dbReadable;
    SQLiteDatabase dbWritable;

    // inflater used to utilize xml files
    LayoutInflater inflater;


    //TODO add phone number input in the future
    EditText phoneNumberInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity userId = new MainActivity();
        // Displays the activity_display_database_info_xml
        setContentView(R.layout.activity_display_database_info);

        // An inflater to display the other xml files in pop up form
        inflater = LayoutInflater.from(displayDatabaseInfo.this);
        View textConfirmationPopUp = inflater.inflate(deny_confirmation_dialog, null);

        // The following is used to display the list of logs in a neat format. It finds the view
        // for the grid recycler which is inside the activity_display_database_info xml file.
        RecyclerView recyclerView = findViewById(R.id.gridRecyclerView);

        // the fab button(floating action button) used in adding a new log entry
        fab = findViewById(R.id.fab);

        // Setting up the setWeightGoal button at the bottom of the screen
        setWeightGoalButton = findViewById(R.id.setWeightGoalButton);


        // The following statements set up the neccessary components for database manipulation in SQLite
        // Using the WeightLogDatabaseDbHelper class created in its own java file, we create an object by passing it context
        WeightLogDatabaseDbHelper dbHelper = new WeightLogDatabaseDbHelper(displayDatabaseInfo.this);

        // dbWritable and dbReadable are used to write or read from the database
        dbWritable = dbHelper.getWritableDatabase();
        dbReadable = dbHelper.getReadableDatabase();

        // values variable will be used to add new entries to the database
        ContentValues values = new ContentValues();

        // parentsIds variable is used to find the parent id connecting the weight log table and
        // the user table. More will be explained in the userdatabase files
        String parentIds = String.valueOf(user_id);


        // I will use all of these table fields after query
        String[] projection = {
                "_id",
                "parentid",
                "date",
                "weight",
                "comment"
        };

        // The following is setting up query options. In this instance I want to search for entries
        // with the matching parent_id connected to the instanced user
        String selection = "parentid LIKE ?";
        String[] selectionArgs = {parentIds};
        String sortOrder = "date DESC";

        // The actual query. Using a cursor, it will read the database and retrieve the matching
        // string in selectionArgs
        Cursor cursor = dbReadable.query(
                "weightlogs",
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );


        // The if statement makes sure that the cursor has entries with at least one entry. If true
        // then the values will be extracted and placed as strings in several variables
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                Double weight = cursor.getDouble(cursor.getColumnIndexOrThrow("weight"));
                String comment = cursor.getString(cursor.getColumnIndexOrThrow("comment"));
                Long logId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));

                // The extracted data will then be added to a log used in various functions in the app
                log.add(new WeightLog(date, weight, comment, logId));
                // While statement checks to see if it has reached the last element
            } while (cursor.moveToNext());

            // Cursor gets closed to save on resources
            cursor.close();
        }



;        // The recyclerView object of class RecyclerView uses the setLayoutManager in order to prime
        // the use of custom layouts
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // An adapter is instanced by passing it context, the log array holding data retrieved by the query,
        // and the RecycvlerView interface defined in its own separate file.
        adapter = new MyAdapter(this, log, this);

        // The recyclerview sets the adapter in order to prime the binding of the log arraylist to the views.
       recyclerView.setAdapter(adapter);


        // The requestPermission launcher is used for asking permission from the user directly through the standard permission pop up.
        // If user taps on "allow" then the following if statement will execute the sendSMS method. if they press "deny" then a toast
        //will say that SMS messages are disabled.
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                //TODO Permission granted logic
                Toast.makeText(this, "Confirmation message will be sent to 714-321-3434", Toast.LENGTH_SHORT).show();
                sendSMS(textSentMessage, "7143530216");
            } else {
                Toast.makeText(displayDatabaseInfo.this, "SMS messages is disabled", Toast.LENGTH_SHORT).show();
            }
        });


        // A listener that sets up the set weight goal and notifications button
        setWeightGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View setWeightGoalPage = inflater.inflate(receive_sms_notifications_page, null);
                weightGoalInput = setWeightGoalPage.findViewById(R.id.weightGoalInput);
                saveButtonForNotifications = setWeightGoalPage.findViewById(R.id.saveButtonForNotifications);


                // the notficiationsDialog object is created using the AlertDialog class and utilizes the nested Builder
                // class to create the alert dialog pop up. It sets up the view that allows the user
                // to input their weight goal
               AlertDialog notificationsDialog = new AlertDialog.Builder(displayDatabaseInfo.this)
                        .setView(setWeightGoalPage)
                        .create();

                // A listener that is set up for the save button whenever the user is done inputting
                // their goal weight. The function within this method includes permission control and
                //sms sending logic
                saveButtonForNotifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // The following if statement checks whether the app already has permission for sending SMS by accessing the manifest and comparing it
                        // to the constant value PERMISSION_GRANTED. If true then we will call upon the sendSMS() custom method that I created later in the file.
                        // if false then it'll jump to the else if statement to display an educational UI explaining why the user should enable permissions. The third
                        // conditional statement will only execute once at the beginning of first opening the app to request permission and then handle the user's response.
                        if (ContextCompat.checkSelfPermission(displayDatabaseInfo.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            weightGoalInputString = weightGoalInput.getText().toString();
                            textSentMessage = "You have changed your weight goal to: " + weightGoalInputString + ". We will notify you whenever you are close to reaching your goal";
                            sendSMS(textSentMessage, "7143530216");
                            Toast.makeText(displayDatabaseInfo.this, "Text message sent to phone number 714-353-0216", Toast.LENGTH_LONG).show();
                        } else if (ActivityCompat.shouldShowRequestPermissionRationale(displayDatabaseInfo.this, Manifest.permission.SEND_SMS)) {
                            notificationsDialog.dismiss();
                            View educationalUI= inflater.inflate(educational_ui_layout, null);
                            AlertDialog educationalUIAlertDialog = new AlertDialog.Builder(displayDatabaseInfo.this)
                                    .setView(educationalUI)
                                    .create();
                            educationalUIAlertDialog.show();
                        } else {
                            requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
                        }
                        notificationsDialog.dismiss();
                    }
                });
                notificationsDialog.show();
            }
        });


        // A listener for the floating action button that takes the user to the log weight pop-up
        // screen
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = inflater.inflate(add_new_log_pop_up, null);
                dateInput = popupView.findViewById(R.id.dateInput);
                weightInput = popupView.findViewById(R.id.weightInput);
                commentInput = popupView.findViewById(R.id.commentInput);
                saveButton = popupView.findViewById(R.id.saveButton);
                AlertDialog dialog = new AlertDialog.Builder(displayDatabaseInfo.this)
                        .setView(popupView)
                        .create();
                // Another listener for when the user presses the save button to save their weight logs details
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Converts the edit text inputs into strings to put be saved to the array list.
                        String dateInputEntered = dateInput.getText().toString();
                        String weightInputEntered = weightInput.getText().toString();
                        String commentInputEntered = commentInput.getText().toString();
                        Long id;

                        values.put(UserDatabase.Weightlogs.COLUMN_NAME_PARENT, parentIds);
                        values.put("date",dateInputEntered);
                        values.put("weight",weightInputEntered);
                        values.put("comment",commentInputEntered);
                        long newRowId = dbWritable.insert("weightlogs",null, values);


                        Toast.makeText(displayDatabaseInfo.this, "Saved " + dateInputEntered + " WeightLog", Toast.LENGTH_SHORT).show();
                        double weightInputDouble = Double.parseDouble(weightInputEntered);
                        log.add(new WeightLog(dateInputEntered, weightInputDouble, commentInputEntered, newRowId));
                        // I call upon the setAdapter again to display the updated weight logs again
                        recyclerView.setAdapter(adapter);

                        dialog.dismiss();
                    }
                });
            dialog.show();

            }
        });


    }
    // The sendSMS method will only be used if the app has explicit permission from the user.
    public void sendSMS(String message, String phoneNumber) {
        // Creates an instance with the default subscription id
        SmsManager smsManager = SmsManager.getDefault();
        //Method that sends a text message
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    // The logic that occurs whenever a user taps the delete button on a specific row. The delete button listener is in
    // the ViewHolder class file. The only argument is the position of the view item in the recycler view.
    // The logic of the function consists of accessing the arraylist's specific index and stores that
    // specific element(weight log) in a temporary variable. Each log has an ID variable attached to it
    //(part of BaseColumn) we then match that Id with the same one in the database in the weightlogs table.
    // Once found and deleted I print out a statement through logcat to confirm the number of rows deleted.
    // Lastly I delete the weight log from the local arraylist (log) and notify the adapter that an element
    // is deleted in order for the view to be removed.
    @Override
    public void onDeleteButtonClick(int position) {
       WeightLog tempLog;
       Long tempId;


       tempLog = log.get(position);
       String selection = "_id LIKE ?";
       tempId = tempLog.getId();
       String[] selectionArgs = new String[]{String.valueOf(tempId)};

        int deletedRows = dbReadable.delete("weightlogs", selection, selectionArgs);
        Log.d("Number of rows deleted: ", String.valueOf(deletedRows));

        log.remove(position);
        adapter.notifyItemRemoved(position);

    }

    // The logic used whenever the user taps on an entry to change any values. This is unfinished due to
    // time. First an alert dialog is triggered that prompted the user to enter new values to an
    // existing log. This is where I stopped. The original plan was to extract the values of the input
    // and access the SQLite by searching by its row id and change the values.
    @Override
    public void onClickForUpdate(int position) {
        String updatedDate;
        String updatedWeight;
        String updatedComment;

        String originalDate;
        String originalWeight;
        String originalComment;


        WeightLog originalWeightLog;


        originalWeightLog = log.get(position);

        View logUpdatePopup = inflater.inflate(update_log_popup, null);


        updatedDate = String.valueOf(logUpdatePopup.findViewById(R.id.newWeightField));
        updatedWeight = String.valueOf(logUpdatePopup.findViewById(R.id.newDateField));
        updatedComment = String.valueOf(logUpdatePopup.findViewById(R.id.newCommentField));

        //logUpdatePopup.setAdapter(adapter);

                AlertDialog updatePopup = new AlertDialog.Builder(displayDatabaseInfo.this)
                .setView(logUpdatePopup)
                .create();

        updatePopup.show();







    }

}
