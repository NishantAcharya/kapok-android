package com.kapok.brianramirez.kapok;


import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmObject;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

public class RealmManager {

    public static void add(final RealmObject toAdd){

        SyncConfiguration config = SyncUser.current().createConfiguration(Constants.REALM_URL).waitForInitialRemoteData().build();

        // Open the remote Realm
        RealmAsyncTask realm = Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                // Realm is ready
                realm.beginTransaction();
                realm.insert(toAdd);
                realm.commitTransaction();
            }

            @Override
            public void onError(Throwable exception) {
                // Handle error
                System.out.println("error ---------------");
            }
        });
    }

}
