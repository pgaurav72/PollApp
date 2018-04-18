package com.gaurav.userpollapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PollActivity extends AppCompatActivity {

  private EditText pollName, pollDesc, pollQuestion, optionOne, optionTwo, optionThree, optionFour;
  private Button postPollButton;
  private DatabaseReference pollReference;
  private FirebaseAuth mAuth;
  private String uid;

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

  }

  public void toMainActivity(View view) {
    String name = pollName.getText().toString().trim();
    String desc = pollDesc.getText().toString().trim();
    String question = pollQuestion.getText().toString().trim();
    String option1 = optionOne.getText().toString().trim();
    String option2 = optionTwo.getText().toString().trim();
    String option3 = optionThree.getText().toString().trim();
    String option4 = optionFour.getText().toString().trim();

    if (TextUtils.isEmpty(name) && TextUtils.isEmpty(desc) && TextUtils.isEmpty(question) && TextUtils.isEmpty(option1) && TextUtils.isEmpty(option2) && TextUtils.isEmpty(option3) && TextUtils.isEmpty(option4)){
      new AlertDialog.Builder(PollActivity.this)
              .setTitle("Enter all fields!")
              .setNeutralButton("Ok", null)
              .show();
    }
    else if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(question) && !TextUtils.isEmpty(option1) && !TextUtils.isEmpty(option2) && !TextUtils.isEmpty(option3) && !TextUtils.isEmpty(option4)
){
      pollReference = FirebaseDatabase.getInstance().getReference().child("users_polls").child(uid);
       final DatabaseReference newPost = pollReference.push();
      newPost.child("poll_name").setValue(name);
      newPost.child("poll_desc").setValue(desc);
      newPost.child("poll_question").setValue(question);
      newPost.child("option_one").setValue(option1);
      newPost.child("option_two").setValue(option2);
      newPost.child("option_three").setValue(option3);
      newPost.child("option_four").setValue(option4);
      newPost.child("uid_current_user").setValue(uid);
    }
  }
}
