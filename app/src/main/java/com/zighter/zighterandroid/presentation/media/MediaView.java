package com.zighter.zighterandroid.presentation.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.data.entities.media.Media;

import java.util.List;

public interface MediaView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setMedias(@NonNull List<DrawableMedia> medias);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showCurrentMediaPosition(int currentPosition, int size, boolean isIconTextShown);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showMediaDescription(@Nullable String name, @NonNull String body);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideMediaDescription(boolean isIconTextShown);
}
