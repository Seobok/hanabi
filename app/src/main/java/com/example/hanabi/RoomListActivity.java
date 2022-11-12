package com.example.hanabi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    DatabaseReference gameRef, indexRef, updateRef, userRef;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView;
    Adapter adapter = new Adapter();

    Button createRoomBtn;

    int index;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_list);

        recyclerView = (RecyclerView) findViewById(R.id.roomList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hanabi-ea7e9-default-rtdb.asia-southeast1.firebasedatabase.app/");

        createRoomBtn = (Button) findViewById(R.id.createRoomBtn);

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
                room.roomNumber = snapshot.getKey();
                adapter.roomList.add(room);
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
                Button OkBtn = dialogView.findViewById(R.id.createRoom_OKBtn);

                dlg.setView(dialogView);
                Dialog dialog = dlg.create();

                OkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = roomName.getText().toString();
                        if(!(title.isEmpty() || title.equals(""))) {
                            String password = roomPassword.getText().toString();

                            gameRef = firebaseDatabase.getReference("Room").child(Integer.toString(index));

                            Hashtable<String,String> newRoom = new Hashtable<>();

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

                        //TODO 게임 대기실로 이동
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
