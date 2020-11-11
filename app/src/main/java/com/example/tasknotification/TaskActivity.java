package com.example.tasknotification;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;

import static android.app.AlarmManager.RTC_WAKEUP;
import static okhttp3.internal.http.HttpDate.format;

public class TaskActivity extends AppCompatActivity{

    public   int notificationId = 1;
    RecyclerView mFirestoreList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mFirebaseAuth;
    String userid;
    FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestoreList = findViewById(R.id.firestoreList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userid = mFirebaseAuth.getCurrentUser().getUid();
        Button set = findViewById(R.id.button);

        EditText editText = findViewById(R.id.list_name);
        TimePicker timePicker = findViewById(R.id.timePicker);
        //DatePicker datePicker = findViewById(R.id.datePicker);

        Intent intent = new Intent(TaskActivity.this,AlarmReceiver.class);
        intent.putExtra("NotificationId",notificationId);
        intent.putExtra("todo",editText.getText().toString());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(TaskActivity.this,0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                final String task ,dt;
                task = editText.getText().toString();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                final long alarmStartTime = startTime.getTimeInMillis();
                Date time = startTime.getTime();
                dt = String.valueOf(time);

                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
                Toast.makeText(TaskActivity.this, "Done", Toast.LENGTH_SHORT).show();
                //editText.setText(String.valueOf(time));
                //Toast.makeText(TaskActivity.this, "Done!", Toast.LENGTH_SHORT).show();


                        userid = mFirebaseAuth.getCurrentUser().getUid();
                        final DocumentReference documentReference= firebaseFirestore.collection(userid).document(task);
                        Map<String,Object> user = new HashMap<>();
                        user.put("task",task);
                        user.put("time",dt);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TaskActivity.this, "ADDED successfully, Thank You!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TaskActivity.this, HomeActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TaskActivity.this," Unsuccessful, Please Try Again!" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}