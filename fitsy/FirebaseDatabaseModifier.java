package com.example.fitsy;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseModifier {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReferenceUser;
    private DatabaseReference mDatabase;
    private List<User> users = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<User> users, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseModifier() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mReferenceUser = firebaseDatabase.getReference("User");
    }
    public void readUsers(final DataStatus dataStatus){
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }
                dataStatus.DataIsLoaded(users,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void writeNewUser(String userId, String age, String weight, String height, String sex,
                             String name, String m_bicepcurl, String m_bench, String m_deadlift,
                             String t_freestyle, String t_butterfly, String t_backstroke, int fav_num) {
        User user = new User(age, weight,height, sex, name,m_bicepcurl,m_bench,m_deadlift,
                t_freestyle,t_butterfly,t_backstroke,0);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void updateFav(String userId, int fav_num){
        mDatabase.child("users").child(userId).child("fav_num").setValue(fav_num);
    }

}
