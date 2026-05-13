package com.dragonball.app.viewmodel;

import com.dragonball.app.data.repository.DragonBallRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class PlanetDetailViewModel_Factory implements Factory<PlanetDetailViewModel> {
  private final Provider<DragonBallRepository> repositoryProvider;

  public PlanetDetailViewModel_Factory(Provider<DragonBallRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public PlanetDetailViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static PlanetDetailViewModel_Factory create(
      Provider<DragonBallRepository> repositoryProvider) {
    return new PlanetDetailViewModel_Factory(repositoryProvider);
  }

  public static PlanetDetailViewModel newInstance(DragonBallRepository repository) {
    return new PlanetDetailViewModel(repository);
  }
}
