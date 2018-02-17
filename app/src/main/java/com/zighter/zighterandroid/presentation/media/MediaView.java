package com.zighter.zighterandroid.presentation.media;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;

import java.util.List;

public interface MediaView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setMedias(@NonNull List<DrawableMedia> medias);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showCurrentMediaPositionChange(int currentPosition, int size);
}
