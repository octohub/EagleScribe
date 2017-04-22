package com.richardlucasapps.eaglescribe;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.richardlucasapps.eaglescribe.model.Course;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Utils {

  public static void hideKeyboard(Activity activity) {
    InputMethodManager imm =
        (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    //Find the currently focused view, so we can grab the correct window token from it.
    View view = activity.getCurrentFocus();
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
      view = new View(activity);
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void openEagleScribeInGooglePlay(Context context) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    //Try Google play
    intent.setData(Uri.parse(context.getString(R.string.app_uri_for_google_play)));
    if (noActivityFoundForIntent(context, intent)) {
      Toast.makeText(context, context.getString(R.string.could_not_open_app_in_Google_play),
          Toast.LENGTH_SHORT).show();
    }
  }

  private static boolean noActivityFoundForIntent(Context context, Intent intent) {
    try {
      context.startActivity(intent);
      return false;
    } catch (ActivityNotFoundException e) {
      return true;
    }
  }

  public static void logSubscribe(final Tracker tracker, final Course course) {
    tracker.send(
        new HitBuilders.EventBuilder().setCategory("Subscribe").setAction(course.course).build());
  }

  public static void logUnsubscribe(final Tracker tracker, final Course course) {
    tracker.send(
        new HitBuilders.EventBuilder().setCategory("Unsubscribe").setAction(course.course).build());
  }

  public static void logRate(final Tracker tracker) {
    tracker.send(new HitBuilders.EventBuilder().setCategory("Rate EagleScribe").build());
  }

  public static void showMaxSubscriptionError(final Context context, final Tracker tracker) {
    Toast.makeText(context, R.string.maximum_classes, Toast.LENGTH_LONG).show();
    tracker.send(new HitBuilders.EventBuilder().setCategory("Max Subscriptions").build());
  }

    /*
  There are 5 view types.
  0. Class is open, don't show any notification asset next to class because we are not
  allowing users to subscribe to open classes.
  1. A class is closed and you have not yet subscribed.
  2. A class is closed and you have already subscribed.
  3. Class open and user subscribed.
  4. A class is cancelled.
   */

  @ClassStatus public static int getClassStatus(final Course course) {
    final CoursesSingleton subscribedUserCourses = CoursesSingleton.getInstance();

    if (course.isCancelled) {
      return CLASS_CANCELLED;
    }

    if (!course.closed && !subscribedUserCourses.isSubscribed(
        course)) { //class is open and user NOT subscribed
      return CLASS_OPEN;
    }

    //noinspection ConstantConditions
    if (course.closed && !subscribedUserCourses.isSubscribed(
        course)) { //class closed and user NOT subscribed
      return CLASS_CLOSED_USER_NOT_SUBSCRIBED;
    }

    //noinspection ConstantConditions,ConstantConditions,ConstantConditions
    if (course.closed && subscribedUserCourses.isSubscribed(
        course)) { //class closed and user IS subscribed
      return CLASS_CLOSED_USER_SUBSCRIBED;
    }

    if (!course.closed && subscribedUserCourses.isSubscribed(
        course)) { //class open and user IS subscribed
      return CLASS_OPEN_USER_SUBSCRIBED;
    }

    return CLASS_OPEN;
  }

  @Retention(SOURCE) @IntDef({
      CLASS_OPEN, CLASS_CLOSED_USER_NOT_SUBSCRIBED, CLASS_CLOSED_USER_SUBSCRIBED,
      CLASS_OPEN_USER_SUBSCRIBED, CLASS_CANCELLED
  }) public @interface ClassStatus {
  }

  public static final int CLASS_OPEN = 0;
  public static final int CLASS_CLOSED_USER_NOT_SUBSCRIBED = 1;
  public static final int CLASS_CLOSED_USER_SUBSCRIBED = 2;
  public static final int CLASS_OPEN_USER_SUBSCRIBED = 3;
  public static final int CLASS_CANCELLED = 4;
}
