package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.zip.Inflater;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    public Context getContext() {
        return context;
    }

    private Context context ;
    private List<Request> requestList;

    public RequestAdapter(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_layout, null);
        RequestViewHolder requestViewHolder = new RequestViewHolder(view);
        return requestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i) {

        final Request request = requestList.get(i);

        requestViewHolder.usrname.setText(request.getUsrname());
        requestViewHolder.usrage.setText(request.getUsrage());
        requestViewHolder.usrsex.setText(request.getUsrsex());
        requestViewHolder.drivername.setText(request.getDrivername());
        requestViewHolder.contactno.setText(request.getContactno());
        requestViewHolder.vehicleno.setText(request.getVehicleno());

        requestViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Please", "Work");
                Intent i = new Intent("remove");
                i.putExtra("index", i);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(i);

                FirebaseFirestore.getInstance().collection("users").document("user1").collection("requests").whereEqualTo("usrname", request.getUsrname()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                    }
                                } else {
                                    Log.d("data", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView usrname, usrage, usrsex, drivername, contactno, vehicleno;
        public Button button;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            this.usrname = itemView.findViewById(R.id.usrname);
            this.usrage = itemView.findViewById(R.id.usrage);
            this.usrsex = itemView.findViewById(R.id.usrsex);
            this.drivername = itemView.findViewById(R.id.drivername);
            this.contactno = itemView.findViewById(R.id.contactno);
            this.vehicleno = itemView.findViewById(R.id.vehicleno);

            this.button = (Button) itemView.findViewById(R.id.endrequestButton);
        }
    }
}
