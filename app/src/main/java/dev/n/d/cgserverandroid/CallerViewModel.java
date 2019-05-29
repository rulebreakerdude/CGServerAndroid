package dev.n.d.cgserverandroid;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class CallerViewModel extends AndroidViewModel {

    private CallerRepository mRepository;

    private LiveData<List<Caller>> mAllCallers;

    public CallerViewModel (Application application) {
        super(application);
        mRepository = new CallerRepository(application);
        mAllCallers = mRepository.getAllCallers();
    }

    LiveData<List<Caller>> getAllCallers() { return mAllCallers; }

    public void insert(Caller caller) { mRepository.insert(caller); }
    public void deleteAll() { mRepository.deleteAll(); }
    public void deleteAnEntry(String callerToBeDeleted) { mRepository.deleteAnEntry(callerToBeDeleted); }
    public List<Caller> getAllTraineesAsList(){ return mRepository.getAllCallersAsList(); }
    public void updateApiDatetime(String primaryKey, String datetime) { mRepository.updateApiDatetime(primaryKey, datetime); }
    public void updateSuccessfulCallbackDatetime(String primaryKey, String datetime) { mRepository.updateSuccessfulCallbackDatetime(primaryKey, datetime); }
    public void updateResponseCGNet(String primaryKey, String response) { mRepository.updateResponseCGNet(primaryKey, response); }
    public void updateResponseIMI(String primaryKey, String response) { mRepository.updateResponseIMI(primaryKey, response); }
    public void callFromCGNet(String primaryKey) { mRepository.callFromCGNet(primaryKey); }
    public void callFromIMI(String primaryKey) { mRepository.callFromIMI(primaryKey); }

}
