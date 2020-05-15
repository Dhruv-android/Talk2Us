package com.talk2us.ui.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.talk2us.R;
import com.talk2us.models.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    List<Message> mMessage;
    Context context;
    public ChatAdapter(Context context){
        this.context=context;
    }
    public void update(List<Message> message){
        this.mMessage=message;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_chat_send,parent,false);
        return new ChatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        Message current=mMessage.get(position);
        holder.msg.setText(current.getWord());
        Log.d("hello",position+"  "+current.toString());
        if(current.getSent()){
            holder.send.setText("Send");
        }
        else{
            holder.send.setText("Not Send");
        }
    }

    @Override
    public int getItemCount() {
         if (mMessage != null)
            return mMessage.size();
        else return 0;
    }

    public class ChatHolder extends RecyclerView.ViewHolder{
        TextView msg;
        TextView send;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.chatMessage);
            send=itemView.findViewById(R.id.send_status);

        }
    }
}
