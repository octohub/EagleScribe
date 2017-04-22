package com.richardlucasapps.eaglescribe;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.richardlucasapps.eaglescribe.dagger.AppComponent;
import com.richardlucasapps.eaglescribe.dagger.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class EagleScribeApplication extends Application {
  private AppComponent component;
  private Tracker mTracker;

  @Override public void onCreate() {
    super.onCreate();

    // Initialize LeakCanary and Timber
    if (BuildConfig.DEBUG) {
      LeakCanary.install(this);
      Timber.plant(new Timber.DebugTree());
    }

    // Initialize Dagger
    component = DaggerAppComponent.builder().build();

    // Initialize Calligraphy
    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Roboto-Regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build());
  }

  public AppComponent getAppComponent() {
    return component;
  }

  /**
   * Gets the default {@link Tracker} for this {@link Application}.
   *
   * @return tracker
   */
  synchronized public Tracker getDefaultTracker() {
    if (mTracker == null) {
      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      mTracker = analytics.newTracker(R.xml.global_tracker);
    }
    return mTracker;
  }
}
