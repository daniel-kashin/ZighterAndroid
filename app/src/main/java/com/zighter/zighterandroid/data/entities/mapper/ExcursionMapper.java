package com.zighter.zighterandroid.data.entities.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.presentation.Excursion;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursion;
import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.service.ServiceSight;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;
import com.zighter.zighterandroid.data.file.FileHelper;

import java.util.ArrayList;
import java.util.List;

public class ExcursionMapper {
    private static final List<Media> DUMMY_MEDIAS = new ArrayList<>(20);
    private static final String DUMMY_DESCRIPTION = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
            "\n" +
            "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham."
            + "\n"
            + "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc."
            + "\n"
            + "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)";

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
    public Excursion fromService(@NonNull ServiceExcursion serviceExcursion)
            throws ServerResponseValidationException {

        serviceExcursion = serviceExcursion.tryGetValidOrThrowException();

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
                             sights,
                             paths,
                             serviceExcursion.getWestNorthMapBound(),
                             serviceExcursion.getEastSouthMapBound(),
                             serviceExcursion.getMaxMapZoom(),
                             serviceExcursion.getMinMapZoom());
    }

    @NonNull
    public List<BoughtExcursion> fromServiceAndStorage(@NonNull List<ServiceBoughtExcursion> serviceBoughtExcursions,
                                                       @Nullable List<StorageBoughtExcursion> storageBoughtExcursions,
                                                       @NonNull FileHelper fileHelper) {
        List<BoughtExcursion> result = new ArrayList<>();

        for (ServiceBoughtExcursion serviceBoughtExcursion : serviceBoughtExcursions) {
            ServiceBoughtExcursion boughtExcursionCopy = serviceBoughtExcursion.tryGetValid(false);
            if (boughtExcursionCopy != null) {
                boolean isFullySaved = isFullySaved(boughtExcursionCopy, storageBoughtExcursions);
                result.add(fromService(serviceBoughtExcursion, isFullySaved, fileHelper));
            }
        }

        return result;
    }

    @NonNull
    public List<BoughtExcursion> fromStorage(@NonNull List<StorageBoughtExcursion> storageBoughtExcursions,
                                              @NonNull FileHelper fileHelper) {
        List<BoughtExcursion> result = new ArrayList<>(storageBoughtExcursions.size());

        for (StorageBoughtExcursion storageBoughtExcursion : storageBoughtExcursions) {
            result.add(fromStorage(storageBoughtExcursion, fileHelper));
        }

        return result;
    }

    @NonNull
    public List<StorageBoughtExcursion> toStorage(@NonNull List<BoughtExcursion> boughtExcursions) {
        List<StorageBoughtExcursion> result = new ArrayList<>(boughtExcursions.size());

        for (BoughtExcursion boughtExcursion : boughtExcursions) {
            result.add(toStorage(boughtExcursion));
        }

        return result;
    }

    @NonNull
    private StorageBoughtExcursion toStorage(@NonNull BoughtExcursion boughtExcursion) {
        String image = boughtExcursion.getImage() != null
                ? boughtExcursion.getImage().getUrl()
                : null;

        return new StorageBoughtExcursion(boughtExcursion.getUuid(),
                                          boughtExcursion.getName(),
                                          boughtExcursion.getLocation(),
                                          boughtExcursion.getOwner(),
                                          image,
                                          boughtExcursion.isGuideAvailable(),
                                          boughtExcursion.isMediaAvailable(),
                                          boughtExcursion.isRouteAvailable(),
                                          boughtExcursion.isFullySaved());
    }

    @NonNull
    public BoughtExcursion fromStorage(@NonNull StorageBoughtExcursion storageBoughtExcursion,
                                        @NonNull FileHelper fileHelper) {
        return new BoughtExcursion(storageBoughtExcursion.getUuid(),
                                   storageBoughtExcursion.getName(),
                                   storageBoughtExcursion.getOwner(),
                                   storageBoughtExcursion.getLocation(),
                                   fileHelper.getImage(storageBoughtExcursion.getImageUrl()),
                                   storageBoughtExcursion.isGuideAvailable(),
                                   storageBoughtExcursion.isMediaAvailable(),
                                   storageBoughtExcursion.isRouteAvailable(),
                                   storageBoughtExcursion.isFullySaved());
    }

    @NonNull
    private BoughtExcursion fromService(@NonNull ServiceBoughtExcursion serviceBoughtExcursion, boolean isFullySaved, @NonNull FileHelper fileHelper) {
        return new BoughtExcursion(serviceBoughtExcursion.getUuid(),
                                   serviceBoughtExcursion.getName(),
                                   serviceBoughtExcursion.getOwner(),
                                   serviceBoughtExcursion.getLocation(),
                                   fileHelper.getImage(serviceBoughtExcursion.getImageUrl()),
                                   serviceBoughtExcursion.isGuideAvailable(),
                                   serviceBoughtExcursion.isMediaAvailable(),
                                   serviceBoughtExcursion.isRouteAvailable(),
                                   isFullySaved);
    }

    private boolean isFullySaved(@NonNull ServiceBoughtExcursion serviceBoughtExcursion,
                                 @Nullable List<StorageBoughtExcursion> storageBoughtExcursions) {
        if (storageBoughtExcursions == null) {
            return false;
        }

        for (StorageBoughtExcursion storageBoughtExcursion : storageBoughtExcursions) {
            if (storageBoughtExcursion.getUuid().equals(serviceBoughtExcursion.getUuid())) {
                return storageBoughtExcursion.isFullySaved() &&
                        (!serviceBoughtExcursion.isGuideAvailable() || storageBoughtExcursion.isGuideAvailable())
                        && (!serviceBoughtExcursion.isMediaAvailable() || storageBoughtExcursion.isMediaAvailable())
                        && (!serviceBoughtExcursion.isRouteAvailable() || storageBoughtExcursion.isRouteAvailable());
            }
        }

        return false;
    }
}
