package com.krish.bgmitournament.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krish.bgmitournament.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private CircleImageView registerImage;
    private TextInputLayout registerName,registerEmail,registerPassword;
    private Button registerButton;
    private TextView registerLogin;

    private final int REQ = 1;
    private Bitmap bitmap;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerImage = findViewById(R.id.registerImage);
        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);
        registerLogin = findViewById(R.id.registerLogin);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("UserImage");


        registerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 openGallery();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        registerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void checkValidation() {
        String name = registerName.getEditText().getText().toString();
        String email = registerEmail.getEditText().getText().toString();
        String password = registerPassword.getEditText().getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Fill All the Forms...", Toast.LENGTH_SHORT).show();
        }else {
            registerUser();
        }
    }

    private void registerUser() {
        auth.createUserWithEmailAndPassword(registerEmail.getEditText().getText().toString(),registerPassword.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            uploadImage();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void uploadImage() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(uid+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UpdateUser(uri);
                    }
                });
            }
        });
    }

    private void UpdateUser(Uri uri) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(registerName.getEditText().getText().toString())
                .setPhotoUri(uri)
                .build();
        auth.getCurrentUser().updateProfile(userProfileChangeRequest);
        auth.signOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openGallery() {
        Intent pickimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage,REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ &&  resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            registerImage.setImageBitmap(bitmap);
        }
    }
}