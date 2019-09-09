package mubstimor.android.quickorder.di.main;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mubstimor.android.quickorder.di.ViewModelKey;
import mubstimor.android.quickorder.ui.main.orders.OrdersViewModel;
import mubstimor.android.quickorder.ui.main.orders.condiments.CondimentViewModel;
import mubstimor.android.quickorder.ui.main.orders.confirm.ConfirmViewModel;
import mubstimor.android.quickorder.ui.main.orders.details.DetailsViewModel;
import mubstimor.android.quickorder.ui.main.orders.menu.SelectMenuViewModel;
import mubstimor.android.quickorder.ui.main.orders.neworder.SelectTableViewModel;
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

    @Binds
    @IntoMap
    @ViewModelKey(SelectTableViewModel.class)
    public abstract ViewModel bindTablesViewModel(SelectTableViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectMenuViewModel.class)
    public abstract ViewModel bindMenuViewModel(SelectMenuViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CondimentViewModel.class)
    public abstract ViewModel bindCondimentViewModel(CondimentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ConfirmViewModel.class)
    public abstract ViewModel bindConfirmViewModel(ConfirmViewModel viewModel);
}
