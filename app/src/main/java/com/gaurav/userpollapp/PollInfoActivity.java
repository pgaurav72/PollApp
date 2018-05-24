package com.gaurav.userpollapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PollInfoActivity extends AppCompatActivity {

  private TextView pollInfoName;
  private TextView pollInfoQuestion;
  private TextView option_One;
  private TextView option_Two;
  private TextView option_Three;
  private TextView option_Four;
  private TextView totalVotesTextView;
  private TextView option_one_votes;
  private TextView option_two_votes;
  private TextView option_three_votes;
  private TextView option_four_votes;
  private FirebaseAuth mAuth;
  private DatabaseReference pollInfoRef;
  private DatabaseReference votedRef;
  private DatabaseReference keyRef;
  private DatabaseReference databaseRef;
  //private DatabaseReference totalVotes;
  //private DatabaseReference voteGiven;
  private DatabaseReference countRef;
  private DatabaseReference numberOFVotes;
   private DatabaseReference countOFVotes;
   private DatabaseReference oneVoteRef;
   private DatabaseReference twoVoteRef;
   private DatabaseReference threeVoteRef;
   private DatabaseReference fourVoteRef;
   private DatabaseReference totalVotes;

  private String post_key;
  private String uid;
  private static int voteOne = 0;
  private static int voteTwo = 0;
  private static int voteThree = 0;
  private static int voteFour = 0;

  private int clicked;
  private int totalVotesCount;
  private int row;
  private String pollName;
  private String vote_given = "given";

  public void forceCrash(View view) {
    throw new RuntimeException("This is a crash");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_poll_info);

    mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    uid = Objects.requireNonNull(currentUser).getUid();

    pollInfoName = findViewById(R.id.poll_info_name);
    pollInfoQuestion = findViewById(R.id.poll_info_question);
    option_One = findViewById(R.id.poll_info_op1);
    option_Two = findViewById(R.id.poll_info_op2);
    option_Three = findViewById(R.id.poll_info_op3);
    option_Four = findViewById(R.id.poll_info_op4);
    option_one_votes = findViewById(R.id.option_one_votes);
    option_two_votes = findViewById(R.id.option_two_votes);
    option_three_votes = findViewById(R.id.option_three_votes);
    option_four_votes = findViewById(R.id.option_four_votes);
    totalVotesTextView = findViewById(R.id.total_votes_text_view);


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

        // ----------------------------------- To check if vote given or not in shared preferences ----------------------------------------
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userID = pref.getString("user_id",null);
        String voteGive = pref.getString("vote_given","not_given");
        String pollname = pref.getString("poll_name","not_clicked");

        assert userID != null;
        try {
          if (userID.equals(uid) && voteGive.equals("given") && pollname.equals(pollName)) {
            option_One.setClickable(false);
            option_Two.setClickable(false);
            option_Three.setClickable(false);
            option_Four.setClickable(false);
          }else if (userID.equals(uid) && voteGive.equals("not_given") && pollname.equals("not_clicked")){
            option_One.setClickable(true);
            option_Two.setClickable(true);
            option_Three.setClickable(true);
            option_Four.setClickable(true);
          }
        }catch (Exception e){
          e.printStackTrace();
        }



        //--------------------------------------------------------------------------------------------------------------------------

        pollInfoName.setText(pollName);
        pollInfoQuestion.setText(pollQuestion);
        option_One.setText(optionOne);
        option_Two.setText(optionTwo);
        option_Three.setText(optionThree);
        option_Four.setText(optionFour);

        checkIfClicked();
        individualVotesCount();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Option_votes").child(pollName);

      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    countOFVotes = FirebaseDatabase.getInstance().getReference().child("individual_votes_count");


  }




  public void clickedOnText(int value) {

    int row = value;
    option_One.setClickable(false);
    option_Two.setClickable(false);
    option_Three.setClickable(false);
    option_Four.setClickable(false);
    voteGiven(row);
  }

  public void storeData(){
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString("user_id", uid);
    editor.putString("vote_given","given");
    editor.putString("poll_name",pollName);
    editor.apply();

  }


  //---------------------------------------- Option one clicked ----------------------------------------------------------------
  public void optionOneClicked(View view) {

//----------------------------------------
   storeData();
    clicked = 1;
    int one = 1;
    final DatabaseReference option = countOFVotes.child(pollName).child("option_one").push();
     keyRef.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
         option.setValue("For "+pollName+" first option is selected.");
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

       }
     });

    Toast.makeText(this, "Vote given!", Toast.LENGTH_SHORT).show();
    if (clicked == 1) {
      clickedOnText(one);
    }

  }

//------------------------------------------------------------------------------------------------------------------
  //------------------------------------------- Option two clicked --------------------------------------------------

  public void optionTwoClicked(View view) {

    storeData();
    //----To check how many times option two is selected for current poll----------
   final DatabaseReference option = countOFVotes.child(pollName).child("option_two").push();
    keyRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        option.setValue("For "+pollName+" second option is selected.");
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
    //-------------------------------------------------------------------------------
    clicked = 1;
    int two = 2;
    Toast.makeText(this, "Vote given!", Toast.LENGTH_SHORT).show();
    if (clicked == 1) {
      clickedOnText(two);
    }
  }

  //----------------------------------------------------------------------------------------------------------------
  //----------------------------------------- Option three  clicked -------------------------------------------------

  public void optionThreeClicked(View view) {

    storeData();
    //------------------------
    final DatabaseReference option = countOFVotes.child(pollName).child("option_three").push();
    keyRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        option.setValue("For "+pollName+" third option is selected.");
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
    //-----------------------------------------------------------------------------
    clicked = 1;
    int three = 3;
    Toast.makeText(this, "Vote given!", Toast.LENGTH_SHORT).show();
    if (clicked == 1) {
      clickedOnText(three);
    }
  }


  //---------------------------------------------------------------------------------------------------------------------------
  //--------------------------------------------------- option four clicked ----------------------------------------------

  public void optionFourClicked(View view) {

    storeData();
    //------------------------------
     final DatabaseReference option = countOFVotes.child(pollName).child("option_four").push();
    keyRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        option.setValue("For "+pollName+" fourth option is selected.");
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
    //-----------------------------------------------------------------------------------
    clicked = 1;
    int four = 4;
    Toast.makeText(this, "Vote given!", Toast.LENGTH_SHORT).show();
    if (clicked == 1) {
      clickedOnText(four);
    }

  }

  //-----------------------------------------------------------------------------------------------------------------------
//----------------------------- To send data when voted -----------------------------------------------------
  public void voteGiven(int whichClicked) {
    final int number = whichClicked;
    votedRef = FirebaseDatabase.getInstance().getReference().child("users_votes");
    final DatabaseReference sendData = votedRef.child(pollName).child(uid).push();
    pollInfoRef.child(post_key).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        sendData.child("voter_id").setValue(uid);
        sendData.child("voted").setValue(number);
        sendData.child("given_to_poll").setValue(pollName);
        sendData.child("vote_given").setValue(vote_given);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  //------------------------------------------------------------------------------------------------------------
  //----------------------------------- To check if the option is clicked once only ---------------------------
  public void checkIfClicked() {
    mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    uid = Objects.requireNonNull(currentUser).getUid();
    keyRef = FirebaseDatabase.getInstance().getReference().child("users_votes").child(pollName).child(uid);
    keyRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          //----------------------------------------- To remove last child added -------------------------------------------------------------
          int numberOfChilds = (int) dataSnapshot.getChildrenCount();
          if (numberOfChilds == 2){
           DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users_votes").child(pollName).child(uid).limitToLast(1).getRef();
           reference.removeValue();
          }

          //-----------------------------------------------------------------------------------------------------------------------------------
          String yes = (String) snapshot.child("vote_given").getValue();
          try {
            assert yes != null;
            if ("given".equals(yes)) {
              option_One.setClickable(false);
              option_Two.setClickable(false);
              option_Three.setClickable(false);
              option_Four.setClickable(false);
            } else {
              option_One.setClickable(true);
              option_Two.setClickable(true);
              option_Three.setClickable(true);
              option_Four.setClickable(true);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
    // ------------------------------------------- TO get total votes count --------------------------------------------------------

    countRef = FirebaseDatabase.getInstance().getReference().child("users_votes").child(pollName);
    countRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        int totalVotes = ((int) dataSnapshot.getChildrenCount());
        String votes = String.valueOf(totalVotes);
        totalVotesTextView.setText("Total votes: "+votes);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });


// -----------------------------------------------------------------------------------------------------------------------------------
  }

  //------------------------------------------- To get individual votes for options ---------------------------------------------------
  public void individualVotesCount() {

    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
    final SharedPreferences.Editor editor = pref.edit();

    String firstOption=pref.getString("option_one_votes",String.valueOf(0));
    option_one_votes.setText(firstOption);
    String secondOption=pref.getString("option_two_votes",String.valueOf(0));
    option_one_votes.setText(secondOption);
    String thirdOption=pref.getString("option_three_votes",String.valueOf(0));
    option_one_votes.setText(thirdOption);
    String fourthOption=pref.getString("option_four_votes",String.valueOf(0));
    option_one_votes.setText(fourthOption);

    oneVoteRef = FirebaseDatabase.getInstance().getReference().child("individual_votes_count").child(pollName);
    final DatabaseReference forOne = oneVoteRef.child("option_one");
    twoVoteRef = FirebaseDatabase.getInstance().getReference().child("individual_votes_count").child(pollName);
    final DatabaseReference forTwo = twoVoteRef.child("option_two");
    threeVoteRef = FirebaseDatabase.getInstance().getReference().child("individual_votes_count").child(pollName);
    final DatabaseReference forThree = threeVoteRef.child("option_three");
    fourVoteRef = FirebaseDatabase.getInstance().getReference().child("individual_votes_count").child(pollName);
    final DatabaseReference forFour = fourVoteRef.child("option_four");



    forOne.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        int one = (int) dataSnapshot.getChildrenCount();
        option_one_votes.setText(String.valueOf(one));
        editor.putString("option_one_votes", String.valueOf(one));
        editor.apply();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    forTwo.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        int two = (int) dataSnapshot.getChildrenCount();
        option_two_votes.setText(String.valueOf(two));
        editor.putString("option_two_votes", String.valueOf(two));
        editor.apply();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    forThree.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        int three = (int) dataSnapshot.getChildrenCount();
        option_three_votes.setText(String.valueOf(three));
        editor.putString("option_three_votes", String.valueOf(three));
        editor.apply();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    forFour.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        int four = (int) dataSnapshot.getChildrenCount();
        option_four_votes.setText(String.valueOf(four));
        editor.putString("option_four_votes", String.valueOf(four));
        editor.apply();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu tohome) {
    getMenuInflater().inflate(R.menu.tohome,tohome);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {

    int id =  menuItem.getItemId();
    if ( id == R.id.back_to_home){
      Intent toLogInActivity = new Intent(getApplicationContext(),MainActivity.class);
      startActivity(toLogInActivity);

    }
    return super.onOptionsItemSelected(menuItem);
  }




}


  //--------------------------------------------------------------------------------------------------------------------------------




