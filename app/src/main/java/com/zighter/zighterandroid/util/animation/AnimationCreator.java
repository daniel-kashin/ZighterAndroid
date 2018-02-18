package com.zighter.zighterandroid.util.animation;

import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public final class AnimationCreator {
    public static final int CONTROLS_SHOW_ANIMATION_DURATION = 225;
    public static final int CONTROLS_HIDE_ANIMATION_DURATION = 195;

    private AnimationCreator() {
    }

    public static ValueAnimator fastOutTranslateX(float fromX, float toX, View... views) {
        ValueAnimator translateAnimator = ValueAnimator.ofFloat(fromX, toX);
        translateAnimator.setInterpolator(new FastOutLinearInInterpolator());
        translateAnimator.addUpdateListener(valueAnimator -> {
            for (View view : views) {
                view.setX((float) translateAnimator.getAnimatedValue());
            }
        });
        return translateAnimator;
    }

    public static ValueAnimator fastOutTranslateY(float fromY, float toY, View... views) {
        ValueAnimator translateAnimator = ValueAnimator.ofFloat(fromY, toY);
        translateAnimator.setInterpolator(new FastOutLinearInInterpolator());
        translateAnimator.addUpdateListener(valueAnimator -> {
            for (View view : views) {
                view.setY((float) translateAnimator.getAnimatedValue());
            }
        });
        return translateAnimator;
    }

    public static ValueAnimator slowInTranslateX(float fromX, float toX, View... views) {
        ValueAnimator translateAnimator = ValueAnimator.ofFloat(fromX, toX);
        translateAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        translateAnimator.addUpdateListener(valueAnimator -> {
            for (View view : views) {
                view.setX((float) translateAnimator.getAnimatedValue());
            }
        });
        return translateAnimator;
    }

    public static ValueAnimator slowInTranslateY(float fromY, float toY, View... views) {
        ValueAnimator translateAnimator = ValueAnimator.ofFloat(fromY, toY);
        translateAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        translateAnimator.addUpdateListener(valueAnimator -> {
            for (View view : views) {
                view.setY((float) translateAnimator.getAnimatedValue());
            }
        });
        return translateAnimator;
    }

    public static ValueAnimator slowFadeOut(View... views) {
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        alphaAnimator.addUpdateListener(valueAnimator -> {
            for (View view : views) {
                view.setAlpha((float) alphaAnimator.getAnimatedValue());
            }
        });
        return alphaAnimator;
    }

    public static ValueAnimator slowFadeIn(View... views) {
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        alphaAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        alphaAnimator.addUpdateListener(valueAnimator -> {
            for (View view : views) {
                view.setAlpha((float) alphaAnimator.getAnimatedValue());
            }
        });
        return alphaAnimator;
    }

    public static ValueAnimator slowScale(float targetScale, View view) {
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(view.getScaleX(), targetScale);
        scaleAnimator.setInterpolator(new FastOutLinearInInterpolator());
        scaleAnimator.addUpdateListener(valueAnimator -> {
            view.setScaleX((float) scaleAnimator.getAnimatedValue());
            view.setScaleY((float) scaleAnimator.getAnimatedValue());
        });
        return scaleAnimator;
    }

    public static ValueAnimator popOut(View view) {
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0.0f, view.getScaleX());
        scaleAnimator.setInterpolator(new OvershootInterpolator());
        scaleAnimator.addUpdateListener(valueAnimator -> {
            view.setScaleX((float) scaleAnimator.getAnimatedValue());
            view.setScaleY((float) scaleAnimator.getAnimatedValue());
        });
        return scaleAnimator;
    }

    public static ValueAnimator translateX(float toX, View view) {
        ValueAnimator translateXAnimator = ValueAnimator.ofFloat(view.getX(), toX);
        translateXAnimator.setInterpolator(new OvershootInterpolator());
        translateXAnimator.addUpdateListener(valueAnimator -> {
            view.setX((float) valueAnimator.getAnimatedValue());
        });

        return translateXAnimator;
    }

    public static ValueAnimator translateY(float toY, View view) {
        ValueAnimator translateYAnimator = ValueAnimator.ofFloat(view.getY(), toY);
        translateYAnimator.setInterpolator(new OvershootInterpolator());
        translateYAnimator.addUpdateListener(valueAnimator -> {
            view.setY((float) valueAnimator.getAnimatedValue());
        });

        return translateYAnimator;
    }

    public static ValueAnimator slowInInteger(int from, int to,
                                              ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator intAnimator = ValueAnimator.ofInt(from, to);
        intAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        intAnimator.addUpdateListener(listener);
        return intAnimator;
    }
}