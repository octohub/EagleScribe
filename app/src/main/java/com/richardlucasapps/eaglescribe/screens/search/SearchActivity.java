package com.richardlucasapps.eaglescribe.screens.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.richardlucasapps.eaglescribe.R;
import com.richardlucasapps.eaglescribe.screens.base.CourseActivity;
import javax.inject.Inject;

public class SearchActivity extends CourseActivity<SearchPresenter> implements SearchView {

  @Inject SearchPresenter presenter;

  @BindView(R.id.searchField) EditText searchField;

  @BindView(R.id.search_view_empty_state) TextView emptyState;

  public static void launch(Context context) {
    Intent intent = new Intent(context, SearchActivity.class);
    context.startActivity(intent);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();
    setupEditText();
  }

  @Override protected String getScreenName() {
    return "Search";
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  @Override protected void inject() {
    getTypedApplication().getAppComponent().inject(this);
  }

  @Override protected int getActivityLayoutId() {
    return R.layout.activity_search;
  }

  @NonNull @Override protected SearchPresenter getPresenter() {
    return presenter;
  }

  private void setupEditText() {
    searchField.setOnEditorActionListener((view, actionId, event) -> {
      presenter.searchClicked(view.getText().toString());
      return true;
    });
  }

  @Override public void showQueryTooShortMessage() {
    Toast.makeText(this, getString(R.string.search_toast), Toast.LENGTH_SHORT).show();
  }

  @Override public void logSearch(String query) {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
    tracker.send(new HitBuilders.EventBuilder().setCategory("Search").setAction(query).build());
  }

  @Override public void onCourseUnsubscribe(int position) {
    // no op. When a course is unsubscribed from the SearchView it is not removed, unlike in the MainView.
  }

  @Override public void showEmptyState() {
    emptyState.setVisibility(View.VISIBLE);
  }

  @Override public void hideEmptyState() {
    emptyState.setVisibility(View.GONE);
  }
}
