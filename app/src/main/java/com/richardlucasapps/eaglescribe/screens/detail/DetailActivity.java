package com.richardlucasapps.eaglescribe.screens.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import com.richardlucasapps.eaglescribe.R;
import com.richardlucasapps.eaglescribe.Utils;
import com.richardlucasapps.eaglescribe.Utils.ClassStatus;
import com.richardlucasapps.eaglescribe.model.Course;
import com.richardlucasapps.eaglescribe.screens.base.BaseActivity;
import java.util.List;
import javax.inject.Inject;

import static com.richardlucasapps.eaglescribe.Utils.CLASS_CANCELLED;
import static com.richardlucasapps.eaglescribe.Utils.CLASS_CLOSED_USER_NOT_SUBSCRIBED;
import static com.richardlucasapps.eaglescribe.Utils.CLASS_CLOSED_USER_SUBSCRIBED;
import static com.richardlucasapps.eaglescribe.Utils.CLASS_OPEN;
import static com.richardlucasapps.eaglescribe.Utils.CLASS_OPEN_USER_SUBSCRIBED;
import static com.richardlucasapps.eaglescribe.Utils.showMaxSubscriptionError;

public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailView {

  public static final String KEY_COURSE = "KEY_COURSE";

  public static void launch(Context context, Course course) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(KEY_COURSE, course);
    context.startActivity(intent);
  }

  @Inject DetailPresenter presenter;

  @BindView(R.id.imageView) ImageView imageView;

  @BindView(R.id.progress_subscribe) ProgressBar progressBar;

  @BindView(R.id.name) TextView name;

  @BindView(R.id.code) TextView code;

  @BindView(R.id.status) TextView status;

  @BindView(R.id.professor) TextView professor;

  @BindView(R.id.time) TextView time;

  @BindView(R.id.days) TextView days;

  @BindView(R.id.credit) TextView credit;

  @BindView(R.id.room) TextView room;

  @BindView(R.id.school) TextView school;

  @BindView(R.id.semester) TextView semester;

  private Course course;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    course = getIntent().getParcelableExtra(KEY_COURSE);

    setViews(course);
    setStatusDependentViews(course);
    setupListeners();

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override protected String getScreenName() {
    return "Detail";
  }

  private Course getCourse() {
    return course != null ? course : getIntent().getParcelableExtra(KEY_COURSE);
  }

  private void setupListeners() {
    imageView.setOnClickListener(view -> {
      presenter.subscribeClicked(getCourse());
    });
  }

  private void setStatusDependentViews(final Course course) {
    @ClassStatus final int status = Utils.getClassStatus(course);

    switch (status) {
      case CLASS_CANCELLED: {
        name.setTextColor(ContextCompat.getColor(this, R.color.color_closed));
        imageView.setVisibility(View.GONE);
        break;
      }
      case CLASS_OPEN: {
        name.setTextColor(ContextCompat.getColor(this, R.color.color_open));
        imageView.setVisibility(View.GONE);
        break;
      }
      case CLASS_OPEN_USER_SUBSCRIBED: {
        name.setTextColor(ContextCompat.getColor(this, R.color.color_open));
        imageView.setImageDrawable(
            ActivityCompat.getDrawable(this, R.drawable.ic_notifications_active_white_24dp));
        imageView.setVisibility(View.VISIBLE);
        break;
      }
      case CLASS_CLOSED_USER_NOT_SUBSCRIBED: {
        name.setTextColor(ContextCompat.getColor(this, R.color.color_closed));
        imageView.setImageDrawable(
            ActivityCompat.getDrawable(this, R.drawable.ic_notifications_none_white_24dp));
        imageView.setVisibility(View.VISIBLE);
        break;
      }
      case CLASS_CLOSED_USER_SUBSCRIBED: {
        name.setTextColor(ContextCompat.getColor(this, R.color.color_closed));
        imageView.setImageDrawable(
            ActivityCompat.getDrawable(this, R.drawable.ic_notifications_active_white_24dp));
        imageView.setVisibility(View.VISIBLE);
        break;
      }
    }
  }

  @Override protected void inject() {
    getTypedApplication().getAppComponent().inject(this);
  }

  @Override protected int getActivityLayoutId() {
    return R.layout.activity_detail;
  }

  @NonNull @Override protected DetailPresenter getPresenter() {
    return presenter;
  }

  @Override public void showLoading() {
    imageView.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideLoading() {
    progressBar.setVisibility(View.GONE);
    imageView.setVisibility(View.VISIBLE);
    setStatusDependentViews(getCourse());
  }

  public void setViews(Course course) {
    if (course != null) {
      name.setText(course.course);
      code.setText(course.code);
      setStatusString(course);
      professor.setText(course.professor);
      time.setText(course.time);
      setDayString(course);
      credit.setText(course.credit);
      room.setText(course.room);
      school.setText(course.school);
      semester.setText(course.semester);
    }
  }

  private void setDayString(Course course) {
    List<String> courseDays = course.day;

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < courseDays.size(); i++) {
      builder.append(courseDays.get(i));
      if (!(i == courseDays.size() - 1)) { //if not last day, append comma
        builder.append(",");
      }
      builder.append(" ");
    }
    days.setText(builder.toString());
  }

  private void setStatusString(Course course) {
    setStatusColor(course);
    if (course.isCancelled) {
      status.setText(getString(R.string.cancelled));
    } else if (course.closed) {
      status.setText(getString(R.string.closed));
    } else {
      status.setText(getString(R.string.open));
    }
  }

  private void setStatusColor(Course course) {
    if (course.closed || course.isCancelled) {
      status.setTextColor(ContextCompat.getColor(this, R.color.color_closed));
    } else {
      status.setTextColor(ContextCompat.getColor(this, R.color.color_open));
    }
  }

  @Override public void showMaxSubscriptionsError() {
    showMaxSubscriptionError(this, tracker);
  }

  @Override public void logSubscribe(final Course course) {
    Utils.logSubscribe(tracker, course);
  }

  @Override public void logUnsubscribe(Course course) {
    Utils.logUnsubscribe(tracker, course);
  }
}
