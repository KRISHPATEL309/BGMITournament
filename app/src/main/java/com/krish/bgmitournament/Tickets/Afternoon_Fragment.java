package com.krish.bgmitournament.Tickets;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krish.bgmitournament.R;

import java.util.ArrayList;
import java.util.List;

public class Afternoon_Fragment extends Fragment {

    private RecyclerView afternoonMatch;
    private List<MatchData> list2;
    private DatabaseReference reference,dbRef;
    private ProgressBar progressBar;
    private MatchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_afternoon_, container, false);

        afternoonMatch = view.findViewById(R.id.afternoonMatch);
        progressBar = view.findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Matches");

        afternoonMatch();

        return view;
    }
    private void afternoonMatch() {
        dbRef = reference.child("Afternoon");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<MatchData>();
                afternoonMatch.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    MatchData data = snapshot1.getValue(MatchData.class);
                    list2.add(0,data);
                }
                afternoonMatch.setHasFixedSize(true);
                afternoonMatch.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new MatchAdapter(getContext(),list2);
                progressBar.setVisibility(View.GONE);
                afternoonMatch.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}