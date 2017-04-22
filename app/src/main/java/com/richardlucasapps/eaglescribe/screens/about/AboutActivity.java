package com.richardlucasapps.eaglescribe.screens.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import com.richardlucasapps.eaglescribe.R;
import com.richardlucasapps.eaglescribe.Utils;
import com.richardlucasapps.eaglescribe.screens.base.BaseActivity;
import javax.inject.Inject;

public class AboutActivity extends BaseActivity<AboutPresenter> implements AboutView {

  public static void launch(Context context) {
    Intent intent = new Intent(context, AboutActivity.class);
    context.startActivity(intent);
  }

  @Inject AboutPresenter presenter;

  @BindView(R.id.button_rate) Button buttonRate;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    setupTwitterTextViews((TextView) findViewById(R.id.activity_about_twitter_richardLucas));
    setupTwitterTextViews((TextView) findViewById(R.id.activity_about_twitter_kevinSullivan));
    setupTwitterTextViews((TextView) findViewById(R.id.activity_about_twitter_adamNelsen));

    buttonRate.setOnClickListener(this);
  }

  @Override protected String getScreenName() {
    return "About";
  }

  @Override protected void inject() {
    getTypedApplication().getAppComponent().inject(this);
  }

  @Override protected int getActivityLayoutId() {
    return R.layout.activity_about;
  }

  @NonNull @Override protected AboutPresenter getPresenter() {
    return presenter;
  }

  @Override public void showLoading() {

  }

  @Override public void hideLoading() {

  }

  private void setupTwitterTextViews(TextView textView) {
    textView.setMovementMethod(LinkMovementMethod.getInstance());
  }

  @Override public void openGooglePlay() {
    Utils.logRate(tracker);
    Utils.openEagleScribeInGooglePlay(this);
  }
}
