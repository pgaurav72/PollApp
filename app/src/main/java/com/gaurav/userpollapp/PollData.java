package com.gaurav.userpollapp;

public class PollData {
  public PollData(String user_image, String poll_question, String poll_name) {
    this.user_image = user_image;
    this.poll_question = poll_question;
    this.poll_name = poll_name;
  }

  String user_image;
  String poll_question;
  String poll_name;

  public PollData(String name_of_user) {
    this.name_of_user = name_of_user;
  }

  public String getName_of_user() {
    return name_of_user;
  }

  public void setName_of_user(String name_of_user) {
    this.name_of_user = name_of_user;
  }

  String name_of_user;

  public String getUser_image() {
    return user_image;
  }

  public void setUser_image(String user_image) {
    this.user_image = user_image;
  }

  public String getPoll_question() {
    return poll_question;
  }

  public void setPoll_question(String poll_question) {
    this.poll_question = poll_question;
  }

  public String getPoll_name() {
    return poll_name;
  }

  public void setPoll_name(String poll_name) {
    this.poll_name = poll_name;
  }



  public PollData(){

  }



}
