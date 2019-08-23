
package com.firstdata.sdk.sample.di;

import javax.inject.Singleton;

import com.firstdata.sdk.sample.ConnectPayApplication;
import com.firstdata.sdk.sample.repo.CPSDKAppRepo;
import com.firstdata.sdk.sample.ui.activities.BaseClientActivity;
import com.firstdata.sdk.sample.ui.activities.BaseFragment;

import dagger.Component;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {
        ApplicationModule.class
})
/**
 * Dagger component
 */
public interface ApplicationComponent extends AndroidInjector<ConnectPayApplication> {

    void inject( BaseFragment activity );

    void inject( BaseClientActivity activity );

    void inject(CPSDKAppRepo aRepo);

//     void inject(SessionConfigFragment activity);
//
//     void inject(LaunchConfigFragment activity);
//
//     void inject(UseCaseConfigFragment activity);

}
