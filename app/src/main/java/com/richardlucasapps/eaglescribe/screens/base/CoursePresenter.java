package com.richardlucasapps.eaglescribe.screens.base;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.richardlucasapps.eaglescribe.CoursesSingleton;
import com.richardlucasapps.eaglescribe.firebase.InstanceIdService;
import com.richardlucasapps.eaglescribe.model.Course;
import com.richardlucasapps.eaglescribe.model.UserCourse;

public class CoursePresenter<T extends CourseView> extends BasePresenter<T> {

  @Override protected void onClick(int id) {
  }

  public void clicked(Course course) {
    view.launchDetail(course);
  }

  public void subscribeClicked(Course course, int adapterPosition) {
    CoursesSingleton subscribedCourses = CoursesSingleton.getInstance();

    boolean isSubscribed = subscribedCourses.isSubscribed(course);
    boolean canSubscribeToMore = subscribedCourses.canSubscribeToMore();

    if (isSubscribed) {
      unsubscribeFromCourse(course, adapterPosition);
    } else if (canSubscribeToMore) {
      subscribeToCourse(course, adapterPosition);
    } else {
      view.showMaxSubscriptionsError();
    }
  }

  private void subscribeToCourse(final Course course, final int adapterPosition) {
    view.showLoading(adapterPosition);

    String token = InstanceIdService.getToken();

    if (getFirebaseAuth().getCurrentUser() != null && token != null) {
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference reference = database.getReference();

      FirebaseUser user = getFirebaseAuth().getCurrentUser();

      UserCourse userCourse = new UserCourse(course.code, user.getUid(), token);

      reference.child(TOP_LEVEL_CHILD)
          .child(user.getUid())
          .push()
          .setValue(userCourse, (databaseError, databaseReference) -> {
            view.logSubscribe(course);
            CoursesSingleton.getInstance().addSubscribedCourse(course, databaseReference.getKey());
            view.hideLoading(adapterPosition);
            refreshSubscribedUserCourses();
          });
    }
  }

  private void unsubscribeFromCourse(final Course course, final int adapterPosition) {
    view.showLoading(adapterPosition);

    String token = InstanceIdService.getToken();

    if (getFirebaseAuth().getCurrentUser() != null && token != null) {
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference reference = database.getReference();

      FirebaseUser user = getFirebaseAuth().getCurrentUser();

      String key = CoursesSingleton.getInstance().getFirebaseKeyFromCourse(course);

      if (key != null) {
        reference.child(TOP_LEVEL_CHILD)
            .child(user.getUid())
            .child(key)
            .removeValue((databaseError, databaseReference) -> {
              view.logUnsubscribe(course);
              CoursesSingleton.getInstance().removeSubscribedCourse(course);
              view.hideLoading(adapterPosition);
              refreshSubscribedUserCourses();
              view.onCourseUnsubscribe(adapterPosition);
            });
      } else {
        view.hideLoading(adapterPosition);
      }
    }
  }
}