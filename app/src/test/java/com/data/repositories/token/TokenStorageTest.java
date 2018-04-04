package com.data.repositories.token;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;
import com.zighter.zighterandroid.data.repositories.token.TokenStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Random;

import io.reactivex.functions.Predicate;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TokenStorageTest {
    private Context spyContext;
    private TokenStorage tokenStorage;

    @Before
    public void before() {
        spyContext = spy(RuntimeEnvironment.application);
        tokenStorage = new TokenStorage(spyContext);
    }

    @Test
    public void saveToken_getToken_valuesEqual() throws Exception {
        String token = randomString();
        tokenStorage.saveToken(token);

        tokenStorage.getTokenSingle()
                .test()
                .assertComplete()
                .assertValue(token);
    }

    @Test
    public void saveToken_isTokenPresent_returnsTrue() throws Exception {
        String token = randomString();
        tokenStorage.saveToken(token);

        assertEquals(tokenStorage.isTokenPresent(), true);
    }

    @Test
    public void removeToken_getToken_throwsException() throws Exception {
        tokenStorage.saveToken(null);

        tokenStorage.getTokenSingle()
                .test()
                .assertError(error -> error instanceof ServerNotAuthorizedException);
    }

    @Test
    public void saveLogin_getLogin_valuesEqual() throws Exception {
        String token = randomString();
        tokenStorage.saveLogin(token);

        tokenStorage.getLoginSingle()
                .test()
                .assertComplete()
                .assertValue(token);
    }

    @Test
    public void removeLogin_getLogin_throwsException() throws Exception {
        tokenStorage.saveLogin(null);

        tokenStorage.getLoginSingle()
                .test()
                .assertError(error -> error instanceof ServerNotAuthorizedException);
    }

    @NonNull
    private static String randomString() {
        return "" + new Random().nextDouble();
    }
}
