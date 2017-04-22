package com.richardlucasapps.eaglescribe.screens.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.richardlucasapps.eaglescribe.R;
import com.richardlucasapps.eaglescribe.Utils;
import com.richardlucasapps.eaglescribe.screens.about.AboutActivity;
import com.richardlucasapps.eaglescribe.screens.base.CourseActivity;
import com.richardlucasapps.eaglescribe.screens.search.SearchActivity;
import java.util.ArrayList;
import javax.inject.Inject;

public class MainActivity extends CourseActivity<MainPresenter> implements MainView {

  @Inject MainPresenter presenter;

  @BindView(R.id.fab) FloatingActionButton fab;

  @BindView(R.id.main_view_empty_state) TextView emptyState;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.onCreate();
    fab.setOnClickListener(this);
  }

  @Override protected void inject() {
    getTypedApplication().getAppComponent().inject(this);
  }

  @Override protected int getActivityLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected void onResume() {
    super.onResume();
    presenter.onResume();
  }

  @Override protected String getScreenName() {
    return "Subscribed";
  }

  @NonNull @Override protected MainPresenter getPresenter() {
    return presenter;
  }

  @Override public void launchSearch() {
    SearchActivity.launch(this);
  }

  @Override public void clearCourseAdapter() {
    adapter.setList(new ArrayList<>());
  }

  @Override public void showMessage(String message) {
    new AlertDialog.Builder(this).setMessage(message)
        .setTitle(getString(R.string.eagle_scribe_status))
        .setPositiveButton(android.R.string.ok, null)
        .create()
        .show();
  }

  @Override public void onCourseUnsubscribe(int position) {
    removeUnsubscribedCourse(position);
  }

  @Override public void showEmptyState() {
    emptyState.setVisibility(View.VISIBLE);
  }

  @Override public void hideEmptyState() {
    emptyState.setVisibility(View.GONE);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id) {
      case R.id.action_about:
        AboutActivity.launch(this);
        return true;
      case R.id.action_rate:
        Utils.logRate(tracker);
        Utils.openEagleScribeInGooglePlay(this);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
