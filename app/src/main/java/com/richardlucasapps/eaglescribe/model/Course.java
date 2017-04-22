package com.richardlucasapps.eaglescribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Course implements Parcelable {

  public String course;
  public String code;
  public String school;
  public String semester;
  public String professor;
  public String credit;
  public List<String> day;
  public String time;
  public String room;
  public boolean closed;
  public boolean isCancelled;

  //region Parcelable

  public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
    @Override public Course createFromParcel(Parcel source) {
      return new Course(source);
    }

    @Override public Course[] newArray(int size) {
      return new Course[size];
    }
  };

  protected Course(Parcel in) {
    this.course = in.readString();
    this.code = in.readString();
    this.school = in.readString();
    this.semester = in.readString();
    this.professor = in.readString();
    this.credit = in.readString();
    this.day = in.createStringArrayList();
    this.time = in.readString();
    this.room = in.readString();
    this.closed = in.readByte() != 0;
    this.isCancelled = in.readByte() != 0;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.course);
    dest.writeString(this.code);
    dest.writeString(this.school);
    dest.writeString(this.semester);
    dest.writeString(this.professor);
    dest.writeString(this.credit);
    dest.writeStringList(this.day);
    dest.writeString(this.time);
    dest.writeString(this.room);
    dest.writeByte(this.closed ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isCancelled ? (byte) 1 : (byte) 0);
  }
  //endregion Parcelable
}
