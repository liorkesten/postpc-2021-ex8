package huji.postpc.exercises.ex8.data;

import android.app.Application;

import androidx.work.WorkManager;

public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        if (instance == null) {
            instance = new MyApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        WorkManager workManager = WorkManager.getInstance(this);
        workManager.cancelAllWork();
    }
}
