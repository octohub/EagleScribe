package com.richardlucasapps.eaglescribe.repository;

import com.richardlucasapps.eaglescribe.model.response.Multiple;
import com.richardlucasapps.eaglescribe.model.response.Search;
import com.richardlucasapps.eaglescribe.model.response.Status;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * An implementation of the data-access repository that access the L.L. Bean API over the network.
 */
@Singleton public class NetworkRepository implements Repository {
  private EagleScribeApi api;

  @Inject public NetworkRepository(EagleScribeApi api) {
    this.api = api;
  }

  @Override public Observable<Search> search(String query) {
    return api.search(query);
  }

  @Override public Observable<Multiple> getMultiple(String codes) {
    return api.getMultiple(codes);
  }

  @Override public Observable<Status> getStatus(String version) {
    return api.getStatus(version);
  }
}
