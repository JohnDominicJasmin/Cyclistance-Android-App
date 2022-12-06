package com.example.cyclistance.core.utils.network_observer

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean


open class SingleLiveEvent<T> : MutableLiveData<T?>() {
    private val mPending: AtomicBoolean = AtomicBoolean(false)
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        if (hasActiveObservers()) {
            Timber.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        super.observe(owner){
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it);
            }
        }

    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}
