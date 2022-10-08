package com.krish.bgmitournament.Tickets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.krish.bgmitournament.Orders.Buy_Tickets_Activity;
import com.krish.bgmitournament.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewAdapter> {
    private Context context;
    private List<MatchData> list;

    public MatchAdapter() {
    }

    public MatchAdapter(Context context, List<MatchData> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MatchViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_layout,parent,false);

        return new MatchViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewAdapter holder, @SuppressLint("RecyclerView") int position) {
        MatchData currentItem = list.get(position);
        holder.matchDate.setText(currentItem.getDate());
        holder.matchTime.setText(currentItem.getTime());
        holder.uploadDate.setText("Upload Date: "+currentItem.getUploaddate());
        holder.uploadTime.setText("Upload Time: "+currentItem.getUploadtime());
        holder.refId.setText("Ref No.: "+currentItem.getReferencenum());
        holder.slots.setText("Slots: "+currentItem.getMaxParticipants());
        holder.match_categories.setText(currentItem.getMatchTime());
        holder.room_id.setText("Room Id: "+currentItem.getRoom_Id());
        holder.room_password.setText("Room Pass: "+currentItem.getRoom_Pass());
        holder.matchMode.setText(currentItem.getMatchCategories());
        holder.pricePool.setText("First & Second Price: \n"+currentItem.getPricepool());
        holder.entryfee.setText(currentItem.getCharge());

        Picasso.get().load(currentItem.getMatchImage()).into(holder.matchimg);

//        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setMessage("Are You sure want to delete this Match ?");
//                builder.setCancelable(true);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        DatabaseReference reference;
//                        FirebaseAuth auth;
//                        FirebaseUser user;
//
//                        auth=FirebaseAuth.getInstance();
//                        user=auth.getCurrentUser();
//
//                        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(user.getUid()).child(currentItem.getReferencenum());
//                        reference.removeValue()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(v.getContext(), "Deleted...", Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                        notifyItemRemoved(position);
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog dialog = null;
//                try {
//                    dialog = builder.create();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (dialog != null)
//                    dialog.show();
//            }
//        });
        DatabaseReference reference;
        FirebaseAuth auth;
        FirebaseUser user;
        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Orders");


        holder.mTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(currentItem.getReferencenum())){
                            Intent intent = new Intent(v.getContext(), Buy_Tickets_Activity.class);
                            intent.putExtra("UT",currentItem.getUploadtime());
                            intent.putExtra("UD",currentItem.getUploaddate());
                            intent.putExtra("MT",currentItem.getTime());
                            intent.putExtra("MD",currentItem.getDate());
                            intent.putExtra("MDuration",currentItem.getMatchTime());
                            intent.putExtra("MC",currentItem.getMatchCategories());
                            intent.putExtra("price",currentItem.getCharge());
                            intent.putExtra("RID",currentItem.getRoom_Id());
                            intent.putExtra("RP",currentItem.getRoom_Pass());
                            intent.putExtra("ref_no",currentItem.getReferencenum());
                            intent.putExtra("reward",currentItem.getPricepool());
                            intent.putExtra("slots",currentItem.getMaxParticipants());
                            intent.putExtra("URI",currentItem.getMatchImage());
                            context.startActivity(intent);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Already purchase This Ticket?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = null;
                            try {
                                dialog = builder.create();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (dialog != null)
                                dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MatchViewAdapter extends RecyclerView.ViewHolder {

        private CircleImageView matchimg;
        private TextView uploadDate,uploadTime,refId,slots,match_categories,matchDate,matchTime,room_id,room_password,matchMode,pricePool,entryfee;
//        private Button removeBtn,chooseSquadBtn;
        private CardView mTickets;

        public MatchViewAdapter(@NonNull View itemView) {
            super(itemView);
            matchimg = itemView.findViewById(R.id.matchimg);
            uploadDate = itemView.findViewById(R.id.uploadDate);
            uploadTime = itemView.findViewById(R.id.uploadTime);
            refId = itemView.findViewById(R.id.refId);
            slots = itemView.findViewById(R.id.slot);
            match_categories = itemView.findViewById(R.id.match_categories);
            matchDate = itemView.findViewById(R.id.matchDate);
            matchTime = itemView.findViewById(R.id.matchTime);
            room_id = itemView.findViewById(R.id.room_id);
            room_password = itemView.findViewById(R.id.room_password);
            matchMode = itemView.findViewById(R.id.matchMode);
            pricePool = itemView.findViewById(R.id.pricePool);
            entryfee = itemView.findViewById(R.id.entryfee);
            mTickets = itemView.findViewById(R.id.mTickets);

        }
    }
}
