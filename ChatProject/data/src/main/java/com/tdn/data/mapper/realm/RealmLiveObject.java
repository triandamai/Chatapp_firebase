package com.tdn.data.mapper.realm;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import io.realm.ObjectChangeSet;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;

public class RealmLiveObject<T extends RealmModel> extends LiveData<T> {
    // The listener will listen until the object is deleted.
    // An invalidated object shouldn't be set in LiveData, null is set instead.
    private RealmObjectChangeListener<T> listener = new RealmObjectChangeListener<T>() {
        @Override
        public void onChange(@NonNull T object, ObjectChangeSet objectChangeSet) {
            if (!objectChangeSet.isDeleted()) {
                setValue(object);
            } else {
                setValue(null);
            }
        }
    };

    /**
     * Wraps the provided managed RealmObject as a LiveData.
     *
     * The provided object should not be null, should be managed, and should be valid.
     *
     * @param object the managed RealmModel to wrap as LiveData
     */
    @MainThread
    public RealmLiveObject(@NonNull T object) {
        //noinspection ConstantConditions
        if (object == null) {
            throw new IllegalArgumentException("The object cannot be null!");
        }
        if (!RealmObject.isManaged(object)) {
            throw new IllegalArgumentException("LiveRealmObject only supports managed RealmModel instances!");
        }
        if (!RealmObject.isValid(object)) {
            throw new IllegalArgumentException("The provided RealmObject is no longer valid, and therefore cannot be observed for changes.");
        }
        setValue(object);
    }

    // We should start observing and stop observing, depending on whether we have observers.
    // Deleted objects can no longer be observed.
    // We can also no longer observe the object if all local Realm instances on this thread (the UI thread) are closed.

    /**
     * Starts observing the RealmObject, if it is still valid.
     */
    @Override
    protected void onActive() {
        super.onActive();
        T object = getValue();
        if (object != null && RealmObject.isValid(object)) {
            RealmObject.addChangeListener(object, listener);
        }
    }

    /**
     * Stops observing the RealmObject.
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        T object = getValue();
        if (object != null && RealmObject.isValid(object)) {
            RealmObject.removeChangeListener(object, listener);
        }
    }
}
