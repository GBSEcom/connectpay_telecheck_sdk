
package com.firstdata.sdk.sample.di;

import static com.firstdata.cpsdk.singleton.Base64Kt.buildBase64Provider;
import static com.firstdata.cpsdk.singleton.OkhttpKt.buildOKHttpClient;
import static com.firstdata.sdk.sample.utility.AppConstantKt.APP_BASE_URI;

import javax.inject.Named;
import javax.inject.Singleton;

import com.firstdata.cpsdk.singleton.Base64Provider;
import com.firstdata.cpsdk.singleton.CPApiClient;
import com.firstdata.sdk.sample.ConnectPayApplication;
import com.firstdata.sdk.sample.repo.CPSDKAppRepo;
import com.firstdata.util.network.InternetConnectivityChecker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Application;

import org.jetbrains.annotations.NotNull;

import dagger.Module;
import dagger.Provides;

@Module
/**
 * Dagger module which provides dependencies need for the CP-Sample application.
 */
public class ApplicationModule {

    private final ConnectPayApplication application;

    public ApplicationModule(ConnectPayApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    CPSDKAppRepo provideCPSDKAppRepo() {
        return new CPSDKAppRepo();
    }

    @Provides
    @Singleton
    CPApiClient provideCPApiClient() {

        return new CPApiClient(new InternetConnectivityChecker() {
            @Override
            public boolean hasInternetConnectivity() {
                return true;
            }

            @NotNull
            @Override
            public String noInternetConnectivityMessage() {
                return "Check your Internet connection!";
            }
        }, provideGson(), buildOKHttpClient());
    }

    @Provides
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    Base64Provider provideBase64() {
        return buildBase64Provider();
    }

    @Provides
    @Named("baseUrl")
    String provideBaseUrl() {
        return APP_BASE_URI;
    }
}
