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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoomListActivity extends Activity {

    private static final String TAG = "RoomListActivity";

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView;
    Adapter adapter = new Adapter();

    Button createRoomBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_list);

        recyclerView = (RecyclerView) findViewById(R.id.roomList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        createRoomBtn = (Button) findViewById(R.id.createRoomBtn);

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

                            temp.title = title;
                            temp.password = password;
                            temp.numberOfPlayer = "1";

                            adapter.roomList.add(temp);
                        } else {
                            Toast.makeText(getApplicationContext(), "방 제목을 입력하세요", Toast.LENGTH_SHORT).show();
                        }
                        //TODO 게임 대기실로 이동
                        dialog.cancel();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                adapter.notifyDataSetChanged();
            }
        });
    }
}
