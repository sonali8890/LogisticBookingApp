package com.sonali.portertestapp.di.component;

import com.sonali.portertestapp.di.module.NetworkModule;
import com.sonali.portertestapp.domain.repository.CommonRepositoryImpl;
import com.sonali.portertestapp.domain.repository.GoogleRepositoryImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sonali
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {

    void inject(GoogleRepositoryImpl commonRepo);
    void inject(CommonRepositoryImpl commonRepo);
}
