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
public final class CharacterDetailViewModel_Factory implements Factory<CharacterDetailViewModel> {
  private final Provider<DragonBallRepository> repositoryProvider;

  public CharacterDetailViewModel_Factory(Provider<DragonBallRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CharacterDetailViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CharacterDetailViewModel_Factory create(
      Provider<DragonBallRepository> repositoryProvider) {
    return new CharacterDetailViewModel_Factory(repositoryProvider);
  }

  public static CharacterDetailViewModel newInstance(DragonBallRepository repository) {
    return new CharacterDetailViewModel(repository);
  }
}
