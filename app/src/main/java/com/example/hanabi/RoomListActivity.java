package com.example.hanabi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;

public class RoomListActivity extends Activity {

    private static final String TAG = "RoomListActivity";

    DatabaseReference gameRef, indexRef, updateRef, userRef, cardRef;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView;
    Adapter adapter = new Adapter();

    ImageButton createRoomBtn;

    int index;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_list);

        Log.d("TAG", getApplicationContext().toString());

        recyclerView = (RecyclerView) findViewById(R.id.roomList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hanabi-ea7e9-default-rtdb.asia-southeast1.firebasedatabase.app/");

        createRoomBtn = (ImageButton) findViewById(R.id.createRoomBtn);

        String email = firebaseUser.getEmail();
        String[] split = email.split("@",2);
        id = split[0];

        indexRef = firebaseDatabase.getReference("Index").child("NewRoomIndex");
        indexRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                index = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateRef = firebaseDatabase.getReference("Room");

        updateRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Room room = snapshot.getValue(Room.class);
                if(room == null) {
                    Log.d("TAG","updateRef addListener roomIsNULL");
                    return;
                }
                if(room.isGameStart.equals("false"))
                {
                    room.roomNumber = snapshot.getKey();
                    adapter.roomList.add(room);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Room room = snapshot.getValue(Room.class);

                if(room.isGameStart.equals("true")) {
                    onChildRemoved(snapshot);
                }

                room.roomNumber = snapshot.getKey();
                for(int i =0; i<adapter.roomList.size();i++) {
                    if(adapter.roomList.get(i).roomNumber.equals(room.roomNumber)) {
                        adapter.roomList.set(i,room);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Room room = snapshot.getValue(Room.class);

                room.roomNumber = snapshot.getKey();
                for(int i = 0; i < adapter.roomList.size(); i++){
                    if (adapter.roomList.get(i).roomNumber.equals(room.roomNumber)){
                        adapter.roomList.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room temp = new Room();

                View dialogView = View.inflate(RoomListActivity.this, R.layout.dialog_make_room, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(RoomListActivity.this);

                EditText roomName = dialogView.findViewById(R.id.createRoom_roomName);
                EditText roomPassword = dialogView.findViewById(R.id.createRoom_roomPassword);
                ImageButton OkBtn = dialogView.findViewById(R.id.createRoom_OKBtn);

                dlg.setView(dialogView);
                Dialog dialog = dlg.create();

                OkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = roomName.getText().toString();
                        if(!(title.isEmpty() || title.equals(""))) {
                            String password = roomPassword.getText().toString();

                            Hashtable<String,String> newRoom = new Hashtable<>();

                            gameRef = updateRef.child(Integer.toString(index));

                            newRoom.put("numberOfPlayer", "1");
                            newRoom.put("title", title);
                            newRoom.put("password", password);
                            newRoom.put("roomMasterId", id);
                            newRoom.put("roomNumber", Integer.toString(index));
                            newRoom.put("isGameStart", "false");

                            Log.d(TAG,newRoom.toString());

                            gameRef.setValue(newRoom);

                            indexRef.setValue(++index);
                        } else {
                            Toast.makeText(getApplicationContext(), "방 제목을 입력하세요", Toast.LENGTH_SHORT).show();
                        }

                        userRef = gameRef.child("User");

                        Hashtable<String,String> newUser = new Hashtable<>();
                        newUser.put("p1", id);
                        newUser.put("p2", "대기중");
                        newUser.put("p3", "대기중");
                        newUser.put("p2Ready", "false");
                        newUser.put("p3Ready", "false");

                        userRef.setValue(newUser);

                        for(int i=0;i<50;i++) {
                            cardRef = gameRef.child("Board").child(Integer.toString(i));
                            Hashtable<String,String> newCard = new Hashtable<>();

                            String color;
                            if(i<10)
                                color = "red";
                            else if (i<20)
                                color = "blue";
                            else if (i<30)
                                color = "white";
                            else if (i<40)
                                color = "yellow";
                            else
                                color = "green";
                            newCard.put("color", color);

                            String number;
                            if(0 <= (i%10) && (i%10) <= 2)
                                number = "1";
                            else if (3 <= (i%10) && (i%10) <= 4)
                                number = "2";
                            else if (5 <= (i%10) && (i%10) <= 6)
                                number = "3";
                            else if (7 <= (i%10) && (i%10) <= 8)
                                number = "4";
                            else
                                number = "5";
                            newCard.put("number", number);

                            newCard.put("position", "deck");
                            newCard.put("handPosition", "");

                            cardRef.setValue(newCard);
                        }

                        // <-- 대기실로 이동
                        Intent intent = new Intent(getApplicationContext(), InGameActivity.class);
                        intent.putExtra("RoomNum", Integer.toString(index-1));
                        startActivity(intent);
                        // -->

                        dialog.cancel();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                adapter.notifyDataSetChanged();
            }
        });

        adapter.notifyDataSetChanged();
    }
}
