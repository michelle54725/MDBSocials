package com.mao.mp3_mdbsocials;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEvent extends AppCompatActivity {
    EditText name;
    EditText desc;
    EditText date;
    ImageView pic;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        name = findViewById(R.id.nameField);
        desc = findViewById(R.id.descField);
        date = findViewById(R.id.dateField);
        pic = findViewById(R.id.pictureField);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        } else {
            email = "N/A";
        }

        findViewById(R.id.uploadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                final Uri file = data.getData();

                //put pic in storage first, then put into database
                //get unique ID
                final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("/Socials");
                final String mKey = myRef.push().getKey(); //create empty node to get key of it

                final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference socialsRef = mStorageRef.child(mKey);

                socialsRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override

                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //make Social object to store in myRef
                                Social mSocial = new Social(
                                        name.getText().toString(),
                                        desc.getText().toString(),
                                        getDate(date.getText().toString()),
                                        mKey, email);
                                //store in Database
                                myRef.child(mKey).setValue(mSocial);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
            }
        }
    }

    public String getDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); // Make sure user insert date into edittext in this format.
        Date dateObject = null;
        try {
            dateObject = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormatISO8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormatISO8601.format(dateObject);
    }

}
