package com.data.repositories.excursion;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.zighter.zighterandroid.data.database.DatabaseFactory;
import com.zighter.zighterandroid.data.entities.mapper.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.presentation.Excursion;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursion;
import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.service.ServicePoint;
import com.zighter.zighterandroid.data.entities.service.ServiceRoute;
import com.zighter.zighterandroid.data.entities.service.ServiceSight;
import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionStorage;
import com.zighter.zighterandroid.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class)
public class ExcursionRepositoryTest {
    private ExcursionRepository excursionRepository;
    private ExcursionMapper excursionMapper;

    @Before
    public void before() {
        StorIOSQLite storIOSQLite = DatabaseFactory.create(RuntimeEnvironment.application);
        ExcursionStorage excursionStorage = new ExcursionStorage(storIOSQLite);
        excursionMapper = new ExcursionMapper();
        excursionRepository = new ExcursionRepository(null, // not used
                                                      null, // not used
                                                      excursionStorage,
                                                      excursionMapper,
                                                      null, // not used
                                                      null); // not used
    }

    @Test
    public void saveExcursionToStorage_getExcursionFromStorage_returnsTheSame() throws Exception {
        Excursion excursion = randomExcursion();

        excursionRepository.saveExcursionToStorage(excursion).blockingAwait();
        Optional<Excursion> optionalCopy = excursionRepository.getExcursionFromStorage(excursion.getUuid()).blockingGet();
        Excursion copy = optionalCopy.get();


        // description
        assertFalse(copy == null);
        assertEquals(copy.getName(), excursion.getName());
        assertEquals(copy.getUuid(), excursion.getUuid());
        assertEquals(copy.getWestNorthMapBound().getLongitude(), excursion.getWestNorthMapBound().getLongitude());
        assertEquals(copy.getWestNorthMapBound().getLatitude(), excursion.getWestNorthMapBound().getLatitude());
        assertEquals(copy.getEastSouthMapBound().getLongitude(), excursion.getEastSouthMapBound().getLongitude());
        assertEquals(copy.getEastSouthMapBound().getLatitude(), excursion.getEastSouthMapBound().getLatitude());
        assertEquals(copy.getMaxMapZoom(), ServiceExcursion.DEFAULT_MAX_MAP_ZOOM);
        assertEquals(copy.getMinMapZoom(), ServiceExcursion.DEFAULT_MIN_MAP_ZOOM);

        // sights
        assertEquals(copy.getSightSize(), excursion.getSightSize());
        for (int i = 0; i < copy.getSightSize(); ++i) {
            Sight serviceSight = copy.getSightAt(i);
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
        assertEquals(copy.getPathSize(), excursion.getPathSize());
        for (int i = 0; i > copy.getPathSize(); ++i) {
            ServicePath servicePath = copy.getPathAt(i);
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

    private static double randomDouble() {
        return new Random().nextDouble();
    }

    @NonNull
    private Excursion randomExcursion() throws ServerResponseValidationException {
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
        ServiceExcursion serviceExcursion = new ServiceExcursion(randomString(), randomString(), new ServiceRoute(servicePaths, serviceSights), new ServicePoint(randomDouble(), randomDouble()), new ServicePoint(randomDouble(), randomDouble()), null);
        return excursionMapper.fromService(serviceExcursion);
    }
}
