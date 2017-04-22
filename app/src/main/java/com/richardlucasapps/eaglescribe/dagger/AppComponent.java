package com.richardlucasapps.eaglescribe.dagger;

import com.richardlucasapps.eaglescribe.screens.about.AboutActivity;
import com.richardlucasapps.eaglescribe.screens.detail.DetailActivity;
import com.richardlucasapps.eaglescribe.screens.main.MainActivity;
import com.richardlucasapps.eaglescribe.screens.search.SearchActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class }) public interface AppComponent {
  void inject(MainActivity target);

  void inject(SearchActivity target);

  void inject(DetailActivity target);

  void inject(AboutActivity target);
}

