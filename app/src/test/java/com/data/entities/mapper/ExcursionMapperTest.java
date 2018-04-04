package com.data.entities.mapper;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.mapper.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.presentation.Guide;
import com.zighter.zighterandroid.data.entities.service.ServiceGuide;

import org.junit.Before;

import java.util.Random;

import static junit.framework.Assert.assertEquals;

public class ExcursionMapperTest {

    private ExcursionMapper excursionMapper;

    @Before
    public void before() {
        excursionMapper = new ExcursionMapper();
    }

    public void guideFromService_returnsTheSame() throws Exception {
        ServiceGuide serviceGuide = new ServiceGuide(randomString(), randomString(), randomString(),
                                                     randomString(), randomString(), randomString());
        Guide guide = excursionMapper.fromService(serviceGuide);

        assertEquals(serviceGuide.getEmail(), guide.getEmail());
        assertEquals(serviceGuide.getFirstName(), guide.getFirstName());
        assertEquals(serviceGuide.getLastName(), guide.getLastName());
        assertEquals(serviceGuide.getPhone(), guide.getPhone());
        assertEquals(serviceGuide.getUsername(), guide.getEmail());
        assertEquals(serviceGuide.getEmail(), guide.getEmail());
    }

    @NonNull
    private static String randomString() {
        return "" + new Random().nextDouble();
    }
}
