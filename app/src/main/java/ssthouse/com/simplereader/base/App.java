package ssthouse.com.simplereader.base;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

import timber.log.Timber;

/**
 * Created by ssthouse on 2016/9/30.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        ActiveAndroid.initialize(this);
    }
}
