package com.talk2us.ui.chat;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.talk2us.R;
import com.talk2us.models.Message;
import com.talk2us.utils.Constants;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    List<Message> mMessage;
    Context context;
    final public static int SEND=0;
    final public static int RECEIVED=1;
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
        View v;
        if(viewType==SEND) {
             v=LayoutInflater.from(context).inflate(R.layout.item_chat_send,parent,false);
        }
        else{
            v=LayoutInflater.from(context).inflate(R.layout.item_chat_recieved,parent,false);
        }
        return new ChatHolder(v,viewType);
    }

    @Override
    public int getItemViewType(int position) {

        if (mMessage.get(position).getSentFrom().equals(Constants.CLIENT)){
            return SEND;
        }else {
            return RECEIVED;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
       holder.bindView(mMessage.get(position));
    }

    @Override
    public int getItemCount() {
         if (mMessage != null)
            return mMessage.size();
        else return 0;
    }

    public class ChatHolder extends RecyclerView.ViewHolder{
        TextView msg;
        ImageView send;
        TextView timeStamp;
        int viewType;
        public ChatHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            msg = itemView.findViewById(R.id.chat_message);
            timeStamp=itemView.findViewById(R.id.timestamp);
            send = itemView.findViewById(R.id.sent_status);
            this.viewType=viewType;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void bindView(Message message){
            msg.setText(message.getWord());
            timeStamp.setText(message.getTimeStamp());
            if(viewType==SEND){
                if(message.getSent()){
                    send.setImageDrawable(context.getDrawable(R.drawable.ic_check));
                }
                else{
                    send.setImageDrawable(context.getDrawable(R.drawable.ic_clock));
                }
            }
        }
    }
}
