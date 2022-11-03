package com.example.hanabi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    public ArrayList<Room> roomList = new ArrayList<>();;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_view_item, parent,false);

        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.roomName.setText(room.title);
        holder.roomInfo.setText(room.numberOfPlayer);

        holder.enterRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(room.numberOfPlayer.equals("3") || room.isGameStart=="true")
                    return;
                Log.d("RoomListActivity", "click room" + holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
