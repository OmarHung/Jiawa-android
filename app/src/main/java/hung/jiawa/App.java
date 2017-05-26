package hung.jiawa;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by omar8 on 2017/5/25.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}