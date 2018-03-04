package com.zighter.zighterandroid.data.job_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;
import android.util.Pair;

import com.birbit.android.jobqueue.JobManager;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_ADDED;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_CANCELLED;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_EXCEPTION;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_STARTED;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_SUCCESS;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.EXTRA_EXCURSION;
import static com.zighter.zighterandroid.data.job_manager.JobManagerWrapper.Action.Added;
import static com.zighter.zighterandroid.data.job_manager.JobManagerWrapper.Action.Cancelled;
import static com.zighter.zighterandroid.data.job_manager.JobManagerWrapper.Action.Started;
import static com.zighter.zighterandroid.data.job_manager.JobManagerWrapper.Action.Success;

public class JobManagerWrapper  {
    private static final String TAG = "JobManagerWrapper";

    @NonNull
    private final JobManager jobManager;
    @NonNull
    private final HashMap<String, Action> lastActions;
    @NonNull
    private final Subject<Pair<String, Action>> subject;

    public JobManagerWrapper(@NonNull JobManager jobManager) {
        this.jobManager = jobManager;
        this.lastActions = new HashMap<>();
        this.subject = PublishSubject.<Pair<String, Action>>create().toSerialized();
    }

    @NonNull
    public JobManager getJobManager() {
        return jobManager;
    }

    public boolean isRunning(@NonNull String id) {
        synchronized (lastActions) {
            Log.d(TAG, "isRunning");
            Action lastAction = lastActions.get(id);
            return lastAction != null && (lastAction == Added || lastAction == Started);
        }
    }

    @NonNull
    public Observable<Pair<String, Action>> subscribeOnDownloadExcursionEvents() {
        synchronized (lastActions) {
            return subject;
        }
    }

    @UiThread
    public void start(@NonNull Context context) {
        synchronized (lastActions) {
            BroadcastReceiver receiver = new DownloadExcursionProgressBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_ADDED);
            intentFilter.addAction(ACTION_STARTED);
            intentFilter.addAction(ACTION_EXCEPTION);
            intentFilter.addAction(ACTION_SUCCESS);
            intentFilter.addAction(ACTION_CANCELLED);
            context.registerReceiver(receiver, intentFilter);
            jobManager.start();
        }
    }

    public class DownloadExcursionProgressBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (lastActions) {
                if (context == null || intent == null || intent.getAction() == null) {
                    return;
                }

                BoughtExcursion boughtExcursion = (BoughtExcursion) intent.getSerializableExtra(EXTRA_EXCURSION);
                if (boughtExcursion == null) {
                    return;
                }

                String uuid = boughtExcursion.getUuid();
                Action action = null;
                switch (intent.getAction()) {
                    case ACTION_ADDED: {
                        Log.d(TAG, "ACTION_ADDED");
                        action = Added;
                        break;
                    }
                    case ACTION_STARTED: {
                        Log.d(TAG, "ACTION_STARTED");
                        action = Started;
                        break;
                    }
                    case ACTION_SUCCESS: {
                        Log.d(TAG, "ACTION_SUCCESS");
                        action = Success;
                        break;
                    }
                    case ACTION_EXCEPTION: {
                        Log.d(TAG, "ACTION_EXCEPTION");
                        action = Action.Exception;
                        break;
                    }
                    case ACTION_CANCELLED: {
                        Log.d(TAG, "ACTION_CANCELLED");
                        action = Cancelled;
                        break;
                    }
                }
                if (action != null) {
                    lastActions.put(uuid, action);
                    subject.onNext(new Pair<>(uuid, action));
                }
            }
        }
    }

    public enum Action {
        Started,
        Success,
        Exception,
        Cancelled,
        Added
    }
}
