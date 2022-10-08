package com.krish.bgmitournament.Tickets;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

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

public class Morning_Fragment extends Fragment {
    private RecyclerView morningMatch;
    private List<MatchData> list1;
    private DatabaseReference reference,dbRef;
    private MatchAdapter adapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_morning_, container, false);
        morningMatch = view.findViewById(R.id.morningMatch);
        progressBar = view.findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Matches");

        morningMatch();
        return view;
    }

    private void morningMatch() {
        dbRef = reference.child("Morning");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<MatchData>();
                    morningMatch.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        MatchData data = snapshot1.getValue(MatchData.class);
                        list1.add(0,data);
                    }
                    morningMatch.setHasFixedSize(true);
                    morningMatch.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new MatchAdapter(getContext(),list1);
                    progressBar.setVisibility(View.GONE);
                    morningMatch.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}