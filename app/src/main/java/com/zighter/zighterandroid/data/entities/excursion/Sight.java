package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;

import java.io.Serializable;
import java.util.List;

public class Sight implements Serializable {
    @NonNull
    private String uuid;
    private double longitude;
    private double latitude;
    @Nullable
    private String description;
    @Nullable
    private String name;
    @Nullable
    private String type;
    @NonNull
    private List<Media> medias;

    @Nullable
    private Image cachedFirstImage;
    private boolean firstImageCached;
    @Nullable
    private Video cachedFirstVideo;
    private boolean firstVideoCached;

    Sight(@NonNull String uuid,
          double longitude,
          double latitude,
          @Nullable String name,
          @Nullable String type,
          @Nullable String description,
          @NonNull List<Media> medias) {
        this.uuid = uuid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.type = type;
        this.medias = medias;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public int getMediaSize() {
        return medias.size();
    }

    public boolean hasMedia() {
        return !medias.isEmpty();
    }

    public int getImageCount() {
        if (!hasMedia()) {
            return 0;
        }

        int imageCount = 0;
        for (Media media : medias) {
            if (media instanceof Image) {
                imageCount++;
            }
        }
        return imageCount;
    }

    @Nullable
    public Image getFirstImage() {
        if (!hasMedia()) {
            return null;
        }

        if (!firstImageCached) {
            for (Media media : medias) {
                if (media instanceof Image) {
                    cachedFirstImage = (Image) media;
                    firstImageCached = true;
                    break;
                }
            }
        }

        return cachedFirstImage;
    }

    public int getVideoCount() {
        if (!hasMedia()) {
            return 0;
        }

        int videoCount = 0;
        for (Media media : medias) {
            if (media instanceof Video) {
                videoCount++;
            }
        }
        return videoCount;
    }

    @Nullable
    public Video getFirstVideo() {
        if (!hasMedia()) {
            return null;
        }

        if (!firstVideoCached) {
            for (Media media : medias) {
                if (media instanceof Video) {
                    cachedFirstVideo= (Video) media;
                    firstVideoCached = true;
                    break;
                }
            }
        }

        return cachedFirstVideo;
    }

    @NonNull
    public Media getMediaAt(int index) {
        return medias.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Sight)) {
            return false;
        }
        Sight that = (Sight) obj;
        return that.getUuid().equals(uuid);
    }
}
