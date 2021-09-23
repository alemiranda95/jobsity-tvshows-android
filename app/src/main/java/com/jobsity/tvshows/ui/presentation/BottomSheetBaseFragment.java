package com.jobsity.tvshows.ui.presentation;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.jobsity.tvshows.R;
import com.jobsity.tvshows.ui.presentation.main.MainActivity;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Merlin;

public abstract class BottomSheetBaseFragment extends BottomSheetDialogFragment implements Connectable {

    protected MainActivity mActivity;
    private Merlin mMerlin;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setOnShowListener(dialogInterface -> {
            View parentView = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (parentView != null) {
                setupFullHeight(parentView);
                BottomSheetBehavior.from(parentView).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(parentView).setSkipCollapsed(true);
            }
        });

        return dialog;
    }

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

    protected void setupFullHeight(View parentView) {
        ViewGroup.LayoutParams layoutParams = parentView.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        parentView.setLayoutParams(layoutParams);
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

    protected void showNoInternetSnackbar(View.OnClickListener onTryAgainListener) {
        Snackbar snackbar = createSnackbar(getString(R.string.no_internet_error));
        snackbar.setAction(R.string.no_internet_snackbar_action_text, onTryAgainListener);
        snackbar.show();
    }

    protected void showErrorSnackbar(String error) {
        createSnackbar(error).show();
    }

    private Snackbar createSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(getDialog().getWindow().getDecorView(),
                text, BaseTransientBottomBar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.darkred));
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(3);
        return snackbar;
    }

    protected abstract View setBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract void setListeners();

    protected abstract void setViewModel();

    protected abstract void setObservers();

}
