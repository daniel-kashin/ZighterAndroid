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
    private static final String DUMMY_DESCRIPTION = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
            "\n" +
            "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.";

    static {
        DUMMY_MEDIAS.add(new Image("https://mfiles.alphacoders.com/680/680627.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("https://livewallpaperhd.com/wp-content/uploads/2017/05/Barcelona-Wallpaper-Iphone-Hd.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("https://mfiles.alphacoders.com/680/680627.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("https://mfiles.alphacoders.com/342/342156.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("https://livewallpaperhd.com/wp-content/uploads/2017/05/Barcelona-Wallpaper-Iphone-Hd.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("http://i.imgur.com/CNSEdYc.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("https://mfiles.alphacoders.com/680/680627.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("http://i.imgur.com/CNSEdYc.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("https://livewallpaperhd.com/wp-content/uploads/2017/05/Barcelona-Wallpaper-Iphone-Hd.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("http://freewallpapersworld.com/ui/images/2/WDF_1858745.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("http://madrid-direct.com/wp-content/uploads/2016/08/barca02-1024x576.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("http://madrid-direct.com/wp-content/uploads/2016/08/barca02-1024x576.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("http://madrid-direct.com/wp-content/uploads/2016/08/barca02-1024x576.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("http://madrid-direct.com/wp-content/uploads/2016/08/barca02-1024x576.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("http://spain-dream.ru/wp-content/uploads/2017/12/Otdyh-v-Barselone.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Image("https://livewallpaperhd.com/wp-content/uploads/2017/05/Barcelona-Wallpaper-Iphone-Hd.jpg", null, "Title", DUMMY_DESCRIPTION));
        DUMMY_MEDIAS.add(new Video("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", null, "Title", DUMMY_DESCRIPTION));
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
