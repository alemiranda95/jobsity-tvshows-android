package com.jobsity.tvshows.ui.presentation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jobsity.tvshows.ui.presentation.main.MainActivity;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Merlin;

public abstract class BaseFragment extends Fragment implements Connectable {

    protected MainActivity mActivity;
    private Merlin mMerlin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setViewModel();
        setObservers();

        View root = setBinding(inflater, container);
        setListeners();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindMerlin();
    }

    public void bindMerlin() {
        if (mMerlin == null) {
            mMerlin = new Merlin.Builder().
                    withConnectableCallbacks().withDisconnectableCallbacks().build(mActivity);
            mMerlin.registerConnectable(this);
        }
        mMerlin.bind();
    }

    protected abstract View setBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract void setListeners();

    protected abstract void setViewModel();

    protected abstract void setObservers();

}
