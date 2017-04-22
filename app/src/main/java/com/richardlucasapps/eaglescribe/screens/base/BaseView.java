package com.richardlucasapps.eaglescribe.screens.base;

import com.richardlucasapps.eaglescribe.EagleScribeApplication;

public interface BaseView {
  void showLoading();

  void hideLoading();

  EagleScribeApplication getTypedApplication();
}
