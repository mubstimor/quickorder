package mubstimor.android.quickorder.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mubstimor.android.quickorder.di.auth.AuthModule;
import mubstimor.android.quickorder.di.auth.AuthScope;
import mubstimor.android.quickorder.di.auth.AuthViewModelsModule;
import mubstimor.android.quickorder.ui.auth.AuthActivity;
import mubstimor.android.quickorder.ui.main.MainActivity;

@Module
public abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {AuthViewModelsModule.class, AuthModule.class}
    )
    abstract AuthActivity contributeAuthActivity();

//    @MainScope
//    @ContributesAndroidInjector(
//            modules = {MainFragmentBuildersModule.class, MainViewModelsModule.class, MainModule.class}
//    )
//    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}

