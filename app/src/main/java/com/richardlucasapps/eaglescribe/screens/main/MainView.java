package com.richardlucasapps.eaglescribe.screens.main;

import com.richardlucasapps.eaglescribe.screens.base.CourseView;

public interface MainView extends CourseView {

  void launchSearch();

  void clearCourseAdapter();

  void showMessage(String message);
}
