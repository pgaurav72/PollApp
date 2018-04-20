package com.gaurav.userpollapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.Objects;

public class PollActivity extends AppCompatActivity {

  private EditText pollName, pollDesc, pollQuestion, optionOne, optionTwo, optionThree, optionFour;
  private Button postPollButton;
  private DatabaseReference pollReference;
  private FirebaseAuth mAuth;
  private String uid;
  private DatabaseReference mDatabaseUser;
  private DatabaseReference userNameRef;
  private DatabaseReference privateVoteRef;
  Uri profileDownloadUri;
  private StorageReference storageReference;
  String nameofuser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_poll);

    pollName = findViewById(R.id.enter_poll_name);
    pollDesc = findViewById(R.id.enter_poll_desc);
    pollQuestion = findViewById(R.id.enter_poll_question);
    optionOne = findViewById(R.id.poll_option_one);
    optionTwo = findViewById(R.id.poll_option_two);
    optionThree = findViewById(R.id.poll_option_three);
    optionFour = findViewById(R.id.poll_option_four);
    postPollButton = findViewById(R.id.post_poll_button);

    mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    uid = Objects.requireNonNull(currentUser).getUid();

    mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
    storageReference = FirebaseStorage.getInstance().getReference();
//---------------------------------- To get the image of user from database -----------------------------------------------
    final StorageReference filepath = storageReference.child("users_profile").child(uid);
    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
      @Override
      public void onSuccess(Uri uri) {
       profileDownloadUri = uri;
      }
    });
//-------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------ To get the user name ----------------------------------------------------------
    userNameRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
    userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        nameofuser = dataSnapshot.child("name").getValue(String.class);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

  }




  public void toMainActivity(View view) {
    final String name = pollName.getText().toString().trim();
    final String desc = pollDesc.getText().toString().trim();
    final String question = pollQuestion.getText().toString().trim();
    final String option1 = optionOne.getText().toString().trim();
    final String option2 = optionTwo.getText().toString().trim();
    final String option3 = optionThree.getText().toString().trim();
    final String option4 = optionFour.getText().toString().trim();

    if (TextUtils.isEmpty(name)  && TextUtils.isEmpty(question) && TextUtils.isEmpty(option1) && TextUtils.isEmpty(option2) && TextUtils.isEmpty(option3) && TextUtils.isEmpty(option4)){
      new AlertDialog.Builder(PollActivity.this)
              .setTitle("Enter all fields!")
              .setNeutralButton("Ok", null)
              .show();
    }
    else if (!TextUtils.isEmpty(name)  && !TextUtils.isEmpty(question) && !TextUtils.isEmpty(option1) && !TextUtils.isEmpty(option2) && !TextUtils.isEmpty(option3) && !TextUtils.isEmpty(option4)
){
      pollReference = FirebaseDatabase.getInstance().getReference().child("users_polls");
      privateVoteRef = FirebaseDatabase.getInstance().getReference().child("private_votes").push();
       final DatabaseReference newPost = pollReference.push();
       final DatabaseReference privateVote = privateVoteRef.child(uid);

       mDatabaseUser.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
           newPost.child("poll_name").setValue(name);
           newPost.child("poll_desc").setValue(desc);
           newPost.child("poll_question").setValue(question);
           newPost.child("option_one").setValue(option1);
           newPost.child("option_two").setValue(option2);
           newPost.child("option_three").setValue(option3);
           newPost.child("option_four").setValue(option4);
           newPost.child("uid_current_user").setValue(uid);
           newPost.child("user_image").setValue(profileDownloadUri.toString());
           newPost.child("name_of_user").setValue(nameofuser);
           privateVote.child("poll_name").setValue(name);
           Intent toMainActivity = new Intent(getApplicationContext(),MainActivity.class);
           startActivity(toMainActivity);
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
       });
    }
  }
}
