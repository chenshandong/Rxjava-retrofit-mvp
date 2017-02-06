package com.chensd.funnydemo;

import android.app.Application;

import com.chensd.funnydemo.utils.FileUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by chen on 2017/1/18.
 */
public class MyApplication extends Application {

    private static MyApplication gApp;
    private static final String DB_NAME = "FightImage.realm";
    protected ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Override
    public void onCreate() {
        super.onCreate();
        gApp = (MyApplication) getApplicationContext();
        initRealm();
        createAppDirs();
    }

    private void createAppDirs() {
        FileUtil.createAppDir();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name(DB_NAME)
                .schemaVersion(0)//版本号
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static final MyApplication getGlobalApp(){
        return gApp;
    }

    public ExecutorService getExecutorService(){
        return this.executorService;
    }
}
