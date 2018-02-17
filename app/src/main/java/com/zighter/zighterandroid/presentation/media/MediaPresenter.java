package com.zighter.zighterandroid.presentation.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.entities.excursion.Sight;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import java.util.List;

import io.reactivex.Scheduler;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

@InjectViewState
public class MediaPresenter extends BasePresenter<MediaView> {
    @NonNull
    private final List<DrawableMedia> mediaList;

    private MediaPresenter(@NonNull Sight sight, @NonNull Scheduler worker, @NonNull Scheduler ui) {
        super(worker, ui);
        this.mediaList = sight.getMediasCopy(DrawableMedia.class);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setMedias(mediaList);
    }

    void onMediaPositionChanged(int currentPosition) {
        getViewState().showCurrentMediaPositionChange(currentPosition, mediaList.size());
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
