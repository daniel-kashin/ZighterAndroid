package com.data.entities.mapper;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.mapper.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.presentation.Excursion;
import com.zighter.zighterandroid.data.entities.presentation.Guide;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceGuide;
import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.service.ServicePoint;
import com.zighter.zighterandroid.data.entities.service.ServiceRoute;
import com.zighter.zighterandroid.data.entities.service.ServiceSight;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.entities.storage.StorageGuide;
import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;

public class ExcursionMapperTest {

    private ExcursionMapper excursionMapper;

    @Before
    public void before() {
        excursionMapper = new ExcursionMapper();
    }

    @Test
    public void guide_fromService_returnsTheSame() throws Exception {
        ServiceGuide serviceGuide = new ServiceGuide(randomString(), randomString(), randomString(),
                                                     randomString(), randomString(), randomString());
        Guide guide = excursionMapper.fromService(serviceGuide);

        assertEquals(serviceGuide.getEmail(), guide.getEmail());
        assertEquals(serviceGuide.getFirstName(), guide.getFirstName());
        assertEquals(serviceGuide.getLastName(), guide.getLastName());
        assertEquals(serviceGuide.getPhone(), guide.getPhone());
        assertEquals(serviceGuide.getUsername(), guide.getUsername());
        assertEquals(serviceGuide.getEmail(), guide.getEmail());
    }

    @Test
    public void guide_fromStorage_toStorage_returnsTheSame() throws Exception {
        StorageGuide storageGuide = new StorageGuide(randomString(), randomString(), randomString(),
                                                     randomString(), randomString(), randomString());
        StorageGuide copy = excursionMapper.toStorage(excursionMapper.fromStorage(storageGuide));

        assertEquals(storageGuide.getEmail(), copy.getEmail());
        assertEquals(storageGuide.getFirstName(), copy.getFirstName());
        assertEquals(storageGuide.getLastName(), copy.getLastName());
        assertEquals(storageGuide.getPhone(), copy.getPhone());
        assertEquals(storageGuide.getUsername(), copy.getUsername());
        assertEquals(storageGuide.getEmail(), copy.getEmail());
    }

    @Test
    public void boughtExcursion_fromStorage_toStorage_returnsTheSame() throws Exception {
        StorageBoughtExcursion storageBoughtExcursion = new StorageBoughtExcursion(randomString(),
                                                                                   randomString(),
                                                                                   randomString(),
                                                                                   randomString(),
                                                                                   randomString(),
                                                                                   randomString(),
                                                                                   randomBoolean(),
                                                                                   true,
                                                                                   randomBoolean(),
                                                                                   randomBoolean(),
                                                                                   randomBoolean(),
                                                                                   randomBoolean());
        StorageBoughtExcursion copy = excursionMapper.toStorage(excursionMapper.fromStorage(storageBoughtExcursion));

        assertEquals(storageBoughtExcursion.getImageUrl(), copy.getImageUrl());
        assertEquals(storageBoughtExcursion.getLocation(), copy.getLocation());
        assertEquals(storageBoughtExcursion.getName(), copy.getName());
        assertEquals(storageBoughtExcursion.getOwner(), copy.getOwner());
        assertEquals(storageBoughtExcursion.getOwnerUuid(), copy.getOwnerUuid());
        assertEquals(storageBoughtExcursion.getUuid(), copy.getUuid());
        assertEquals(storageBoughtExcursion.isGuideAvailable(), copy.isGuideAvailable());
        assertEquals(storageBoughtExcursion.isMediaAvailable(), copy.isMediaAvailable());
        assertEquals(storageBoughtExcursion.isRouteAvailable(), copy.isRouteAvailable());
        assertEquals(storageBoughtExcursion.isGuideSaved(), copy.isGuideSaved());
        assertEquals(storageBoughtExcursion.isMediaSaved(), copy.isMediaSaved());
        assertEquals(storageBoughtExcursion.isRouteSaved(), copy.isRouteSaved());
    }

    @Test
    public void boughtExcursion_fromService_returnsTheSame() throws Exception {
        ServiceBoughtExcursion storageBoughtExcursion = new ServiceBoughtExcursion(randomString(),
                                                                                   randomString(),
                                                                                   randomString(),
                                                                                   randomString(),
                                                                                   randomString(),
                                                                                   randomString(),
                                                                                   true,
                                                                                   randomBoolean(),
                                                                                   randomBoolean());
        boolean isMediaSaved = randomBoolean();
        boolean isGuideSaved = randomBoolean();
        boolean isRouteSaved = randomBoolean();

        BoughtExcursion copy = excursionMapper.fromService(storageBoughtExcursion,
                                                           isGuideSaved,
                                                           isMediaSaved,
                                                           isRouteSaved);

        assertEquals(storageBoughtExcursion.getImageUrl(), copy.getImage().getUrl());
        assertEquals(storageBoughtExcursion.getLocation(), copy.getLocation());
        assertEquals(storageBoughtExcursion.getName(), copy.getName());
        assertEquals(storageBoughtExcursion.getOwner(), copy.getOwner());
        assertEquals(storageBoughtExcursion.getOwnerUuid(), copy.getOwnerUuid());
        assertEquals(storageBoughtExcursion.getUuid(), copy.getUuid());
        assertEquals(storageBoughtExcursion.isGuideAvailable(), copy.isGuideAvailable());
        assertEquals(storageBoughtExcursion.isMediaAvailable(), copy.isMediaAvailable());
        assertEquals(storageBoughtExcursion.isRouteAvailable(), copy.isRouteAvailable());
        assertEquals(isGuideSaved, copy.isGuideSaved());
        assertEquals(isMediaSaved, copy.isMediaSaved());
        assertEquals(isRouteSaved, copy.isRouteSaved());
    }

    @Test(expected = ServerResponseValidationException.class)
    public void excursion_fromService_throwsException() throws Exception {
        ServiceExcursion serviceExcursion = new ServiceExcursion(null,
                                                                 null,
                                                                 new ServiceRoute(new ArrayList<>(), new ArrayList<>()),
                                                                 new ServicePoint(randomDouble(), randomDouble()),
                                                                 new ServicePoint(randomDouble(), randomDouble()),
                                                                 new ArrayList<>());
        excursionMapper.fromService(serviceExcursion);
    }

    @Test
    public void excursion_fromService() throws Exception {
        List<ServicePath> servicePaths = new ArrayList<>();
        List<ServicePoint> servicePoints = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            servicePoints.add(new ServicePoint(randomDouble(), randomDouble()));
        }
        servicePaths.add(new ServicePath(randomString(), servicePoints));
        servicePaths.add(new ServicePath(randomString(), servicePoints));

        List<ServiceSight> serviceSights = new ArrayList<>();
        serviceSights.add(new ServiceSight(randomString(), new ServicePoint(randomDouble(), randomDouble()), randomString(), randomString(), randomString()));
        serviceSights.add(new ServiceSight(randomString(), new ServicePoint(randomDouble(), randomDouble()), randomString(), randomString(), randomString()));

        ServiceExcursion serviceExcursion = new ServiceExcursion(randomString(),
                                                                 randomString(),
                                                                 new ServiceRoute(servicePaths, serviceSights),
                                                                 new ServicePoint(randomDouble(), randomDouble()),
                                                                 new ServicePoint(randomDouble(), randomDouble()),
                                                                 null);

        Excursion excursion = excursionMapper.fromService(serviceExcursion);

        // description
        assertEquals(serviceExcursion.getName(), excursion.getName());
        assertEquals(serviceExcursion.getUuid(), excursion.getUuid());
        assertEquals(serviceExcursion.getWestNorthMapBound().getLongitude(), excursion.getWestNorthMapBound().getLongitude());
        assertEquals(serviceExcursion.getWestNorthMapBound().getLatitude(), excursion.getWestNorthMapBound().getLatitude());
        assertEquals(serviceExcursion.getEastSouthMapBound().getLongitude(), excursion.getEastSouthMapBound().getLongitude());
        assertEquals(serviceExcursion.getEastSouthMapBound().getLatitude(), excursion.getEastSouthMapBound().getLatitude());
        assertEquals(serviceExcursion.getMaxMapZoom(), ServiceExcursion.DEFAULT_MAX_MAP_ZOOM);
        assertEquals(serviceExcursion.getMinMapZoom(), ServiceExcursion.DEFAULT_MIN_MAP_ZOOM);

        // sights
        assertEquals(serviceExcursion.getSightSize(), excursion.getSightSize());
        for (int i = 0; i < serviceExcursion.getSightSize(); ++i) {
            ServiceSight serviceSight = serviceExcursion.getSightAt(i);
            Sight sight = excursion.getSightAt(i);

            assertEquals(serviceSight.getAddress(), sight.getAddress());
            assertEquals(serviceSight.getDescription(), sight.getDescription());
            assertEquals(serviceSight.getName(), sight.getName());
            assertEquals(serviceSight.getLatitude(), sight.getLatitude());
            assertEquals(serviceSight.getLongitude(), sight.getLongitude());
            assertEquals(serviceSight.getType(), sight.getType());
            assertEquals(serviceSight.getWebsite(), sight.getWebsite());
            assertEquals(serviceSight.getPhone(), sight.getPhone());
            assertEquals(serviceSight.getUuid(), sight.getUuid());
        }

        // paths
        assertEquals(serviceExcursion.getPathSize(), excursion.getPathSize());
        for (int i = 0; i > serviceExcursion.getPathSize(); ++i) {
            ServicePath servicePath = serviceExcursion.getPathAt(i);
            ServicePath path = excursion.getPathAt(i);

            assertEquals(servicePath.getUuid(), path.getUuid());
            assertEquals(servicePath.getPointSize(), path.getPointSize());
            for (int j = 0; j < servicePath.getPointSize(); ++j) {
                assertEquals(servicePath.getPointAt(j).getLatitude(), path.getPointAt(j).getLatitude());
                assertEquals(servicePath.getPointAt(j).getLongitude(), path.getPointAt(j).getLongitude());
            }
        }
    }



    @NonNull
    private static String randomString() {
        return "" + new Random().nextDouble();
    }

    private static boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    private static double randomDouble() {
        return new Random().nextDouble();
    }
}
