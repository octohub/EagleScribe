package com.richardlucasapps.eaglescribe.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserCourse {
  public String code;
  public String uid;
  public String pushToken;
  public String time;
  public boolean hasBeenNotified;
  public String firebaseKey;

  public UserCourse() {
    // Default constructor required for calls to DataSnapshot.getValue(User.class)
  }

  public UserCourse(String code, String uid, String pushToken) {
    this.code = code;
    this.uid = uid;
    this.pushToken = pushToken;

    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
    df.setTimeZone(tz);
    String nowAsISO = df.format(new Date());

    this.time = nowAsISO;
    this.hasBeenNotified = false;
  }
}
