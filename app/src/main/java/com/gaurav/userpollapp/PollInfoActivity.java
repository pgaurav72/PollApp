package com.gaurav.userpollapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PollInfoActivity extends AppCompatActivity {

  private TextView pollInfoName;
  private TextView pollInfoQuestion;
  private  TextView option_One;
  private TextView option_Two;
  private TextView option_Three;
  private TextView option_Four;
  private FirebaseAuth mAuth;
  private DatabaseReference pollInfoRef;
  private DatabaseReference votedRef;
  //private DatabaseReference totalVotes;
  //private DatabaseReference voteGiven;
  private DatabaseReference checkRef;
  private String post_key;
  private String uid;
  private int voteOne = 0;
  private int voteTwo = 0;
  private int voteThree = 0;
  private int voteFour = 0;
  private int first;
  private int second;
  private int third;
  private int fourth;
  private int clicked;
  private int totalVotesCount;
  private int row;
  String pollName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_poll_info);

    mAuth = FirebaseAuth.getInstance();
    pollInfoName = findViewById(R.id.poll_info_name);
    pollInfoQuestion = findViewById(R.id.poll_info_question);
    option_One = findViewById(R.id.poll_info_op1);
    option_Two = findViewById(R.id.poll_info_op2);
    option_Three = findViewById(R.id.poll_info_op3);
    option_Four = findViewById(R.id.poll_info_op4);

    mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    uid = Objects.requireNonNull(currentUser).getUid();

    post_key = getIntent().getStringExtra("post_key");

    pollInfoRef = FirebaseDatabase.getInstance().getReference().child("users_polls");
    pollInfoRef.child(post_key).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        pollName = (String) dataSnapshot.child("poll_name").getValue();
        String pollQuestion = (String) dataSnapshot.child("poll_question").getValue();
        String optionOne = (String) dataSnapshot.child("option_one").getValue();
        String optionTwo = (String) dataSnapshot.child("option_two").getValue();
        String optionThree = (String) dataSnapshot.child("option_three").getValue();
        String optionFour = (String) dataSnapshot.child("option_four").getValue();

        pollInfoName.setText(pollName);
        pollInfoQuestion.setText(pollQuestion);
        option_One.setText(optionOne);
        option_Two.setText(optionTwo);
        option_Three.setText(optionThree);
        option_Four.setText(optionFour);


      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(PollInfoActivity.this, "Error: ", Toast.LENGTH_SHORT).show();
      }
    });


  }

  public void clickedOnText (int value){
    int row = value;
    option_One.setClickable(false);
    option_Two.setClickable(false);
    option_Three.setClickable(false);
    option_Four.setClickable(false);
    voteGiven(row);

  }



  public void  optionOneClicked(View view){
    voteOne ++;
    first = voteOne;
     clicked  = 1;
     int one = 1;
    Toast.makeText(this, "Vote given!", Toast.LENGTH_SHORT).show();
    if (clicked ==1){
      clickedOnText(one);
    }

  }



  public void optionTwoClicked(View view){
    voteTwo++;
    second = voteTwo;
    clicked  = 1;
    int two = 2;
    Toast.makeText(this, "Vote given!", Toast.LENGTH_SHORT).show();
    if (clicked == 1){
      clickedOnText(two);
    }

  }
  public void optionThreeClicked(View view){
    voteThree++;
    third = voteThree;
    clicked  = 1;
    int three = 3;
    Toast.makeText(this, "Vote given!", Toast.LENGTH_SHORT).show();
    if (clicked == 1){
      clickedOnText(three);
    }

  }
  public void optionFourClicked(View view){
    voteFour++;
    fourth = voteFour;
    clicked  = 1;
    int four = 4;
    Toast.makeText(this, "Vote given!", Toast.LENGTH_SHORT).show();
    if (clicked == 1){
      clickedOnText(four);
    }

  }

//----------------------------- To send data when voted -----------------------------------------------------
  public void  voteGiven(int whichClicked){
    final int number = whichClicked;
    votedRef = FirebaseDatabase.getInstance().getReference().child("users_votes").child(uid);
    //totalVotes = FirebaseDatabase.getInstance().getReference().child("total_votes");
    final DatabaseReference sendData = votedRef.push();
    pollInfoRef.child(post_key).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        sendData.child("voter_id").setValue(uid);
        sendData.child("voted").setValue(number);
        sendData.child("given_to_poll").setValue(pollName);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });



  }
//------------------------------------------------------------------------------------------------------------
}
