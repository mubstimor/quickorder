package mubstimor.android.quickorder.di.main;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mubstimor.android.quickorder.di.ViewModelKey;
import mubstimor.android.quickorder.ui.main.orders.OrdersViewModel;
import mubstimor.android.quickorder.ui.main.orders.details.DetailsViewModel;
import mubstimor.android.quickorder.ui.main.profile.ProfileViewModel;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    public abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OrdersViewModel.class)
    public abstract ViewModel bindPostsViewModel(OrdersViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel.class)
    public abstract ViewModel bindDetailsViewModel(DetailsViewModel viewModel);
}
