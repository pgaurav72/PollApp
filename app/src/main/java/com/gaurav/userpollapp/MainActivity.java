package com.gaurav.userpollapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void toPollActivity(View view) {
    Intent toPollActivity = new Intent(getApplicationContext(), PollActivity.class);
    startActivity(toPollActivity);
  }
}