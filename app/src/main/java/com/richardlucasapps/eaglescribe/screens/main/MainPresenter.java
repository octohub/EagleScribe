package com.richardlucasapps.eaglescribe.screens.main;

import com.google.firebase.crash.FirebaseCrash;
import com.richardlucasapps.eaglescribe.CoursesSingleton;
import com.richardlucasapps.eaglescribe.R;
import com.richardlucasapps.eaglescribe.model.response.Multiple;
import com.richardlucasapps.eaglescribe.model.response.Status;
import com.richardlucasapps.eaglescribe.repository.Repository;
import com.richardlucasapps.eaglescribe.screens.base.CoursePresenter;
import javax.inject.Inject;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter extends CoursePresenter<MainView> {

  private Repository repository;
  private static final String VERSION = "2.0";
  private static final String STATUS_PERFECT = "perfect";
  private static boolean MESSAGE_SHOWN = false;

  @Inject public MainPresenter(Repository repository) {
    this.repository = repository;
  }

  void onCreate() {
    refreshSubscribedUserCourses(this::onResume);
  }

  public void onResume() {
    final CoursesSingleton subscribedUserCoursesSingleton = CoursesSingleton.getInstance();
    final int size = subscribedUserCoursesSingleton.getSubscribedUserCourses().size();

    if (size > 0) {
      view.hideEmptyState();
      view.showLoading();
      repository.getMultiple(
          CoursesSingleton.getInstance().getCommaSeparatedSubscribedClassesCodes())
          .subscribeOn(Schedulers.newThread())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Observer<Multiple>() {
            @Override public void onCompleted() {
              view.hideLoading();
            }

            @Override public void onError(Throwable e) {
              view.hideLoading();
              FirebaseCrash.report(e);
            }

            @Override public void onNext(Multiple response) {
              view.setCourses(response.courses);
            }
          });
    } else {
      view.clearCourseAdapter(); // Confirm no left over courses are still displayed.
      view.showEmptyState();
    }

    if (!MESSAGE_SHOWN) {
      checkStatus();
    }
  }

  private void checkStatus() {
    repository.getStatus(VERSION)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Status>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            FirebaseCrash.report(e);
          }

          @Override public void onNext(Status response) {
            final String status = response.status;
            if (!status.equals(STATUS_PERFECT) && !MESSAGE_SHOWN) {
              view.showMessage(status);
              MESSAGE_SHOWN = true;
            }
          }
        });
  }

  @Override protected void onClick(int id) {
    switch (id) {
      case R.id.fab:
        view.launchSearch();
    }
  }
}
