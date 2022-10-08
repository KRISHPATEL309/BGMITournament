package com.krish.bgmitournament;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class Profile_Activity extends AppCompatActivity {

    private ImageView profileImg;
    private EditText profileName;
    private Button updatebtn;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImg = findViewById(R.id.profileImg);
        profileName = findViewById(R.id.profileName);
        updatebtn = findViewById(R.id.updatebtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Picasso.get().load(user.getPhotoUrl()).into(profileImg);
        profileName.setText(user.getDisplayName());


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 updateProfile();
            }
        });
    }

    private void updateProfile() {
        if (profileName.getText().toString().isEmpty()){
            profileName.setError("Please Enter Name...");
            profileName.requestFocus();
        }else {
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(profileName.getText().toString()).build();
            user.updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(Profile_Activity.this, "Updated...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}