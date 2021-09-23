package com.jobsity.tvshows.api.network;

import android.content.Context;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.TvShowApp;
import com.jobsity.tvshows.util.exception.NoInternetException;
import com.jobsity.tvshows.util.exception.TvShowException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Scheduler;

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private Context context = TvShowApp.getInstance().getApplicationContext();
    private final RxJavaCallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJavaCallAdapterFactory.create();
    }

    private RxErrorHandlingCallAdapterFactory(Scheduler scheduler) {
        original = RxJavaCallAdapterFactory.createWithScheduler(scheduler);
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    public static CallAdapter.Factory createWithScheduler(Scheduler scheduler) {
        return new RxErrorHandlingCallAdapterFactory(scheduler);
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(context, original.get(returnType, annotations, retrofit));
    }

    private class RxCallAdapterWrapper<T> implements CallAdapter<T, Object> {
        private CallAdapter<T, Object> wrapped;
        private Context context;

        public RxCallAdapterWrapper(Context context, CallAdapter<T,Object> wrapped) {
            this.context = context;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object adapt(Call<T> call) {
            final Observable observable = ((Observable) wrapped.adapt(call)).onErrorResumeNext(throwable -> {
                TvShowException exception;

                if (throwable instanceof UnknownHostException) {
                    exception = new NoInternetException(
                            context.getString(R.string.no_internet_error)
                    );
                } else {
                     exception = new TvShowException(
                             context.getString(R.string.api_error),
                             (Throwable) throwable
                     );
                }

                return Observable.error(exception).first();
            });

            return observable;
        }
    }

}
