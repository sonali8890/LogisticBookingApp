package com.sonali.portertestapp;

import android.app.Application;

import com.sonali.portertestapp.di.component.DaggerNetworkComponent;
import com.sonali.portertestapp.di.component.DaggerRepositoryComponent;
import com.sonali.portertestapp.di.component.NetworkComponent;
import com.sonali.portertestapp.di.component.RepositoryComponent;
import com.sonali.portertestapp.di.module.NetworkModule;
import com.sonali.portertestapp.di.module.RepositoryModule;

/**
 * Created by Sonali
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private NetworkComponent mNetworkComponent;
    private RepositoryComponent mRepositoryComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        mNetworkComponent = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule())
                .build();
        mRepositoryComponent = DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public static MyApplication getApplicationInstance(){
        return mInstance;
    }

    public NetworkComponent getmNetworkComponent() {
        return mNetworkComponent;
    }

    public RepositoryComponent getmRepositoryComponent() {
        return mRepositoryComponent;
    }
}
