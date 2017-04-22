package com.richardlucasapps.eaglescribe.screens.search;

import com.richardlucasapps.eaglescribe.screens.base.CourseView;

public interface SearchView extends CourseView {
  void showQueryTooShortMessage();

  void logSearch(String query);
}
