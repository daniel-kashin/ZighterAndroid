package com.zighter.zighterandroid.presentation.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.entities.excursion.Sight;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;

@InjectViewState
public class MediaPresenter extends BasePresenter<MediaView> {
    @NonNull
    private final Sight sight;

    private MediaPresenter(@NonNull Sight sight, @NonNull Scheduler worker, @NonNull Scheduler ui) {
        super(worker, ui);
        this.sight = sight;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setMedias(sight.getMediasCopy(DrawableMedia.class));
    }

    @Override
    public void onDestroy() {
        getViewState().onFullyDestroy();
        super.onDestroy();
    }

    public static class Builder {
        @NonNull
        private final Scheduler worker;
        @NonNull
        private final Scheduler ui;
        @Nullable
        private Sight sight;

        public Builder(@NonNull Scheduler worker, @NonNull Scheduler ui) {
            this.worker = worker;
            this.ui = ui;
        }

        public Builder sight(@NonNull Sight sight) {
            this.sight = sight;
            return this;
        }

        public MediaPresenter build() {
            if (sight == null) {
                throw new IllegalStateException();
            }

            return new MediaPresenter(sight, worker, ui);
        }
    }
}
