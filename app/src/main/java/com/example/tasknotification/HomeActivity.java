package com.example.tasknotification;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    ImageButton btnLogout;
    ImageButton task;
    ImageButton delete;
    RecyclerView mFirestoreList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mFirebaseAuth;
    String userid;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestoreList = findViewById(R.id.firestoreList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        btnLogout = findViewById(R.id.logout);
        task = findViewById(R.id.task);
        delete = findViewById(R.id.delete);

        userid = mFirebaseAuth.getCurrentUser().getUid();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("LOGOUT");
                builder.setMessage("Want to logout ?");
                builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(HomeActivity.this, loginActivity.class));
                        finish();
                    }
                }).setNegativeButton("CONTINUE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Intent intSignUp = new Intent(loginActivity.this, MainActivity.class);
                        //startActivity(intSignUp);
                    }
                });
                builder.create().show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText name;
                name = new EditText(HomeActivity.this);
                name.getPaddingLeft();
                name.setHint(" Task Name");
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("DELETE Task");
                builder.setMessage("Task Name : ");
                builder.setView(name);
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userid = mFirebaseAuth.getCurrentUser().getUid();
                        final String item ;
                        item = name.getText().toString();
                        final Task<Void> documentReference= firebaseFirestore.collection(userid).document(item).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(HomeActivity.this, "TASK  DELETED!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomeActivity.this," Unsuccessful, Please Try Again!" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TaskActivity.class));
//               
            }
        });

        Query query = firebaseFirestore.collection(userid); //query
        //recycler option
        FirestoreRecyclerOptions<com.example.tasknotification.Task> options= new FirestoreRecyclerOptions.Builder<com.example.tasknotification.Task>().setQuery(query, com.example.tasknotification.Task.class).build();
        adapter = new FirestoreRecyclerAdapter<com.example.tasknotification.Task, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new ProductViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull com.example.tasknotification.Task model) {
                holder.list_name.setText(model.getTask());
                holder.list_quantity.setText(model.getTime());
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
//        onStart();
//        adapter.startListening();
    }
    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }
    //view holder
    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView list_name;
        private TextView list_quantity;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.list_name);
            list_quantity = itemView.findViewById(R.id.list_quantity);

        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}