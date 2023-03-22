package com.mrprogrammer.mrtower.Utils;

import android.app.Application;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration DB = new RealmConfiguration.Builder()
                .name(Const.LocalRealmDb)
                .schemaVersion(1)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(DB);

        // Indicate whether the work finished successfully with the Result
        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();
      //  WorkManager.getInstance(getApplicationContext()).enqueue(mywork);

        new Thread(() -> Glide.get(getApplicationContext()).clearDiskCache()).start();
    }
}
