package mubstimor.android.quickorder.di.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mubstimor.android.quickorder.ui.main.posts.PostsFragment;
import mubstimor.android.quickorder.ui.main.profile.ProfileFragment;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract PostsFragment contributePostsFragment();
}
