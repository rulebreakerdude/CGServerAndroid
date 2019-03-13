package dev.n.d.cgserverandroid;

import android.app.Application;
import android.os.AsyncTask;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class CallerRepository {

    private CallerDao mCallerDao;
    private LiveData<List<Caller>> mAllCallers;
    private List<Caller> mCallerList;

    CallerRepository(Application application) {
        CallerDatabase db = CallerDatabase.getDatabase(application);
        mCallerDao = db.callerDao();
        mAllCallers = mCallerDao.getAllCallers();
    }

    LiveData<List<Caller>> getAllCallers() {
        return mAllCallers;
    }

    //**************************************************************************
    //Async starts here post live data
    List<Caller> getAllCallersAsList(){
        new getAllCallersAsListAsyncTask(mCallerDao).execute();
        return mCallerList;
    }

    private class getAllCallersAsListAsyncTask extends AsyncTask<Void, Void, Void>{
        private CallerDao mAsyncTaskDao;
        getAllCallersAsListAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mCallerList=mAsyncTaskDao.getAllCallersAsList();
            return null;
        }
    }
    //**************************************************************************

    public void callFromCGNet(String primary_key){
        new callFromCGNetAsyncTask(mCallerDao).execute(primary_key);
    }

    private static class callFromCGNetAsyncTask extends AsyncTask<String, Void, Void> {
        private CallerDao mAsyncTaskDao;
        callFromCGNetAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.callFromCGNet(params[0]);
            return null;
        }
    }
    //**************************************************************************

    public void callFromIMI(String primary_key){
        new callFromIMIAsyncTask(mCallerDao).execute(primary_key);
    }

    private static class callFromIMIAsyncTask extends AsyncTask<String, Void, Void> {
        private CallerDao mAsyncTaskDao;
        callFromIMIAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.callFromIMI(params[0]);
            return null;
        }
    }
    //**************************************************************************

    public void updateResponseIMI(String primary_key, String response){
        new responseIMIAsyncTask(mCallerDao).execute(primary_key,response);
    }

    private static class responseIMIAsyncTask extends AsyncTask<String, Void, Void> {
        private CallerDao mAsyncTaskDao;
        responseIMIAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.updateResponseIMI(params[0],params[1]);
            return null;
        }
    }
    //**************************************************************************

    public void updateResponseCGNet(String primary_key, String response){
        new responseCGNetAsyncTask(mCallerDao).execute(primary_key,response);
    }

    private static class responseCGNetAsyncTask extends AsyncTask<String, Void, Void> {
        private CallerDao mAsyncTaskDao;
        responseCGNetAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.updateResponseCGNet(params[0],params[1]);
            return null;
        }
    }
    //**************************************************************************

    public void updateApiDatetime(String primary_key, String datetime){
        new updateApiDatetimeAsyncTask(mCallerDao).execute(primary_key,datetime);
    }

    private static class updateApiDatetimeAsyncTask extends AsyncTask<String, Void, Void> {
        private CallerDao mAsyncTaskDao;
        updateApiDatetimeAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.updateApiDatetime(params[0],params[1]);
            return null;
        }
    }
    //**************************************************************************

    public void updateSuccessfulCallbackDatetime(String primary_key, String datetime){
        new updateSuccessfulCallbackDatetimeAsyncTask(mCallerDao).execute(primary_key,datetime);
    }

    private static class updateSuccessfulCallbackDatetimeAsyncTask extends AsyncTask<String, Void, Void> {
        private CallerDao mAsyncTaskDao;
        updateSuccessfulCallbackDatetimeAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.updateSuccessfulCallbackDatetime(params[0],params[1]);
            return null;
        }
    }
    //**************************************************************************


    public void deleteAnEntry(String CallerToBeDeleted){
        new deleteAnEntryAsyncTask(mCallerDao).execute(CallerToBeDeleted);
    }

    private static class deleteAnEntryAsyncTask extends AsyncTask<String, Void, Void> {
        private CallerDao mAsyncTaskDao;
        deleteAnEntryAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.deleteAnEntry(params[0]);
            return null;
        }
    }
    //**************************************************************************

    public void insert (Caller Caller) {
        new insertAsyncTask(mCallerDao).execute(Caller);
    }
    private static class insertAsyncTask extends AsyncTask<Caller, Void, Void> {
        private CallerDao mAsyncTaskDao;
        insertAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Caller... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    //**************************************************************************

    public void deleteAll(){
        new deleteAsyncTask(mCallerDao).execute();
    }
    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private CallerDao mAsyncTaskDao;
        deleteAsyncTask(CallerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    //**************************************************************************
}
