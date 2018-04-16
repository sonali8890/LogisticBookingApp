package com.sonali.portertestapp.di.component;

import com.sonali.portertestapp.di.module.RepositoryModule;
import com.sonali.portertestapp.view.home.HomePresentoImpl;
import com.sonali.portertestapp.view.locationsearch.LocationSearchActivity;
import com.sonali.portertestapp.view.locationsearch.LocationSearchPresentoImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sonali
 */

@Singleton
@Component(modules = {RepositoryModule.class})
public interface RepositoryComponent {

    void inject(HomePresentoImpl commonRepo);
    void inject(LocationSearchPresentoImpl commonRepo);
    void inject(LocationSearchActivity manager);
}
