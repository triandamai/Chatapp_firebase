package com.tdn.data.mapper.realm;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmLiveResult<T extends RealmModel> extends LiveData<RealmResults<T>> {
    private final RealmResults<T> results;

    // The listener will notify the observers whenever a change occurs.
    // The results are modified in change. This could be expanded to also return the change set in a pair.
    private OrderedRealmCollectionChangeListener<RealmResults<T>> listener = new OrderedRealmCollectionChangeListener<RealmResults<T>>() {
        @Override
        public void onChange(@NonNull RealmResults<T> results, @Nullable OrderedCollectionChangeSet changeSet) {
           setValue(results);
        }
    };

    @MainThread
    public RealmLiveResult(@NonNull RealmResults<T> results) {
        //noinspection ConstantConditions
        if (results == null) {
            throw new IllegalArgumentException("Results cannot be null!");
        }
        if (!results.isValid()) {
            throw new IllegalArgumentException("The provided RealmResults is no longer valid, the Realm instance it belongs to is closed. It can no longer be observed for changes.");
        }

        if (results.isLoaded()) {
            // we should not notify observers when results aren't ready yet (async query).
            // however, synchronous query should be set explicitly.
            setValue(results);
        }
        this.results = results;
    }

    /**
     * Starts observing the RealmResults, if it is still valid.
     */
    @Override
    protected void onActive() {
        super.onActive();
        if (results.isValid()) {
            results.addChangeListener(listener);
        }
    }

    /**
     * Stops observing the RealmResults.
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        if (results.isValid()) {
            results.removeChangeListener(listener);
        }
    }
}
