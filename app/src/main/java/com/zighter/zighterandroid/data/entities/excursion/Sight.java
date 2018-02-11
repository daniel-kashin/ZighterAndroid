package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;

import java.io.Serializable;
import java.util.ArrayList;
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

    @NonNull
    public <T extends Media> List<T> getMediasCopy(Class<T> mediaClass) {
        List<T> mediasCopy = new ArrayList<>(medias.size());
        for (Media media : medias) {
            if (mediaClass.isInstance(media)) {
                mediasCopy.add(mediaClass.cast(media));
            }
        }
        return mediasCopy;
    }

    public <T extends Media> int getMediaCount(Class<T> mediaClass) {
        if (!hasMedia()) {
            return 0;
        }

        int count = 0;
        for (Media media : medias) {
            if (mediaClass.isInstance(media)) {
                count++;
            }
        }

        return count;
    }

    @Nullable
    public <T extends  Media> T getFirstMedia(Class<T> mediaClass) {
        if (!hasMedia()) {
            return null;
        }

        T first = null;
        for (Media media : medias) {
            if (mediaClass.isInstance(media)) {
                first = mediaClass.cast(media);
                break;
            }
        }
        return first;
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
