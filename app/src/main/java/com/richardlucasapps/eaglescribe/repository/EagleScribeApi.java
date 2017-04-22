package com.richardlucasapps.eaglescribe.repository;

import com.richardlucasapps.eaglescribe.model.response.Multiple;
import com.richardlucasapps.eaglescribe.model.response.Search;
import com.richardlucasapps.eaglescribe.model.response.Status;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface EagleScribeApi {
  // String url = BASE_URL + query;
  @GET("course/{query}") Observable<Search> search(@Path("query") String query);

  // https://api2.eaglescribe.com/multiple/CSCI110101,CSCI110102
  @GET("multiple/{codes}") Observable<Multiple> getMultiple(@Path("codes") String codes);

  @GET("https://eaglescribestatus.s3.amazonaws.com/status/{version}") Observable<Status> getStatus(
      @Path("version") String version);
}
