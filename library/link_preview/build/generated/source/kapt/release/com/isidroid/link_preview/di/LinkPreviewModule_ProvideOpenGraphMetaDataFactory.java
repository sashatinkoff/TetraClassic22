package com.isidroid.link_preview.di;

import com.isidroid.link_preview.data.source.network.ApiLinkPreviewParser;
import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
    "KotlinInternalInJava"
})
public final class LinkPreviewModule_ProvideOpenGraphMetaDataFactory implements Factory<OpenGraphMetaDataRepository> {
  private final Provider<ApiLinkPreviewParser> apiProvider;

  public LinkPreviewModule_ProvideOpenGraphMetaDataFactory(
      Provider<ApiLinkPreviewParser> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public OpenGraphMetaDataRepository get() {
    return provideOpenGraphMetaData(apiProvider.get());
  }

  public static LinkPreviewModule_ProvideOpenGraphMetaDataFactory create(
      Provider<ApiLinkPreviewParser> apiProvider) {
    return new LinkPreviewModule_ProvideOpenGraphMetaDataFactory(apiProvider);
  }

  public static OpenGraphMetaDataRepository provideOpenGraphMetaData(ApiLinkPreviewParser api) {
    return Preconditions.checkNotNullFromProvides(LinkPreviewModule.INSTANCE.provideOpenGraphMetaData(api));
  }
}
