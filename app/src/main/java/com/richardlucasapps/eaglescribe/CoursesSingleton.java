package com.richardlucasapps.eaglescribe;

import android.support.annotation.Nullable;
import com.richardlucasapps.eaglescribe.model.Course;
import com.richardlucasapps.eaglescribe.model.UserCourse;
import java.util.ArrayList;
import java.util.List;

public class CoursesSingleton {
  private static CoursesSingleton mInstance;
  private static List<UserCourse> subscribedUserCourses;

  private CoursesSingleton() {
    subscribedUserCourses = new ArrayList<>();
  }

  public static synchronized CoursesSingleton getInstance() {
    if (mInstance == null) {
      mInstance = new CoursesSingleton();
    }
    return mInstance;
  }

  public boolean isSubscribed(Course course) {
    for (UserCourse userCourse : subscribedUserCourses) {
      if (userCourse.code.equals(course.code)) {
        return true;
      }
    }
    return false;
  }

  public List<UserCourse> getSubscribedUserCourses() {
    return subscribedUserCourses;
  }

  public void setSubscribedUserCourses(List<UserCourse> subscribedUserCourses) {
    CoursesSingleton.subscribedUserCourses.clear();
    CoursesSingleton.subscribedUserCourses.addAll(subscribedUserCourses);
  }

  /**
   * Adds a course to the list of subscribed courses. This method should only be used to add a
   * course after a request to subscribe to a new course has been completed. This way, the UI can
   * reflect to the user right away that their subscription request has completed.
   */
  public void addSubscribedCourse(Course course, String firebaseKey) {
    UserCourse userCourse = new UserCourse();
    userCourse.code = course.code;
    userCourse.firebaseKey = firebaseKey;
    CoursesSingleton.subscribedUserCourses.add(userCourse);
  }

  /**
   * Removes a course from the list of subscribed courses. This method should only be used to
   * remove
   * a
   * course after a request to unsubscribe from a course has been completed. This way, the UI can
   * reflect to the user right away that their request to unsubscribe has been completed.
   */
  public void removeSubscribedCourse(Course course) {
    for (UserCourse userCourse : subscribedUserCourses) {
      if (userCourse.code.equals(course.code)) {
        subscribedUserCourses.remove(userCourse);
        break;
      }
    }
  }

  public boolean canSubscribeToMore() {
    return subscribedUserCourses.size() < 3;
  }

  public String getCommaSeparatedSubscribedClassesCodes() {
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < subscribedUserCourses.size(); i++) {
      stringBuilder.append(subscribedUserCourses.get(i).code);
      if (i != subscribedUserCourses.size() - 1) { //not last element
        stringBuilder.append(",");
      }
    }
    return stringBuilder.toString();
  }

  @Nullable public String getFirebaseKeyFromCourse(Course course) {
    for (UserCourse userCourse : subscribedUserCourses) {
      if (userCourse.code.equals(course.code)) {
        return userCourse.firebaseKey;
      }
    }
    return null;
  }
}
