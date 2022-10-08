package com.krish.bgmitournament;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.krish.bgmitournament.Login.LoginActivity;
import com.krish.bgmitournament.Login.RegisterActivity;
import com.krish.bgmitournament.Orders.My_Order_Fragment;
import com.krish.bgmitournament.Tickets.Afternoon_Fragment;
import com.krish.bgmitournament.Tickets.Evening_Fragment;
import com.krish.bgmitournament.Tickets.Morning_Fragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout navDrawer;
    private NavigationView navView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    private View header;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private CardView morning,afternoon,evening,myOrder;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navDrawer = findViewById(R.id.navDrawer);
        navView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);

        morning = findViewById(R.id.morning);
        morning.setOnClickListener(this);
        afternoon = findViewById(R.id.afternoon);
        afternoon.setOnClickListener(this);
        evening = findViewById(R.id.evening);
        evening.setOnClickListener(this);
        myOrder = findViewById(R.id.myOrder);
        myOrder.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fragmentManager = getSupportFragmentManager();



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toggle=new ActionBarDrawerToggle(this,navDrawer,toolbar,R.string.Start,R.string.Close);
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.bringToFront();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        Intent intent = new Intent(MainActivity.this,Profile_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.orders:
                        My_Order_Fragment my_order_fragment = new My_Order_Fragment();
                        fragmentManager.beginTransaction().replace(R.id.frameLayout,my_order_fragment).addToBackStack("home").commit();
                        break;
                    case R.id.help:
                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                        intent1.putExtra(Intent.EXTRA_EMAIL,new String[]{"20ce089@charusat.edu.in"});
                        intent1.setType("text/plain");
                        intent1.setPackage("com.google.android.gm");
                        startActivity(intent1);
                        break;

                }
                return true;
            }
        });
        header = navView.getHeaderView(0);
        if (user!=null){
            ImageView userImage = (ImageView) header.findViewById(R.id.userprofilepic);
            Picasso.get().load(user.getPhotoUrl()).placeholder(getDrawable(R.drawable.profile)).into(userImage);

            TextView userName = (TextView) header.findViewById(R.id.userName);
            userName.setText(user.getDisplayName());

            TextView userEmail = (TextView) header.findViewById(R.id.userEmail);
            userEmail.setText(user.getEmail());

            Button logout = (Button) header.findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (user==null){
            startActivity(new Intent(MainActivity.this, LoginActivity .class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.morning:
                Morning_Fragment morning_fragment = new Morning_Fragment();
                fragmentManager.beginTransaction().replace(R.id.frameLayout,morning_fragment).addToBackStack("home").commit();
                break;
            case R.id.afternoon:
                Afternoon_Fragment afternoon_fragment = new Afternoon_Fragment();
                fragmentManager.beginTransaction().replace(R.id.frameLayout,afternoon_fragment).addToBackStack("home").commit();
                break;
            case R.id.evening:
                Evening_Fragment evening_fragment = new Evening_Fragment();
                fragmentManager.beginTransaction().replace(R.id.frameLayout,evening_fragment).addToBackStack("home").commit();
                break;
            case R.id.myOrder:
                My_Order_Fragment my_order_fragment = new My_Order_Fragment();
                fragmentManager.beginTransaction().replace(R.id.frameLayout,my_order_fragment).addToBackStack("home").commit();
                break;
        }
    }
}