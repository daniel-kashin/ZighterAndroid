package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;
import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;

import java.util.ArrayList;
import java.util.List;

public class ExcursionMapper {
    private static final List<Media> DUMMY_MEDIAS = new ArrayList<>(10);

    static {
        DUMMY_MEDIAS.add(new Image("https://mfiles.alphacoders.com/680/680627.jpg", null));
        DUMMY_MEDIAS.add(new Image("https://livewallpaperhd.com/wp-content/uploads/2017/05/Barcelona-Wallpaper-Iphone-Hd.jpg", null));
        DUMMY_MEDIAS.add(new Image("https://mfiles.alphacoders.com/680/680627.jpg", null));
        DUMMY_MEDIAS.add(new Image("https://mfiles.alphacoders.com/342/342156.jpg", null));
        DUMMY_MEDIAS.add(new Image("https://livewallpaperhd.com/wp-content/uploads/2017/05/Barcelona-Wallpaper-Iphone-Hd.jpg", null));
        DUMMY_MEDIAS.add(new Image("http://i.imgur.com/CNSEdYc.jpg", null));
        DUMMY_MEDIAS.add(new Image("https://mfiles.alphacoders.com/680/680627.jpg", null));
        DUMMY_MEDIAS.add(new Image("http://i.imgur.com/CNSEdYc.jpg", null));
        DUMMY_MEDIAS.add(new Image("https://livewallpaperhd.com/wp-content/uploads/2017/05/Barcelona-Wallpaper-Iphone-Hd.jpg", null));
        DUMMY_MEDIAS.add(new Image("http://freewallpapersworld.com/ui/images/2/WDF_1858745.jpg", null));
        DUMMY_MEDIAS.add(new Image("http://madrid-direct.com/wp-content/uploads/2016/08/barca02-1024x576.jpg", null));
        DUMMY_MEDIAS.add(new Image("http://madrid-direct.com/wp-content/uploads/2016/08/barca02-1024x576.jpg", null));
        DUMMY_MEDIAS.add(new Image("http://madrid-direct.com/wp-content/uploads/2016/08/barca02-1024x576.jpg", null));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null));
        DUMMY_MEDIAS.add(new Image("http://madrid-direct.com/wp-content/uploads/2016/08/barca02-1024x576.jpg", null));
        DUMMY_MEDIAS.add(new Image("http://spain-dream.ru/wp-content/uploads/2017/12/Otdyh-v-Barselone.jpg", null));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null));
        DUMMY_MEDIAS.add(new Image("https://livewallpaperhd.com/wp-content/uploads/2017/05/Barcelona-Wallpaper-Iphone-Hd.jpg", null));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null));
    }

    @NonNull
    public Excursion fromService(@NonNull ServiceExcursion serviceExcursion) throws ServerResponseValidationException {
        serviceExcursion.tryGetValidOrThrowException();

        int sightSize = serviceExcursion.getSightSize();
        List<Sight> sights = new ArrayList<>(sightSize);
        for (int i = 0; i < sightSize; ++i) {
            ServiceSight serviceSight = serviceExcursion.getSightAt(i);
            Sight sight = new Sight(serviceSight.getUuid(),
                                    serviceSight.getLongitude(),
                                    serviceSight.getLatitude(),
                                    serviceSight.getName(),
                                    serviceSight.getType(),
                                    serviceSight.getDescription(),
                                    DUMMY_MEDIAS);
            sights.add(sight);
        }

        int pathSize = serviceExcursion.getPathSize();
        List<ServicePath> paths = new ArrayList<>(pathSize);
        for (int i = 0; i < pathSize; ++i) {
            paths.add(serviceExcursion.getPathAt(i));
        }

        return new Excursion(serviceExcursion.getUuid(),
                             serviceExcursion.getName(),
                             serviceExcursion.getLastUpdateDatetime(),
                             serviceExcursion.getCreateDatetime(),
                             serviceExcursion.getPubStatus(),
                             serviceExcursion.getPrice(),
                             serviceExcursion.getCurrency(),
                             serviceExcursion.getUserUuid(),
                             sights,
                             paths,
                             serviceExcursion.getWestNorthMapBound(),
                             serviceExcursion.getEastSouthMapBound(),
                             serviceExcursion.getMaxMapZoom(),
                             serviceExcursion.getMinMapZoom());
    }

}
