package com.krish.bgmitournament.Orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krish.bgmitournament.R;

import java.util.HashMap;

public class Choose_Squad_Activity extends AppCompatActivity {

    private String referenceId;
    private TextInputLayout player1Id,player2Id,player3Id,player4Id,player1Name,player2Name,player3Name,player4Name;
    private Button joinBtn;
    private DatabaseReference reference1,reference2;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_squad);

        referenceId = getIntent().getStringExtra("ref_no");

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        player1Id=findViewById(R.id.player1Id);
        player2Id=findViewById(R.id.player2Id);
        player3Id=findViewById(R.id.player3Id);
        player4Id=findViewById(R.id.player4Id);
        player1Name=findViewById(R.id.player1Name);
        player2Name=findViewById(R.id.player2Name);
        player3Name=findViewById(R.id.player3Name);
        player4Name=findViewById(R.id.player4Name);
        joinBtn=findViewById(R.id.joinBtn);

        reference1 = FirebaseDatabase.getInstance().getReference().child("Total Orders");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Total Orders");

        getSquad();
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinSquad();
            }
        });
    }

    private void joinSquad() {
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap hp = new HashMap();
                hp.put("p1Id",player1Id.getEditText().getText().toString());
                hp.put("p2Id",player2Id.getEditText().getText().toString());
                hp.put("p3Id",player3Id.getEditText().getText().toString());
                hp.put("p4Id",player4Id.getEditText().getText().toString());
                hp.put("p1N",player1Name.getEditText().getText().toString());
                hp.put("p2N",player2Name.getEditText().getText().toString());
                hp.put("p3N",player3Name.getEditText().getText().toString());
                hp.put("p4N",player4Name.getEditText().getText().toString());

                reference1.child(referenceId).child(user.getUid()).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(Choose_Squad_Activity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSquad() {
        reference2.child(referenceId).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String p1Id = snapshot.child("p1Id").getValue(String.class);
                String p2Id = snapshot.child("p2Id").getValue(String.class);
                String p3Id = snapshot.child("p3Id").getValue(String.class);
                String p4Id = snapshot.child("p4Id").getValue(String.class);
                String p1Name = snapshot.child("p1N").getValue(String.class);
                String p2Name = snapshot.child("p2N").getValue(String.class);
                String p3Name = snapshot.child("p3N").getValue(String.class);
                String p4Name = snapshot.child("p4N").getValue(String.class);

                player1Id.getEditText().setText(p1Id);
                player2Id.getEditText().setText(p2Id);
                player3Id.getEditText().setText(p3Id);
                player4Id.getEditText().setText(p4Id);
                player1Name.getEditText().setText(p1Name);
                player2Name.getEditText().setText(p2Name);
                player3Name.getEditText().setText(p3Name);
                player4Name.getEditText().setText(p4Name);

                if (!p1Id.equals("")||!p2Id.equals("")||!p3Id.equals("")||!p4Id.equals("")||!p1Name.equals("")||!p2Name.equals("")||!p3Name.equals("")||!p4Name.equals("")){
                    joinBtn.setText("Update");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}