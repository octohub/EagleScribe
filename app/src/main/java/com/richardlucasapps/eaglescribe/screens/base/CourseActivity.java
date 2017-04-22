package com.richardlucasapps.eaglescribe.screens.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.richardlucasapps.eaglescribe.CourseAdapter;
import com.richardlucasapps.eaglescribe.Divider;
import com.richardlucasapps.eaglescribe.R;
import com.richardlucasapps.eaglescribe.Utils;
import com.richardlucasapps.eaglescribe.model.Course;
import com.richardlucasapps.eaglescribe.screens.detail.DetailActivity;
import java.util.List;

import static com.richardlucasapps.eaglescribe.Utils.showMaxSubscriptionError;

public abstract class CourseActivity<T extends CoursePresenter> extends BaseActivity<T>
    implements CourseView {

  private static final String KEY_COURSES = "KEY_COURSES";
  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.progressbar) View progressBar;
  protected CourseAdapter adapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupAdapter(savedInstanceState);
    setupRecyclerView();
  }

  @Override protected void onResume() {
    super.onResume();
    /**
     * Call notifyDataSetChanged to handle the case where a user subscribes to a course in the
     * {@link DetailActivity} and the views need to be updated in the course list.
     */
    adapter.notifyDataSetChanged();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putParcelableArrayList(KEY_COURSES, adapter.getCourses());
    super.onSaveInstanceState(outState);
  }

  private void setupAdapter(Bundle savedInstanceState) {
    if (adapter == null) {
      adapter = new CourseAdapter(getPresenter());
    }
    if (savedInstanceState != null) {
      setCourses(savedInstanceState.getParcelableArrayList(KEY_COURSES));
    }
  }

  @Override public void setCourses(List<Course> courses) {
    if (adapter != null) {
      adapter.setList(courses);
    }
  }

  private void setupRecyclerView() {
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.addItemDecoration(new Divider(this));
    recyclerView.setAdapter(adapter);
  }

  protected void removeUnsubscribedCourse(int position) {
    adapter.removeCourse(position);
    if (adapter.getItemCount() < 1) {
      showEmptyState();
    } else {
      hideEmptyState();
    }
  }

  @Override public void logSubscribe(final Course course) {
    Utils.logSubscribe(tracker, course);
  }

  @Override public void logUnsubscribe(Course course) {
    Utils.logUnsubscribe(tracker, course);
  }

  @Override public void launchDetail(Course course) {
    DetailActivity.launch(this, course);
  }

  @Override public void showLoading() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideLoading() {
    progressBar.setVisibility(View.GONE);
  }

  @Override public void showLoading(int adapterPosition) {
    adapter.showLoading(adapterPosition);
  }

  @Override public void hideLoading(int adapterPosition) {
    adapter.hideLoading(adapterPosition);
  }

  @Override public void showMaxSubscriptionsError() {
    showMaxSubscriptionError(this, tracker);
  }
}