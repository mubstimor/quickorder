package mubstimor.android.quickorder.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import mubstimor.android.quickorder.BaseApplication;
import mubstimor.android.quickorder.SessionManager;
import mubstimor.android.quickorder.util.PreferencesManager;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    SessionManager sessionManager();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder application(String authToken);

        AppComponent build();
    }
}