package com.richardlucasapps.eaglescribe.screens.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.auth.FirebaseAuth;
import com.richardlucasapps.eaglescribe.EagleScribeApplication;
import com.richardlucasapps.eaglescribe.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity
    implements BaseView, View.OnClickListener {

  /**
   * ButterKnife-bound Views
   */

  @Nullable @BindView(R.id.toolbar) protected Toolbar toolbar;

  protected Tracker tracker;

  @Override public EagleScribeApplication getTypedApplication() {
    return (EagleScribeApplication) getApplication();
  }

  /**
   * Requests an injection of the Activity's dependencies (usually, a
   * Presenter) then does the usual Activity setup (superclass implementation,
   * setContentView, setup of transitions, etc.)
   */
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    // Crucial that this happens before superclass is called.
    inject();
    super.onCreate(savedInstanceState);

    setContentView(getActivityLayoutId());

    ButterKnife.bind(this);

    setupAppbar();

    getPresenter().onCreate(this);

    signIn();

    EagleScribeApplication application = (EagleScribeApplication) getApplication();
    tracker = application.getDefaultTracker();
  }

  private void signIn() {
    final FirebaseAuth firebaseAuth = getPresenter().getFirebaseAuth();
    if (firebaseAuth.getCurrentUser() == null) {
      firebaseAuth.signInAnonymously().addOnCompleteListener(this, task -> {
        // If sign in fails, display a message to the user. If sign in succeeds
        // the auth state listener will be notified and logic to handle the
        // signed in user can be handled in the listener.
        if (!task.isSuccessful()) {
          Toast.makeText(this, R.string.sign_in_fail, Toast.LENGTH_LONG).show();
        } else {
          getPresenter().refreshSubscribedUserCourses();
        }
      });
    }
  }

  @Override protected void onResume() {
    super.onResume();
    tracker.setScreenName(getScreenName());
  }

  protected abstract String getScreenName();

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return (super.onOptionsItemSelected(item));
  }

  /**
   * Must be overridden to request the activity's dependencies.
   */
  protected abstract void inject();

  /**
   * Called by onCreate() to determine what layout should be displayed.
   *
   * @return The id of the layout to display for this Activity.
   */
  protected abstract int getActivityLayoutId();

  /**
   * Set up the toolbar for this screen. Override if a screen needs more
   * specific toolbar functionality; it's not necessary to call this implementation.
   */
  protected void setupAppbar() {
    if (toolbar == null) {
      throw new NullPointerException("You must declare a toolbar in your Activity's layout "
          + "or override the setupAppbar() method.");
    }
    setSupportActionBar(toolbar);
  }

  /**
   * Get a reference to the presenter for this screen.
   *
   * @return This screen's presenter
   */
  @NonNull protected abstract T getPresenter();

  @Override protected void attachBaseContext(Context newBase) {
    // Initialize Calligraphy
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  /**
   * OnClickListener
   */

  @Override public void onClick(View clicked) {
    getPresenter().onClick(clicked.getId());
  }
}
