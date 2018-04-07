package com.zighter.zighterandroid.view.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public final class ControlsAnimator {

    private final Map<String, ControlsGroup> controlsGroups;
    private boolean isInitialized;
    private Queue<Runnable> runnablesToRunOnInitialization = new LinkedList<>();

    private ControlsAnimator(@NonNull HashMap<String, ControlsGroup> map) {
        this.controlsGroups = map;
    }

    public void initPositions() {
        isInitialized = true;
        for (Map.Entry<String, ControlsGroup> controlsGroup : controlsGroups.entrySet()) {
            controlsGroup.getValue().init();
        }

        while (!runnablesToRunOnInitialization.isEmpty()) {
            runnablesToRunOnInitialization.remove().run();
        }
    }

    public void hideControls(String... keys) {
        Runnable runnable = () -> {
            for (String key : keys) {
                controlsGroups.get(key).hideControls();
            }
        };

        if (isInitialized) {
            runnable.run();
        } else {
            runnablesToRunOnInitialization.add(runnable);
        }
    }

    public void showControls(String... keys) {
        Runnable runnable = () -> {
            for (String key : keys) {
                controlsGroups.get(key).showControls();
            }
        };

        if (isInitialized) {
            runnable.run();
        } else {
            runnablesToRunOnInitialization.add(runnable);
        }
    }

    public enum Axis {
        HORIZONTAL,
        VERTICAL
    }


    public interface PositionRetriever {
        float getPosition(View view);
    }

    private static class ControlsGroup {
        final View[] views;
        final PositionRetriever hidePositionRetriever;
        private final Axis axis;

        ControlsGroup(PositionRetriever hidePositionRetriever, Axis axis, View... views) {
            this.views = views;
            this.hidePositionRetriever = hidePositionRetriever;
            this.axis = axis;
        }

        void init() {
            switch (axis) {
                case VERTICAL:
                    for (View view : views) {
                        view.setTag(view.getY());
                    }
                    break;
                case HORIZONTAL:
                    for (View view : views) {
                        view.setTag(view.getX());
                    }
                    break;
            }
        }

        void hideControls() {
            AnimatorSet animatorSet = new AnimatorSet();
            ArrayList<Animator> hideAnimators = new ArrayList<>();
            switch (axis) {
                case HORIZONTAL:
                    for (View view : views) {
                        hideAnimators.add(AnimationCreator.fastOutTranslateX(
                                view.getX(), hidePositionRetriever.getPosition(view), view));
                    }
                    break;
                case VERTICAL:
                    for (View view : views) {
                        hideAnimators.add(AnimationCreator.fastOutTranslateY(
                                view.getY(), hidePositionRetriever.getPosition(view), view));
                    }
                    break;
            }
            animatorSet.playTogether(hideAnimators);
            animatorSet.setDuration(AnimationCreator.CONTROLS_HIDE_ANIMATION_DURATION);
            animatorSet.start();
        }

        void showControls() {
            AnimatorSet animatorSet = new AnimatorSet();
            ArrayList<Animator> showAnimators = new ArrayList<>();
            switch (axis) {
                case HORIZONTAL:
                    for (View view : views) {
                        if (view.getTag() != null) {
                            showAnimators.add(AnimationCreator.slowInTranslateX(
                                    view.getX(), (float) view.getTag(), view));
                        }
                    }
                    break;
                case VERTICAL:
                    for (View view : views) {
                        if (view.getTag() != null) {
                            showAnimators.add(AnimationCreator.slowInTranslateY(
                                    view.getY(), (float) view.getTag(), view));
                        }
                    }
                    break;
            }
            animatorSet.playTogether(showAnimators);
            animatorSet.setDuration(AnimationCreator.CONTROLS_SHOW_ANIMATION_DURATION);
            animatorSet.start();
        }

    }

    public static class Builder {
        @NonNull
        final HashMap<String, ControlsGroup> controlsGroups;

        public Builder() {
            controlsGroups = new HashMap<>();
        }

        @NonNull
        public Builder addGroup(String key,
                                PositionRetriever hidePositionRetriever,
                                Axis axis,
                                View... views) {
            controlsGroups.put(key, new ControlsGroup(hidePositionRetriever, axis, views));
            return this;
        }

        @NonNull
        public ControlsAnimator build() {
            return new ControlsAnimator(controlsGroups);
        }
    }
}