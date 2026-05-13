package com.dragonball.app.data.repository;

import com.dragonball.app.data.remote.DragonBallApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DragonBallRepository_Factory implements Factory<DragonBallRepository> {
  private final Provider<DragonBallApiService> apiServiceProvider;

  public DragonBallRepository_Factory(Provider<DragonBallApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public DragonBallRepository get() {
    return newInstance(apiServiceProvider.get());
  }

  public static DragonBallRepository_Factory create(
      Provider<DragonBallApiService> apiServiceProvider) {
    return new DragonBallRepository_Factory(apiServiceProvider);
  }

  public static DragonBallRepository newInstance(DragonBallApiService apiService) {
    return new DragonBallRepository(apiService);
  }
}
