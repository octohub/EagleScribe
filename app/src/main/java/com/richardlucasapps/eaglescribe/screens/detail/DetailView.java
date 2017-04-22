package com.richardlucasapps.eaglescribe.screens.detail;

import com.richardlucasapps.eaglescribe.model.Course;
import com.richardlucasapps.eaglescribe.screens.base.BaseView;

public interface DetailView extends BaseView {
  void showMaxSubscriptionsError();

  void logSubscribe(Course course);

  void logUnsubscribe(Course course);
}
