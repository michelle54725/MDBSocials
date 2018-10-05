package com.mao.mp3_mdbsocials;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

// TODO: Implement 'interested' button
// include date, desc of activity
// the “Interested” button should show up as being checked,
// and the number of interested people on the screen should increase.
public class DetailActivity extends AppCompatActivity {
    private ImageView mPic;
    private TextView mName;
    private TextView mEmail;
    private TextView mCount;
    private TextView mDesc;
    private CheckBox mInterested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mName = findViewById(R.id.nameView);
        mEmail = findViewById(R.id.emailView);
        mCount = findViewById(R.id.interestedCount);
        mDesc = findViewById(R.id.descView);

        mInterested = findViewById(R.id.interestButton);
        mPic = findViewById(R.id.picView);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // read from Database
        final String mKey = getIntent().getStringExtra("socialKey");
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("/Socials").child(mKey);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Social mSocial = dataSnapshot.getValue(Social.class);
                mName.setText(mSocial.getName());
                mDesc.setText(mSocial.getDesc());
                mEmail.setText(mSocial.getEmail());

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
    }
}
