package com.example.hanabi;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView roomName, roomInfo;
    public Button enterRoomBtn;

    ViewHolder(Context context, View itemView) {
        super(itemView);

        roomName = itemView.findViewById(R.id.roomName);
        roomInfo = itemView.findViewById(R.id.roomInfo);
        enterRoomBtn = itemView.findViewById(R.id.enterRoomBtn);
    }
}
