package com.dragonball.app.di;

import com.dragonball.app.data.remote.DragonBallApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideDragonBallApiServiceFactory implements Factory<DragonBallApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideDragonBallApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public DragonBallApiService get() {
    return provideDragonBallApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideDragonBallApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideDragonBallApiServiceFactory(retrofitProvider);
  }

  public static DragonBallApiService provideDragonBallApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideDragonBallApiService(retrofit));
  }
}
