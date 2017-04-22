package com.richardlucasapps.eaglescribe.repository;

import com.richardlucasapps.eaglescribe.model.response.Multiple;
import com.richardlucasapps.eaglescribe.model.response.Search;
import com.richardlucasapps.eaglescribe.model.response.Status;
import rx.Observable;

public interface Repository {

  Observable<Search> search(String query);

  Observable<Multiple> getMultiple(String codes);

  Observable<Status> getStatus(String version);
}
