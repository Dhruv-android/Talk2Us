package com.talk2us.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talk2us.models.Client;
import com.talk2us.models.Counsellor;
import com.talk2us.models.Message;
import com.talk2us.models.Session;

import java.util.ArrayList;

public class FirebaseUtils {
    private DatabaseReference databaseReference;
    private PrefManager prefManager;

    public static FirebaseUtils getInstance() {
        return new FirebaseUtils();
    }

    private FirebaseUtils() {
        prefManager = PrefManager.INSTANCE;
    }

    public void getSuitableCounsellor(final FirebaseStateListener<Counsellor> listener) {
        final ArrayList<Counsellor> counsellors = new ArrayList<>();
        final Counsellor[] mcounsellor = new Counsellor[1];
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COUNSELLOR);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Counsellor counsellor = s.getValue(Counsellor.class);
                    counsellors.add(counsellor);
                }
                for (Counsellor counsellor : Utils.Companion.sortList(counsellors)) {
                    if (counsellor != null && counsellor.available) {
                        mcounsellor[0] = counsellor;
                        break;
                    }
                }
                startSession(mcounsellor[0], listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startSession(final Counsellor counsellor, final FirebaseStateListener<Counsellor> listener) {
        prefManager.putString(Constants.COUNSELLOR_ID, counsellor.id);
        Session session = new Session(prefManager.getClientId(), prefManager.getClientMessageToken(), counsellor.id, counsellor.messageToken, prefManager.getChatId());
        FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(PrefManager.INSTANCE.getChatId()).setValue(session).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                PrefManager.INSTANCE.putCounsellorMessageToken(counsellor.messageToken);
                listener.onSuccess(counsellor);
            }
        });

    }

    public void sendMessage(final Message message, final FirebaseStateListener<Message> messageFirebaseStateListener) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("chatMessages");
        databaseReference.child(
                PrefManager.INSTANCE.getChatId()
        ).child(message.getTimeStamp()).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                messageFirebaseStateListener.onSuccess(message);
            }
        });
    }

    public void establishChat(final FirebaseStateListener<Boolean> listener) {
        databaseReference = FirebaseDatabase.getInstance().getReference("sessions");
        databaseReference.child(prefManager.
                getCounsellorId()).child(prefManager.getChatId()).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess(true);
            }
        });
    }

    public void setListeners() {
        FirebaseDatabase.getInstance().getReference(Constants.CLIENT)
                .child(PrefManager.INSTANCE.getClientId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Client client=dataSnapshot.getValue(Client.class);
                PrefManager.INSTANCE.putString(Constants.COUNSELLOR_ID,client.counsellorId);
                PrefManager.INSTANCE.putString(Constants.PHONE_NUMBER,client.phone);
                PrefManager.INSTANCE.putString(Constants.CLIENT_ID,client.clientId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    interface ChangeListener {
        void counsellorId(String str);
    }

    public interface FirebaseStateListener<T> {
        void onSuccess(T counsellor);

        void onError(DatabaseError e);
    }
}
