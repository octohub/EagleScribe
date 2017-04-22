package com.richardlucasapps.eaglescribe.screens.search;

import com.google.firebase.crash.FirebaseCrash;
import com.richardlucasapps.eaglescribe.model.response.Search;
import com.richardlucasapps.eaglescribe.repository.Repository;
import com.richardlucasapps.eaglescribe.screens.base.CoursePresenter;
import javax.inject.Inject;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchPresenter extends CoursePresenter<SearchView> {
  private Repository repository;

  @Inject public SearchPresenter(Repository repository) {
    this.repository = repository;
  }

  @Override protected void onClick(int id) {

  }

  public void searchClicked(String query) {
    if (view != null) {
      view.logSearch(query);
    }

    if ((query == null || query.length() < 2) && view != null) {
      view.showQueryTooShortMessage();
    } else {
      search(query);
    }
  }

  private void search(String query) {
    if (view != null) {
      view.showLoading();
    }
    repository.search(query)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Search>() {
          @Override public void onCompleted() {
            if (view != null) {
              view.hideLoading();
            }
          }

          @Override public void onError(Throwable e) {
            if (view != null) {
              view.hideLoading();
            }
            FirebaseCrash.report(e);
          }

          @Override public void onNext(Search response) {
            if (view != null) {
              view.hideLoading();
              view.setCourses(response.courses);
              if (response.courses.size() < 1) {
                view.showEmptyState();
              } else {
                view.hideEmptyState();
              }
            }
          }
        });
  }
}
