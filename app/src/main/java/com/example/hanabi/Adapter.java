package com.example.hanabi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    public ArrayList<Room> roomList = new ArrayList<>();;

    Context parentContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        LayoutInflater inflater= (LayoutInflater) parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_view_item, parent,false);

        ViewHolder viewHolder = new ViewHolder(parentContext, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.roomName.setText(room.title);
        holder.roomInfo.setText(room.numberOfPlayer + " / 3");

        holder.enterRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(room.numberOfPlayer.equals("3") || room.isGameStart=="true")
                    return;

                Log.d("RoomListActivity", "click room" + holder.getAdapterPosition());

                Context context = view.getContext();

                View dialogView = View.inflate(context, R.layout.dialog_enter_room, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);

                final EditText enterRoomPassword = (EditText) dialogView.findViewById(R.id.enter_room_password);
                final ImageButton enterRoomBtn = (ImageButton) dialogView.findViewById(R.id.enter_room_btn);

                dlg.setView(dialogView);
                Dialog dialog = dlg.create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                enterRoomBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(enterRoomPassword.getText().toString().equals(room.password)) {
                            FirebaseDatabase firebaseDatabase;
                            DatabaseReference userRef, roomRef;
                            FirebaseUser firebaseUser;

                            firebaseDatabase = FirebaseDatabase.getInstance("https://hanabi-ea7e9-default-rtdb.asia-southeast1.firebasedatabase.app/");
                            roomRef = firebaseDatabase.getReference("Room").child(room.roomNumber);
                            userRef = roomRef.child("User");
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            String email = firebaseUser.getEmail();
                            String[] split = email.split("@",2);
                            String id = split[0];

                            if(room.numberOfPlayer.equals("1")) {
                                userRef.child("p2").setValue(id);
                                roomRef.child("numberOfPlayer").setValue("2");
                            }
                            else if (room.numberOfPlayer.equals("2")) {
                                userRef.child("p3").setValue(id);
                                roomRef.child("numberOfPlayer").setValue("3");
                            }

                            dialog.cancel();

                            //TODO 게임 대기실로 이동 TEST 필요!!!

                            Intent intent = new Intent(parentContext, InGameActivity.class);
                            intent.putExtra("RoomNum", room.roomNumber);
                            parentContext.startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
