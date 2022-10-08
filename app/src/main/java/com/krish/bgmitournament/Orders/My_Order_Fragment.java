package com.krish.bgmitournament.Orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krish.bgmitournament.R;
import com.krish.bgmitournament.Tickets.MatchAdapter;
import com.krish.bgmitournament.Tickets.MatchData;

import java.util.ArrayList;
import java.util.List;

public class My_Order_Fragment extends Fragment {
    private RecyclerView myorder;
    private List<MatchData> list1;
    private DatabaseReference reference,dbRef;
    private OrderAdapter adapter;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my__order_, container, false);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        myorder = view.findViewById(R.id.myorder);
        progressBar = view.findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Orders");

        myorder();

        return view;
    }

    private void myorder() {
        dbRef = reference.child(user.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<MatchData>();
                myorder.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    MatchData data = snapshot1.getValue(MatchData.class);
                    list1.add(0,data);
                }
                myorder.setHasFixedSize(true);
                myorder.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new OrderAdapter(getContext(),list1);
                progressBar.setVisibility(View.GONE);
                myorder.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}