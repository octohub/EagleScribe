package com.richardlucasapps.eaglescribe.screens.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.richardlucasapps.eaglescribe.CoursesSingleton;
import com.richardlucasapps.eaglescribe.model.UserCourse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public abstract class BasePresenter<T extends BaseView> {

  protected static final String TOP_LEVEL_CHILD = "UserCourses";

  protected T view;

  private FirebaseAuth firebaseAuth;

  protected void onCreate(T view) {
    setView(view);
  }

  @Inject public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
    this.firebaseAuth = firebaseAuth;
  }

  protected void onDestroy() {
    clearView();
  }

  protected void onPause() {

  }

  protected void onResume() {

  }

  /**
   * Update this presenter's reference to the view.
   */
  protected void setView(@NonNull T view) {
    this.view = view;
  }

  /**
   * Clear this presenter's reference to the view, for example because it is being
   * destroyed.
   */
  protected void clearView() {
    this.view = null;
  }

  /**
   * Handle click events.
   */
  protected abstract void onClick(int id);

  protected FirebaseAuth getFirebaseAuth() {
    return firebaseAuth;
  }

  protected void refreshSubscribedUserCourses() {
    refreshSubscribedUserCourses(null);
  }

  public void refreshSubscribedUserCourses(@Nullable Runnable onDataChangedCompletion) {
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    if (firebaseAuth.getCurrentUser() != null) {

      ValueEventListener userCoursesListener = new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot dataSnapshot) {
          // Get Post object and use the values to update the UI
          List<UserCourse> userCourses = new ArrayList<>();

          for (DataSnapshot userCourseSnapshot : dataSnapshot.getChildren()) {
            UserCourse userCourse = userCourseSnapshot.getValue(UserCourse.class);
            userCourse.firebaseKey = userCourseSnapshot.getKey();
            userCourses.add(userCourse);
          }

          CoursesSingleton.getInstance().setSubscribedUserCourses(userCourses);

          if (onDataChangedCompletion != null) {
            onDataChangedCompletion.run();
          }
        }

        @Override public void onCancelled(DatabaseError databaseError) {

        }
      };

      reference.child(TOP_LEVEL_CHILD)
          .child(firebaseAuth.getCurrentUser().getUid())
          .addListenerForSingleValueEvent(userCoursesListener);
    }
  }
}
