package com.talk2us.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talk2us.R;
import com.talk2us.models.Counsellor;
import com.talk2us.models.Message;

import java.util.ArrayList;

public class FirebaseUtils {
    private DatabaseReference databaseReference;
    private PrefManager prefManager;
    public static FirebaseUtils getInstance(){
        return new FirebaseUtils();
    }
    private FirebaseUtils(){
        prefManager=PrefManager.INSTANCE;
    }

    public void getSuitableCounsellor(final FirebaseStateListener<Counsellor> listener){
        final ArrayList<Counsellor>counsellors=new ArrayList<>();
        final Counsellor[] mcounsellor = new Counsellor[1];
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Counsellor");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s:dataSnapshot.getChildren()){
                    Counsellor counsellor=s.getValue(Counsellor.class);
                    counsellors.add(counsellor);

                }
                for(Counsellor counsellor:Utils.Companion.sortList(counsellors)){
                    if(counsellor!=null && counsellor.available){
                        mcounsellor[0] =counsellor;
                    }
                }
                listener.onSuccess(mcounsellor[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public  void sendMessage(final Message message, final FirebaseStateListener<Message> messageFirebaseStateListener){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("chatMessages");
        databaseReference.child(
                PrefManager.INSTANCE.getChatId()
        ).child(message.getTimeStamp()).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                messageFirebaseStateListener.onSuccess(message);
            }
        });
    }

    public void establishChat(final FirebaseStateListener<Boolean> listener){
        databaseReference=FirebaseDatabase.getInstance().getReference("counsellorChats");
        databaseReference.child(prefManager.
                getCounsellorId()).child(prefManager.getChatId()).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess(true);
            }
        });
    }
    public interface FirebaseStateListener<T> {
        void onSuccess(T counsellor);

        void onError(DatabaseError e);
    }
}
