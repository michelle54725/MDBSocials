package com.mao.mp3_mdbsocials;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

// include date, desc of activity
// the “Interested” button should show up as being checked,
// and the number of interested people on the screen should increase.
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mPic;
    private TextView mName;
    private TextView mEmail;
    private TextView mCount;
    private TextView mDesc;
    private String mKey;
    private DatabaseReference mRef;
    private DatabaseReference mUserRef;
    private DatabaseReference mUser;
    private FirebaseUser user;
    private int interestCount;
    private boolean alreadyInterested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mName = findViewById(R.id.nameView);
        mEmail = findViewById(R.id.emailView);
        mCount = findViewById(R.id.interestedCount);
        mDesc = findViewById(R.id.descView);
        mPic = findViewById(R.id.picView);

        findViewById(R.id.backButton).setOnClickListener(this);
        findViewById(R.id.interestButton).setOnClickListener(this);

        // read from Database
        mKey = getIntent().getStringExtra("socialKey");
        mRef = FirebaseDatabase.getInstance().getReference("/Socials").child(mKey);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Social mSocial = dataSnapshot.getValue(Social.class);
                interestCount = mSocial.getInterested();
                mName.setText(mSocial.getName());
                mDesc.setText(mSocial.getDesc());
                mEmail.setText(mSocial.getEmail());
                mCount.setText(String.valueOf(interestCount));

                final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                mStorageRef.child(mKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        RequestOptions myOptions = new RequestOptions()
                                .error(R.drawable.ic_android_black_24dp)
                                .override(100, 100);
                        Glide.with(DetailActivity.this)
                                .load(url)
                                .apply(myOptions).into(mPic);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUserRef = FirebaseDatabase.getInstance().getReference("/Users");
        String k;
        if (user == null) k = "nullUser";
        else k = user.getEmail();

        //create Object
        ArrayList<String> interestedArr = new ArrayList<>();
        interestedArr.add("init");
        //store in Database
        mUser = mUserRef.child(k);
        mUser.setValue(interestedArr);
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(mKey)) {
                    alreadyInterested = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        switch (b.getId()) {
            case R.id.backButton:
                finish();
            case R.id.interestButton:
                // check if already interested
                if (alreadyInterested) {
                    Toast.makeText(DetailActivity.this, "You are already interested!", Toast.LENGTH_SHORT).show();
                    break;
                }
                // increment interest count in Social instance
                mRef.child("interested").setValue(interestCount++);
                // add Social's key to user's 'interested'
                mUser.child(mKey).setValue(mKey);
        }
    }
}
