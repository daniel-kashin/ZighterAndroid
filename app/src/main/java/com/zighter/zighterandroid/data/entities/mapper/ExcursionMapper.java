package com.zighter.zighterandroid.data.entities.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.presentation.Excursion;
import com.zighter.zighterandroid.data.entities.presentation.Guide;
import com.zighter.zighterandroid.data.entities.presentation.Path;
import com.zighter.zighterandroid.data.entities.presentation.Point;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceGuide;
import com.zighter.zighterandroid.data.entities.service.ServiceImage;
import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.service.ServiceSight;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.entities.storage.StorageGuide;
import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;

import java.util.ArrayList;
import java.util.List;

public class ExcursionMapper {
    @NonNull
    private static List<Media> getDummyMediaForSight(@NonNull String excursionUuid,
                                                     @NonNull String sightUuid) {
        List<Media> result = new ArrayList<>();

        if (excursionUuid.equals("7") && sightUuid.equals("0")) {
            result.add(new Video("https://cf-e2.streamablevideo.com/video/mp4/9scrm.mp4?token=1523365759-pdH3aDJd8sgU4H3%2Fvv%2BEt%2FNXiNhTIXcjfCBmccEJW20%3D", null, "Welcome to Pompeu Fabra University – 2017", "UPF is a public, international and intensive-research university that, in just over twenty-five years, has earned a place for itself among the best European universities. Pompeu Fabra University is modern, urban and lives to the rhythm of the city of Barcelona. Discover it! It was designated as an “International Excellence Campus” by the Spanish Ministry of Education in 2010. The university is a modern institution based on the model of the high-powered American research university with controlled admissions and an emphasis on rigorous instruction and research.\n" +
                    "\n" +
                    "An internationally renowned institution, UPF has recently been rated as the top Spanish university by the (London) Times Higher Education Supplement, and it is firmly in the upper echelon of Spanish universities in other rating schemes. Academically, it is the only Spanish and Catalan university along with the Autonomous University of Barcelona among the top 150 best in the world according to Times Higher Education academic classification of universities (140th and 147th place respectively) and it is one of the 7 fastest-rising young universities in the world, according to the same ranking. It ranks first in the national ranking of scientific productivity since 2009.\n" +
                    "\n" +
                    "The university has begun to figure prominently in many international rankings. Its studies on the Economics field have been ranked among the top 50 worldwide, 23rd for Economics and Econometrics in the QS World University Rankings by Subject and 40th for Business & Economics in the Times Higher Education Rankings.\n" +
                    "\n" +
                    "The university's Faculty of Economic and Business Sciences is the first and only faculty in Spain (public or private and for any discipline) to be awarded the Certificate for Quality in Internationalization granted by a consortium of 14 European accreditation agencies.\n" +
                    "\n" +
                    "In 2010, the university was awarded as Campus of International Excellence."));
        }

        return result;
    }

    @NonNull
    public Guide fromService(@NonNull ServiceGuide guide) throws ServerResponseValidationException {
        guide = guide.tryGetValidOrThrowException();

        return new Guide(guide.getUuid(),
                         guide.getUsername(),
                         guide.getFirstName(),
                         guide.getLastName(),
                         guide.getPhone(),
                         guide.getEmail());
    }

    @NonNull
    public Guide fromStorage(@NonNull StorageGuide guide) {
        return new Guide(guide.getUuid(),
                         guide.getUsername(),
                         guide.getFirstName(),
                         guide.getLastName(),
                         guide.getPhone(),
                         guide.getEmail());
    }

    @NonNull
    public StorageGuide toStorage(@NonNull Guide guide) {
        return new StorageGuide(guide.getUuid(),
                                guide.getUsername(),
                                guide.getFirstName(),
                                guide.getLastName(),
                                guide.getPhone(),
                                guide.getEmail());
    }

    @NonNull
    public Excursion fromService(@NonNull ServiceExcursion serviceExcursion) throws ServerResponseValidationException {
        serviceExcursion = serviceExcursion.tryGetValidOrThrowException();

        int sightSize = serviceExcursion.getSightSize();
        List<Sight> sights = new ArrayList<>(sightSize);
        for (int i = 0; i < sightSize; ++i) {
            ServiceSight serviceSight = serviceExcursion.getSightAt(i);

            List<Media> medias = new ArrayList<>();
            if (serviceSight.getImages() != null) {
                List<ServiceImage> images = serviceSight.getImages();
                for (int j = 0; j < images.size(); ++j) {
                    ServiceImage image = images.get(j);
                    medias.add(new Image(image.getUrl(), null, image.getTitle(), image.getDescription()));
                }
            }
            medias.addAll(getDummyMediaForSight(serviceExcursion.getUuid(), serviceSight.getUuid()));

            Sight sight = new Sight(serviceSight.getUuid(),
                                    serviceSight.getLongitude(),
                                    serviceSight.getLatitude(),
                                    serviceSight.getName(),
                                    serviceSight.getType(),
                                    serviceSight.getDescription(),
                                    medias,
                                    serviceSight.getTimetable(),
                                    serviceSight.getPhone(),
                                    serviceSight.getAddress(),
                                    serviceSight.getWebsite());
            sights.add(sight);
        }

        int pathSize = serviceExcursion.getPathSize();
        List<Path> paths = new ArrayList<>(pathSize);
        for (int i = 0; i < pathSize; ++i) {
            ServicePath servicePath = serviceExcursion.getPathAt(i);
            List<Point> points = new ArrayList<>(servicePath.getPointSize());
            for (int j = 0; j < servicePath.getPointSize(); ++j) {
                points.add(new Point(servicePath.getPointAt(j).getLongitude(),
                                     servicePath.getPointAt(j).getLatitude()));
            }
            paths.add(new Path(servicePath.getUuid(), points));
        }

        return new Excursion(serviceExcursion.getUuid(),
                             serviceExcursion.getName(),
                             sights,
                             paths,
                             new Point(serviceExcursion.getWestNorthMapBound().getLongitude(),
                                       serviceExcursion.getWestNorthMapBound().getLatitude()),
                             new Point(serviceExcursion.getEastSouthMapBound().getLongitude(),
                                       serviceExcursion.getEastSouthMapBound().getLatitude()),
                             serviceExcursion.getMaxMapZoom(),
                             serviceExcursion.getMinMapZoom());
    }

    @NonNull
    public List<BoughtExcursion> fromServiceAndStorage(@NonNull List<ServiceBoughtExcursion> serviceBoughtExcursions,
                                                       @Nullable List<StorageBoughtExcursion> storageBoughtExcursions) {
        List<BoughtExcursion> result = new ArrayList<>();

        for (ServiceBoughtExcursion serviceBoughtExcursion : serviceBoughtExcursions) {
            ServiceBoughtExcursion boughtExcursionCopy = serviceBoughtExcursion.tryGetValid(false);
            if (boughtExcursionCopy != null) {
                boolean isGuideSaved = isGuideSaved(boughtExcursionCopy, storageBoughtExcursions);
                boolean isMediaSaved = isMediaSaved(boughtExcursionCopy, storageBoughtExcursions);
                boolean isRouteSaved = isRouteSaved(boughtExcursionCopy, storageBoughtExcursions);
                result.add(fromService(serviceBoughtExcursion, isGuideSaved, isMediaSaved, isRouteSaved));
            }
        }

        return result;
    }

    @NonNull
    public List<BoughtExcursion> fromStorage(@NonNull List<StorageBoughtExcursion> storageBoughtExcursions) {
        List<BoughtExcursion> result = new ArrayList<>(storageBoughtExcursions.size());

        for (StorageBoughtExcursion storageBoughtExcursion : storageBoughtExcursions) {
            result.add(fromStorage(storageBoughtExcursion));
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
    public StorageBoughtExcursion toStorage(@NonNull BoughtExcursion boughtExcursion) {
        return new StorageBoughtExcursion(boughtExcursion.getUuid(),
                                          boughtExcursion.getName(),
                                          boughtExcursion.getLocation(),
                                          boughtExcursion.getOwner(),
                                          boughtExcursion.getOwnerUuid(),
                                          boughtExcursion.getImage().getUrl(),
                                          boughtExcursion.isGuideAvailable(),
                                          boughtExcursion.isMediaAvailable(),
                                          boughtExcursion.isRouteAvailable(),
                                          boughtExcursion.isRouteSaved(),
                                          boughtExcursion.isGuideSaved(),
                                          boughtExcursion.isMediaSaved());
    }

    @NonNull
    public BoughtExcursion fromStorage(@NonNull StorageBoughtExcursion storageBoughtExcursion) {
        return new BoughtExcursion(storageBoughtExcursion.getUuid(),
                                   storageBoughtExcursion.getName(),
                                   storageBoughtExcursion.getOwner(),
                                   storageBoughtExcursion.getOwnerUuid(),
                                   storageBoughtExcursion.getLocation(),
                                   new Image(storageBoughtExcursion.getImageUrl()),
                                   storageBoughtExcursion.isGuideAvailable(),
                                   storageBoughtExcursion.isMediaAvailable(),
                                   storageBoughtExcursion.isRouteAvailable(),
                                   storageBoughtExcursion.isGuideSaved(),
                                   storageBoughtExcursion.isMediaSaved(),
                                   storageBoughtExcursion.isRouteSaved());
    }

    @NonNull
    public BoughtExcursion fromService(@NonNull ServiceBoughtExcursion serviceBoughtExcursion,
                                       boolean isGuideSaved,
                                       boolean isMediaSaved,
                                       boolean isRouteSaved) {
        return new BoughtExcursion(serviceBoughtExcursion.getUuid(),
                                   serviceBoughtExcursion.getName(),
                                   serviceBoughtExcursion.getOwner(),
                                   serviceBoughtExcursion.getOwnerUuid(),
                                   serviceBoughtExcursion.getLocation(),
                                   new Image(serviceBoughtExcursion.getImageUrl()),
                                   serviceBoughtExcursion.isGuideAvailable(),
                                   serviceBoughtExcursion.isMediaAvailable(),
                                   serviceBoughtExcursion.isRouteAvailable(),
                                   isGuideSaved,
                                   isMediaSaved,
                                   isRouteSaved);
    }

    private boolean isGuideSaved(@NonNull ServiceBoughtExcursion serviceBoughtExcursion,
                                 @Nullable List<StorageBoughtExcursion> storageBoughtExcursions) {
        if (storageBoughtExcursions == null) {
            return false;
        }

        for (StorageBoughtExcursion storageBoughtExcursion : storageBoughtExcursions) {
            if (storageBoughtExcursion.getUuid().equals(serviceBoughtExcursion.getUuid())) {
                return storageBoughtExcursion.isGuideSaved();
            }
        }

        return false;
    }

    private boolean isMediaSaved(@NonNull ServiceBoughtExcursion serviceBoughtExcursion,
                                 @Nullable List<StorageBoughtExcursion> storageBoughtExcursions) {
        if (storageBoughtExcursions == null) {
            return false;
        }

        for (StorageBoughtExcursion storageBoughtExcursion : storageBoughtExcursions) {
            if (storageBoughtExcursion.getUuid().equals(serviceBoughtExcursion.getUuid())) {
                return storageBoughtExcursion.isMediaSaved();
            }
        }

        return false;
    }

    private boolean isRouteSaved(@NonNull ServiceBoughtExcursion serviceBoughtExcursion,
                                 @Nullable List<StorageBoughtExcursion> storageBoughtExcursions) {
        if (storageBoughtExcursions == null) {
            return false;
        }

        for (StorageBoughtExcursion storageBoughtExcursion : storageBoughtExcursions) {
            if (storageBoughtExcursion.getUuid().equals(serviceBoughtExcursion.getUuid())) {
                return storageBoughtExcursion.isRouteSaved();
            }
        }

        return false;
    }
}
