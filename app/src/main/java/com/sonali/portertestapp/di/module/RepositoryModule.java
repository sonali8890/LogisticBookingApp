package com.sonali.portertestapp.di.module;


import com.sonali.portertestapp.domain.repository.CommonRepositoryImpl;
import com.sonali.portertestapp.domain.repository.GoogleRepositoryImpl;
import com.sonali.portertestapp.utility.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sonali
 */

@Singleton
@Module
public class RepositoryModule {

    @Provides
    public GoogleRepositoryImpl provideGoogleRepository(){
        return new GoogleRepositoryImpl();
    }


    @Provides
    public CommonRepositoryImpl provideCommonRepo(){
        return new CommonRepositoryImpl();
    }

    @Singleton
    @Provides
    public PreferenceManager providePreference(){
        PreferenceManager perf = new PreferenceManager();
        perf.init();
        return perf;
    }

}
