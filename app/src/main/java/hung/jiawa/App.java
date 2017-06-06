package hung.jiawa;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by omar8 on 2017/5/25.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
    }
}