package com.isidroid.b21.di

//@InstallIn(SingletonComponent::class)
//@Module
//object NetworkModule {
//    private fun <T> httpClient(
//        cl: Class<T>,
//        logLevel: HttpLoggingInterceptor.Level,
//        authInterceptor: Interceptor?,
//        readTimeOut: Long = 15,
//        writeTimeOut: Long = 60
//    ): OkHttpClient {
//        val builder = OkHttpClient().newBuilder()
//            .readTimeout(readTimeOut, TimeUnit.SECONDS)
//            .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
//
//        authInterceptor?.let { builder.addInterceptor(it) }
//        builder.addInterceptor(logger(cl = cl, logLevel = logLevel))
//
//        return builder.build()
//    }
//
//    private fun <T> logger(cl: Class<T>, logLevel: HttpLoggingInterceptor.Level) =
//        HttpLoggingInterceptor { message -> Timber.tag(cl.simpleName).i(message) }
//            .apply { level = logLevel }
//
//    @ExperimentalSerializationApi
//    private fun <T> api(
//        baseUrl: String = "",
//        cl: Class<T>,
//        logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
//        authInterceptor: Interceptor? = null,
//        readTimeOut: Long = 15,
//        writeTimeOut: Long = 60
//    ): T {
//        val contentType = "application/json".toMediaType()
//
//        return Retrofit.Builder()
//            .client(
//                httpClient(
//                    cl = cl,
//                    logLevel = logLevel,
//                    authInterceptor = authInterceptor,
//                    readTimeOut = readTimeOut,
//                    writeTimeOut = writeTimeOut
//                )
//            )
//            .baseUrl(baseUrl)
//            .addConverterFactory(Json.asConverterFactory(contentType))
//            .build()
//            .create(cl) as T
//    }
//}