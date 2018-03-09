package com.zighter.zighterandroid.data.job_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;
import android.util.Pair;

import com.birbit.android.jobqueue.JobManager;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static com.zighter.zighterandroid.data.job_manager.JobManagerEventsBroadcastReceiver.Event;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventsBroadcastReceiver.Event.Added;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventsBroadcastReceiver.Event.Started;

public class JobManagerWrapper implements JobManagerEventsBroadcastReceiver.OnEventListener {
    private static final String TAG = "JobManagerWrapper";

    @NonNull
    private final JobManager jobManager;
    @NonNull
    private final HashMap<String, Event> jobIdsToActions;
    @NonNull
    private final Subject<Pair<String, Event>> subject;

    public JobManagerWrapper(@NonNull JobManager jobManager) {
        this.jobManager = jobManager;
        this.jobIdsToActions = new HashMap<>();
        this.subject = PublishSubject.<Pair<String, Event>>create().toSerialized();
    }

    @NonNull
    public JobManager getJobManager() {
        return jobManager;
    }

    public boolean isAdded(@NonNull String singleIdWithoutPrefix) {
        synchronized (jobIdsToActions) {
            Log.d(TAG, "isAdded");
            Event lastEvent = jobIdsToActions.get(singleIdWithoutPrefix);
            return lastEvent != null && (lastEvent == Added || lastEvent == Started);
        }
    }

    @NonNull
    public Observable<Pair<String, Event>> subscribeOnJobEvents() {
        synchronized (jobIdsToActions) {
            return subject;
        }
    }

    @UiThread
    public void start(@NonNull Context context) {
        synchronized (jobIdsToActions) {
            BroadcastReceiver receiver = new JobManagerEventsBroadcastReceiver(this);
            context.registerReceiver(receiver, JobManagerEventsBroadcastReceiver.INTENT_FILTER);
            jobManager.start();
        }
    }

    @Override
    public void onNextEvent(@NonNull String jobSingleIdWithoutPrefix, @NonNull Event event) {
        synchronized (jobIdsToActions) {
            jobIdsToActions.put(jobSingleIdWithoutPrefix, event);
            subject.onNext(new Pair<>(jobSingleIdWithoutPrefix, event));
        }
    }
}
