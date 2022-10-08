package com.krish.bgmitournament.Orders;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.krish.bgmitournament.R;
import com.krish.bgmitournament.Tickets.MatchData;
import com.razorpay.Checkout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import com.razorpay.PaymentResultListener;

public class Buy_Tickets_Activity extends AppCompatActivity implements PaymentResultListener{

    private CircleImageView matchimg;
    private TextView uploadDate,uploadTime,refId,slots,match_categories,matchDate,matchTime,room_id,room_password,matchMode,pricePool,entryfee;
    private Button paybtn;

    private String UT,UD,MT,MD,MDuration,MC,price,RID,RP,ref_no,reward,URI,slot;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference1,reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_tickets);
        Checkout.preload(getApplicationContext());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference1 = FirebaseDatabase.getInstance().getReference().child("Orders");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Total Orders");

        matchimg = findViewById(R.id.matchimg);
        uploadDate =findViewById(R.id.uploadDate);
        uploadTime = findViewById(R.id.uploadTime);
        refId = findViewById(R.id.refId);
        slots = findViewById(R.id.slot);
        match_categories = findViewById(R.id.match_categories);
        matchDate = findViewById(R.id.matchDate);
        matchTime = findViewById(R.id.matchTime);
        room_id = findViewById(R.id.room_id);
        room_password = findViewById(R.id.room_password);
        matchMode = findViewById(R.id.matchMode);
        pricePool = findViewById(R.id.pricePool);
        entryfee = findViewById(R.id.entryfee);


        paybtn = findViewById(R.id.paybtn);

        UT = getIntent().getStringExtra("UT");
        UD = getIntent().getStringExtra("UD");
        MT = getIntent().getStringExtra("MT");
        MD = getIntent().getStringExtra("MD");
        MDuration = getIntent().getStringExtra("MDuration");
        MC = getIntent().getStringExtra("MC");
        price = getIntent().getStringExtra("price");
        RID = getIntent().getStringExtra("RID");
        RP = getIntent().getStringExtra("RP");
        slot = getIntent().getStringExtra("slots");
        ref_no = getIntent().getStringExtra("ref_no");
        reward = getIntent().getStringExtra("reward");
        URI = getIntent().getStringExtra("URI");

        uploadTime.setText("Upload Time: "+UT);
        uploadDate.setText("Upload Date: "+UD);
        matchDate.setText(MD);
        matchTime.setText(MT);
        matchMode.setText(MC);
        match_categories.setText(MDuration);
        entryfee.setText(price);
        slots.setText("Slots: "+slot);
        room_id.setText("Room Id: "+RID);
        room_password.setText("Room Pass: "+RP);
        refId.setText("Ref No.: "+ref_no);
        pricePool.setText("First & Second Price: \n"+reward);

        Picasso.get().load(URI).into(matchimg);

        paybtn.setText("PAY "+price);

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();
            }
        });

    }

    private void makePayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_AxqLPexVxtJ3vm");
        /**
         * Instantiate Checkout
         */

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.cart);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "BGMI Tournament ");
            options.put("description", ref_no+ match_categories);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount",Integer.parseInt(price)*100);//pass amount in currency subunits
            options.put("prefill.email", user.getEmail());
//            options.put("prefill.contact","9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(Buy_Tickets_Activity.this, "Payment Successfull", Toast.LENGTH_SHORT).show();
        saveOrder();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(Buy_Tickets_Activity.this, "Payment Failed", Toast.LENGTH_SHORT).show();

    }

    private void saveOrder() {
//        private String uploaddate,uploadtime,date,time,referencenum,charge,maxParticipants,pricepool,matchTime,matchCategories,matchImage,room_Id,room_Pass;
//        private String uid,name,p1Id,p2Id,p3Id,p4Id,p1N,p2N,p3N,p4N,ref_no;

        MatchData matchData = new MatchData(UD,UT,MD,MT,ref_no,price,slot,reward,MDuration,MC,URI,RID,RP);
        reference1.child(user.getUid()).child(ref_no).setValue(matchData);

        Choose_Squad choose_squad = new Choose_Squad(user.getUid(),user.getDisplayName(),"","","","","","","","",ref_no);
        reference2.child(ref_no).child(user.getUid()).setValue(choose_squad);

    }
}