package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.R.layout.load;
import static com.example.myapplication.R.layout.no_request;
import static com.example.myapplication.R.layout.recycler_view;

public class MainActivity extends AppCompatActivity implements Serializable {

    private android.support.v7.widget.RecyclerView recyclerView;
    private android.support.v7.widget.RecyclerView.LayoutManager layoutManager;
    List<Request> requestList;
    RequestAdapter adapter;
    int i=0;
    private int layout = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("send"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                removeReceiver, new IntentFilter("remove"));
    }
    /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(load);
        progressBar  = findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);

        requestList = new ArrayList<>();
        /*recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RequestAdapter(this, requestList);
        recyclerView.setAdapter(adapter);/*

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d("try", user.getUid());
                                FirebaseDatabase.getInstance().getReference(user.getUid()+"/uid").setValue(user.getUid());
                                String token = FirebaseInstanceId.getInstance().getToken();
                                FirebaseDatabase.getInstance().getReference(user.getUid() + "/token").setValue(token);
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TRY", "signInAnonymously:failure", task.getException());
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        else{
            Log.i("mean", mAuth.getCurrentUser().getUid());
            updateUI(mAuth.getCurrentUser());
        }
        updateUI(null);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("send"));

    }*/

    private void updateUI(FirebaseUser user) {
        i = 0;
        requestList = new ArrayList<>();
        requestList.clear();

        FirebaseFirestore.getInstance().collection("users").document("user1").collection("requests").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        i+=1;
                        if(i==1) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //list.add(document.getData().get("MSG").toString());
                                    requestList.add(
                                            new Request(
                                                    document.getData().get("usrname").toString(),
                                                    document.getData().get("usrage").toString(),
                                                    document.getData().get("usrsex").toString(),
                                                    document.getData().get("drivername").toString(),
                                                    document.getData().get("contactno").toString(),
                                                    document.getData().get("vehicleno").toString()
                                            )
                                    );
                                    Log.d("data", document.getId() + " => " + document.getData());

                                }
                            } else {
                                Log.d("data", "Error getting documents: ", task.getException());
                            }
                            //createUI();
                            //adapter.notifyDataSetChanged();
                            if(requestList.size()!=0) {
                                layout = 1;
                                setContentView(recycler_view);
                                recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recyclerView);
                                recyclerView.setHasFixedSize(true);

                                layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);

                                adapter = new RequestAdapter(getApplicationContext(), requestList);
                                recyclerView.setAdapter(adapter);
                            }
                            else{
                                layout=2;
                                setContentView(no_request);
                            }
                        }
                    }
                });
    }

    private BroadcastReceiver removeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int k = intent.getIntExtra("index", 0);
            requestList.remove(k);
            adapter.notifyDataSetChanged();
            if(requestList.size()==0){
                layout = 2;
                setContentView(no_request);
            }
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
           //String message = intent.getStringExtra("msg");
            ArrayList<String>list = intent.getStringArrayListExtra("Data");
            String usrname="hey", usrage="hey", usrsex="hey", drivername="hey", contactno="hey", vehicleno="hey";
            for(int i=0; i<12;i+=2){
                if(list.get(i).equals("usrname")){
                    usrname = list.get(i+1);
                }
                else if(list.get(i).equals("usrage")){
                    usrage = list.get(i+1);
                }
                else if(list.get(i).equals("usrsex")){
                    usrsex = list.get(i+1);
                }
                else if(list.get(i).equals("drivername")){
                    drivername = list.get(i+1);
                }
                else if(list.get(i).equals("contactno")){
                    contactno = list.get(i+1);
                }
                else if(list.get(i).equals("vehicleno")){
                    vehicleno = list.get(i+1);
                }
            }
            if(layout==2) {
                requestList.clear();
                requestList.add(
                        new Request(
                                usrname,
                                usrage,
                                usrsex,
                                drivername,
                                contactno,
                                vehicleno
                        )
                );
                setContentView(recycler_view);
                recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                adapter = new RequestAdapter(getApplicationContext(), requestList);
                recyclerView.setAdapter(adapter);
            }
            else{
                requestList.add(
                        new Request(
                                usrname,
                                usrage,
                                usrsex,
                                drivername,
                                contactno,
                                vehicleno
                        )
                );
                adapter.notifyDataSetChanged();
            }
            layout = 1;

            Log.i("Here", "Here");

            //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        layout = 0;
        setContentView(load);

        updateUI(null);
    }
}


