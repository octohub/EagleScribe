package com.richardlucasapps.eaglescribe.screens.base;

import com.richardlucasapps.eaglescribe.model.Course;
import java.util.List;

public interface CourseView extends BaseView {
  void setCourses(List<Course> courses);

  void showLoading(int position);

  void hideLoading(int position);

  void showMaxSubscriptionsError();

  void launchDetail(Course course);

  void onCourseUnsubscribe(int position);

  void showEmptyState();

  void hideEmptyState();

  void logSubscribe(Course course);

  void logUnsubscribe(Course course);
}
