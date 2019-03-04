package com.kapok.brianramirez.kapok;


import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

public class RealmManager {

    static SyncConfiguration config = SyncUser.current().createConfiguration(Constants.REALM_URL).waitForInitialRemoteData().build();

    static Realm realm;

    public RealmManager() {
        realm = Realm.getInstance(config);
    }

    public static void add(RealmObject toAdd){
        realm.beginTransaction();
        realm.insertOrUpdate(toAdd);
        realm.commitTransaction();
    }

}


