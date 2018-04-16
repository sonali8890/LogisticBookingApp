package com.sonali.portertestapp.di.module;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sonali.portertestapp.BuildConfig;
import com.sonali.portertestapp.MyApplication;
import com.sonali.portertestapp.domain.NoInternetConnectionException;
import com.sonali.portertestapp.domain.URLConstants;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sonali
 */
@Module
public class NetworkModule {

    @Provides
    public Interceptor provideHeaderInterceptor() {

        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .build();

                if (isNetworkConnected()) {
                    return chain.proceed(request);
                } else {
                    throw new NoInternetConnectionException();
                }
            }
        };

    }

    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Interceptor headerInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor);

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }


    @Provides
    @Singleton
    @Named("BaseUrl")
    Retrofit provideBaseRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(URLConstants.API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    @Named("GoogleLocation")
    Retrofit provideGoogleLocationBaseRetrofit(OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(URLConstants.GOOGLE_LOCATION_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    @Named("GooglePlaces")
    Retrofit provideGooglePlacesBaseRetrofit(OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(URLConstants.GOOGLE_PLACE_API)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) MyApplication.getApplicationInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
