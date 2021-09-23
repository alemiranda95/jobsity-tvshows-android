package com.jobsity.tvshows.api.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class NetworkBuilder {
    private static final String BASE_URL = "http://api.tvmaze.com";
    private static OkHttpClient.Builder httpClient;

    private NetworkBuilder() {}

    public static <T> T getInstance(Class<T> clazz) {
        initializeRestClient();

        RxErrorHandlingCallAdapterFactory rxAdapter =
                (RxErrorHandlingCallAdapterFactory)
                        RxErrorHandlingCallAdapterFactory.createWithScheduler(Schedulers.io());

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .client(httpClient.build())
                .build();

        return retrofit.create(clazz);
    }

    private static void initializeRestClient() {
        if (httpClient == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().
                    setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // logging interceptor
                    .connectTimeout(180, TimeUnit.SECONDS) // connect timeout
                    .readTimeout(180, TimeUnit.SECONDS);  // socket timeout
        }
    }
}
