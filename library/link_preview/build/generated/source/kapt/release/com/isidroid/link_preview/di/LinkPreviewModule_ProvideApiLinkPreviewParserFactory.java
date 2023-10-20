package com.isidroid.link_preview.di;

import com.isidroid.link_preview.data.source.network.ApiLinkPreviewParser;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class LinkPreviewModule_ProvideApiLinkPreviewParserFactory implements Factory<ApiLinkPreviewParser> {
  @Override
  public ApiLinkPreviewParser get() {
    return provideApiLinkPreviewParser();
  }

  public static LinkPreviewModule_ProvideApiLinkPreviewParserFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ApiLinkPreviewParser provideApiLinkPreviewParser() {
    return Preconditions.checkNotNullFromProvides(LinkPreviewModule.INSTANCE.provideApiLinkPreviewParser());
  }

  private static final class InstanceHolder {
    private static final LinkPreviewModule_ProvideApiLinkPreviewParserFactory INSTANCE = new LinkPreviewModule_ProvideApiLinkPreviewParserFactory();
  }
}
