package mubstimor.android.quickorder;

import android.content.Context;

import javax.inject.Inject;

import dagger.Binds;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import mubstimor.android.quickorder.di.DaggerAppComponent;
import mubstimor.android.quickorder.util.Constants;
import mubstimor.android.quickorder.util.PreferencesManager;

public class BaseApplication extends DaggerApplication {

    PreferencesManager preferencesManager;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        preferencesManager = new PreferencesManager(this);
        return DaggerAppComponent.builder().application(this).application(preferencesManager.getValue(Constants.KEY_USERTOKEN)).build();
    }

}