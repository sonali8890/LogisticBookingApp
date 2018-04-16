package com.sonali.portertestapp.domain;

import com.sonali.portertestapp.view.base.BaseView;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Sonali
 */
public abstract class ApiCallbackWrapper<T extends BaseResponse> extends DisposableObserver<T> {

    private BaseView view;

    public ApiCallbackWrapper(BaseView view) {
        this.view = view;
    }

    @Override
    public void onNext(T t) {
        BaseResponse res = t;
        if (res.isIncomplete_results()) {
            onError(new Throwable("INTERNAL_SERVER_ERROR"));
        } else {
            onSuccess(t);
        }
    }

    public void onError(String title) {
        view.displayError(title);
    }

    @Override
    public void onError(Throwable t) {
        onError(t.getMessage());
    }

    @Override
    public void onComplete() {
    }

    protected abstract void onSuccess(T t);

}
