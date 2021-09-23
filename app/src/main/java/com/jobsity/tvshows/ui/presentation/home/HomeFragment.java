package com.jobsity.tvshows.ui.presentation.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jobsity.tvshows.ui.adapter.ShowAdapter;
import com.jobsity.tvshows.databinding.FragmentHomeBinding;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.interfaces.SHowAdapterInterface;
import com.jobsity.tvshows.ui.presentation.BaseFragment;
import com.jobsity.tvshows.ui.presentation.showinfo.ShowInfoFragment;
import com.jobsity.tvshows.util.constant.AppConstants;

public class HomeFragment extends BaseFragment
        implements SHowAdapterInterface, View.OnClickListener {

    //ViewModel
    private HomeViewModel mHomeViewModel;

    //Binding
    private FragmentHomeBinding mBinding;

    //Variables
    private ShowAdapter mShowAdapter;

    @Override
    protected View setBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);

        mBinding.recyclerviewShows.setLayoutManager(new GridLayoutManager(getContext(),
                AppConstants.SHOWS_GRID_LAYOUT_COLUMN_COUNT));
        mShowAdapter = new ShowAdapter(getContext(), mHomeViewModel.gethomeViewState().getValue().getShowList(), this);
        mBinding.recyclerviewShows.setAdapter(mShowAdapter);

        return mBinding.getRoot();
    }

    @Override
    protected void setListeners() {
        //Nothing to do
    }

    @Override
    protected void setViewModel() {
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected void setObservers() {
        mHomeViewModel.gethomeViewState().observe(getViewLifecycleOwner(), homeViewState -> {
            if (homeViewState.isShowListChanged()) {
                mShowAdapter.notifyItemRangeChanged(
                        homeViewState.getPreviousShowListSize(),
                        homeViewState.getShowList().size()
                );
            }

            mBinding.layoutLoadingLists.textviewNothingToShow.setVisibility(homeViewState.isNothingToShow() ?
                    View.VISIBLE :
                    View.GONE);

            mBinding.layoutLoadingLists.progressbarLoading.setVisibility(homeViewState.isShowProgress() ?
                    View.VISIBLE :
                    View.GONE);

            if (homeViewState.isShowNoInternetError()) {
                mActivity.showNoInternetSnackbar(this);
            }

            if (homeViewState.getErrorMessage() != null) {
                mActivity.showErrorSnackbar(homeViewState.getErrorMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onShowClick(Show show) {
        ShowInfoFragment.newInstance(show).show(getParentFragmentManager(), null);
    }

    @Override
    public void onLastItemReached() {
        mHomeViewModel.obtainShows();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == com.google.android.material.R.id.snackbar_action) {
            mHomeViewModel.obtainShows();
        }
    }

    @Override
    public void onConnect() {
        mHomeViewModel.obtainShows();
    }
}