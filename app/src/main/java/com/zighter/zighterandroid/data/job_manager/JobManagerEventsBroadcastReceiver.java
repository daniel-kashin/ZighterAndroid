package com.zighter.zighterandroid.data.job_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.zighter.zighterandroid.data.job_manager.JobManagerEventContract.ACTION_ADDED;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventContract.ACTION_CANCELLED;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventContract.ACTION_EXCEPTION;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventContract.ACTION_STARTED;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventContract.ACTION_SUCCESS;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventContract.EXTRA_JOB_SINGLE_ID_WITHOUT_PREFIX;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventsBroadcastReceiver.Event.Added;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventsBroadcastReceiver.Event.Started;
import static com.zighter.zighterandroid.data.job_manager.JobManagerEventsBroadcastReceiver.Event.Success;

public class JobManagerEventsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "JobManagerEventsBR";

    @NonNull
    public static final IntentFilter INTENT_FILTER = new IntentFilter();

    static {
        INTENT_FILTER.addAction(ACTION_ADDED);
        INTENT_FILTER.addAction(ACTION_STARTED);
        INTENT_FILTER.addAction(ACTION_EXCEPTION);
        INTENT_FILTER.addAction(ACTION_SUCCESS);
        INTENT_FILTER.addAction(ACTION_CANCELLED);
    }

    @NonNull
    private final OnEventListener onEventListener;

    public JobManagerEventsBroadcastReceiver(@NonNull OnEventListener onEventListener) {
        this.onEventListener = onEventListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null || intent.getAction() == null) {
            return;
        }

        String jobId = intent.getStringExtra(EXTRA_JOB_SINGLE_ID_WITHOUT_PREFIX);
        if (jobId == null) {
            return;
        }

        Event event = null;
        switch (intent.getAction()) {
            case ACTION_ADDED: {
                Log.d(TAG, "ACTION_ADDED");
                event = Added;
                break;
            }
            case ACTION_STARTED: {
                Log.d(TAG, "ACTION_STARTED");
                event = Started;
                break;
            }
            case ACTION_SUCCESS: {
                Log.d(TAG, "ACTION_SUCCESS");
                event = Success;
                break;
            }
            case ACTION_EXCEPTION: {
                Log.d(TAG, "ACTION_EXCEPTION");
                event = Event.Exception;
                break;
            }
            case ACTION_CANCELLED: {
                Log.d(TAG, "ACTION_CANCELLED");
                event = Event.Cancelled;
                break;
            }
        }
        if (event != null) {
            onEventListener.onNextEvent(jobId, event);
        }
    }

    public enum Event {
        Started,
        Success,
        Exception,
        Cancelled,
        Added
    }

    interface OnEventListener {
        void onNextEvent(@NonNull String jobSingleIdWithoutPrefix, @NonNull Event event);
    }
}



