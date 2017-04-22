package com.richardlucasapps.eaglescribe.dagger;

import com.google.firebase.auth.FirebaseAuth;
import com.richardlucasapps.eaglescribe.repository.EagleScribeApi;
import com.richardlucasapps.eaglescribe.repository.NetworkRepository;
import com.richardlucasapps.eaglescribe.repository.Repository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class AppModule {

  private static final String BASE_URL = "https://api2.eaglescribe.com/";

  @Provides @Singleton Repository provideRepository(EagleScribeApi eagleScribeApi) {
    return new NetworkRepository(eagleScribeApi);
  }

  @Provides @Singleton EagleScribeApi provideEagleScribeApi(Retrofit retrofit) {
    return retrofit.create(EagleScribeApi.class);
  }

  @Provides @Singleton Retrofit provideRetrofit() {
    return new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build();
    /*
    Do .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
    to make the call happen on an io thread.
     */
  }

  @Provides @Singleton FirebaseAuth provideFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }
}

