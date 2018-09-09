package com.kapok.brianramirez.kapok;


import io.realm.Realm;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class RealmManager {

    static SyncConfiguration config = new SyncConfiguration.Builder(SyncUser.current(), Constants.REALM_URL).build();

    public static void add(RealmObject toAdd){
        Realm realm = Realm.getInstance(config);

        try {
            realm.beginTransaction();
            realm.copyToRealm(toAdd);
            realm.commitTransaction();
        } catch (Error e) {
            System.out.println("There was an error adding this object to realm. Here's the stack trace:");
            System.out.println(e);
        }
    }

}
