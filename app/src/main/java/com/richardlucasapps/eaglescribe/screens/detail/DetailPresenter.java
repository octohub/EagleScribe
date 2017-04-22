package com.richardlucasapps.eaglescribe.screens.detail;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.richardlucasapps.eaglescribe.CoursesSingleton;
import com.richardlucasapps.eaglescribe.firebase.InstanceIdService;
import com.richardlucasapps.eaglescribe.model.Course;
import com.richardlucasapps.eaglescribe.model.UserCourse;
import com.richardlucasapps.eaglescribe.screens.base.BasePresenter;
import javax.inject.Inject;

public class DetailPresenter extends BasePresenter<DetailView> {
  @Override protected void onClick(int id) {

  }

  @Inject public DetailPresenter() {
  }

  public void subscribeClicked(Course course) {
    CoursesSingleton subscribedCourses = CoursesSingleton.getInstance();

    boolean isSubscribed = subscribedCourses.isSubscribed(course);
    boolean canSubscribeToMore = subscribedCourses.canSubscribeToMore();

    if (isSubscribed) {
      unsubscribeFromCourse(course);
    } else if (canSubscribeToMore) {
      subscribeToCourse(course);
    } else {
      view.showMaxSubscriptionsError();
    }
  }

  private void subscribeToCourse(final Course course) {
    view.showLoading();

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
            view.hideLoading();
            refreshSubscribedUserCourses();
          });
    }
  }

  private void unsubscribeFromCourse(final Course course) {
    view.showLoading();

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
              view.hideLoading();
              refreshSubscribedUserCourses();
            });
      } else {
        view.hideLoading();
      }
    }
  }
}