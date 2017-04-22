package com.richardlucasapps.eaglescribe.screens.about;

import com.richardlucasapps.eaglescribe.R;
import com.richardlucasapps.eaglescribe.screens.base.BasePresenter;
import javax.inject.Inject;

public class AboutPresenter extends BasePresenter<AboutView> {
  @Inject public AboutPresenter() {
  }

  @Override protected void onClick(int id) {
    switch (id) {
      case R.id.button_rate:
        view.openGooglePlay();
        break;
    }
  }
}
