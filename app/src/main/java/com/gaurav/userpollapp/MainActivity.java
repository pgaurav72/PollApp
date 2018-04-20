package com.gaurav.userpollapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * @author Gaurav
 */
public class MainActivity extends AppCompatActivity {

   RecyclerView pollList;
  private Query query;
  private String uid;
  private FirebaseAuth mAuth;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    uid = Objects.requireNonNull(currentUser).getUid();

    pollList = findViewById(R.id.poll_list_recycler_view);
    pollList.setHasFixedSize(true);
    pollList.setLayoutManager(new LinearLayoutManager(this));

    query = FirebaseDatabase.getInstance().getReference().child("users_polls");
  }

  public void toPollActivity(View view) {
    Intent toPollActivity = new Intent(getApplicationContext(), PollActivity.class);
    startActivity(toPollActivity);
  }

  @Override
  protected void onStart() {
    super.onStart();
    FirebaseRecyclerOptions<PollData> options =
            new FirebaseRecyclerOptions.Builder<PollData>()
            .setQuery(query,PollData.class)
            .build();

    FirebaseRecyclerAdapter filenameRecyclerAdapter =
            new FirebaseRecyclerAdapter<PollData, pollViewHolder> (options) {
              @Override
              protected void onBindViewHolder(pollViewHolder holder, int position, PollData model) {
                holder.setUser_image(getApplicationContext(), model.getUser_image());
                holder.setName_of_user(model.getName_of_user());
                holder.setPoll_name(model.getPoll_name());
                holder.setPoll_question(model.getPoll_question());

                final String post_key = getRef(position).getKey();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Intent to_poll_info_activity = new Intent(getApplicationContext(),PollInfoActivity.class);
                    to_poll_info_activity.putExtra("post_key", post_key);
                    startActivity(to_poll_info_activity);
                  }
                });

              }

              @NonNull
              @Override
              public pollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listofpoll, parent, false);
                return new pollViewHolder(view);
              }
            };
    try {
      filenameRecyclerAdapter.startListening();
      pollList.setAdapter(filenameRecyclerAdapter);
      filenameRecyclerAdapter.notifyDataSetChanged();
    }catch (Exception e){
      Toast.makeText(this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
    }
  }
public static class  pollViewHolder extends RecyclerView.ViewHolder {

  String user_image;
  String poll_question;
  String poll_name;
  String name_of_user;
  private ImageView pollProfilePic;
  private TextView polluserName;
  private TextView pollName;
  private TextView pollQuestion;

  public void setUser_image(Context context, String user_image) {
    this.user_image = user_image;
    Picasso.get().load(user_image).into(pollProfilePic);
  }

  public void setName_of_user(String name_of_user) {
    this.name_of_user = name_of_user;
    polluserName.setText(name_of_user);
  }

  public void setPoll_question(String poll_question) {
    this.poll_question = poll_question;
    pollQuestion.setText(poll_question);
  }

  public void setPoll_name(String poll_name) {
    this.poll_name = poll_name;
    pollName.setText(poll_name);
  }

  public pollViewHolder(View itemView) {

    super(itemView);
    pollProfilePic = itemView.findViewById(R.id.poll_profile_pic);
    polluserName = itemView.findViewById(R.id.poll_user_name);
    pollName = itemView.findViewById(R.id.poll_name);
    pollQuestion = itemView.findViewById(R.id.poll_question);

  }

}


  //------------------------------------- For menu ------------------------------------------------------


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu,menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {

    int id =  menuItem.getItemId();
    if ( id == R.id.sign_out_of_main_activity){
      FirebaseAuth.getInstance().signOut();
      Intent toLogInActivity = new Intent(getApplicationContext(),LogInActivity.class);
      startActivity(toLogInActivity);

    }

    return super.onOptionsItemSelected(menuItem);
  }



 /* @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();
    if (id == R.id.sign_out){
      FirebaseAuth.getInstance().signOut();
      Intent toLogIn = new Intent(getApplicationContext(),LogInActivity.class);
      startActivity(toLogIn);
    }

    return super.onOptionsItemSelected(item);
  } */
//--------------------------------------------------------------------------------------------------------------------


  //-------------------------------------------Exit on back start-------------------------------------
  private Boolean exit = false;
  @Override
  public void onBackPressed() {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
    System.exit(0);

  }
  //--------------------------------------- Exit on back end -----------------------------------------------
}
