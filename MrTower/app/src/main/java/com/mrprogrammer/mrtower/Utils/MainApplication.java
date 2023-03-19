package com.mrprogrammer.mrtower.Utils;

import android.app.Application;

import com.bumptech.glide.Glide;

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


        new Thread(() -> Glide.get(getApplicationContext()).clearDiskCache()).start();
    }
}
