package mubstimor.android.quickorder.ui.main.orders.neworder;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import mubstimor.android.quickorder.SessionManager;
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.models.Table;
import mubstimor.android.quickorder.network.main.MainApi;
import mubstimor.android.quickorder.ui.main.Resource;

public class SelectTableViewModel extends ViewModel {

    private static final String TAG = "SelectTableViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<Table>>> tables;

    @Inject
    public SelectTableViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "OrdersViewModel: viewmodel is working ...");
    }

    public LiveData<Resource<List<Table>>> observePosts(){
        if(tables == null){
            tables = new MediatorLiveData<>();
            tables.setValue(Resource.loading((List<Table>)null));

//            String username = sessionManager.getAuthUser().getValue().data.getUsername();

            final LiveData<Resource<List<Table>>> source = LiveDataReactiveStreams.fromPublisher(
                  mainApi.getTables()
                    .onErrorReturn(new Function<Throwable, List<Table>>() {
                        @Override
                        public List<Table> apply(Throwable throwable) throws Exception {
                            Log.e(TAG, "apply: ", throwable);
                            Table table = new Table();
                            table.setTableId(-1);
                            ArrayList<Table> tables = new ArrayList<>();
                            tables.add(table);
                            return tables;
                        }
                    })
                    .map(new Function<List<Table>, Resource<List<Table>>>() {
                        @Override
                        public Resource<List<Table>> apply(List<Table> tables) throws Exception {
                            if(tables.size() > 0){
                                if(tables.get(0).getTableId() == -1){
                                    return Resource.error("Something went wrong", null);
                                }
                            }
                            return Resource.success(tables);
                        }
                    })
                    .subscribeOn(Schedulers.io())
            );
            tables.addSource(source, new Observer<Resource<List<Table>>>() {
                @Override
                public void onChanged(Resource<List<Table>> listResource) {
                    tables.setValue(listResource);
                    tables.removeSource(source);
                }
            });
        }
        return tables;
    }

}
